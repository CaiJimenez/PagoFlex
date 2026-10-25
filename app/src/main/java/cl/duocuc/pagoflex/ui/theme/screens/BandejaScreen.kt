package cl.duocuc.pagoflex.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.duocuc.pagoflex.data.DatosPrueba
import cl.duocuc.pagoflex.data.Repositorio
import cl.duocuc.pagoflex.model.EstadoReporte
import cl.duocuc.pagoflex.model.Reporte
import cl.duocuc.pagoflex.ui.comoFecha
import cl.duocuc.pagoflex.ui.theme.components.ChipEstadoReporte
import cl.duocuc.pagoflex.ui.theme.theme.EstadoVencido
import cl.duocuc.pagoflex.ui.theme.theme.PagoFlexTheme
import cl.duocuc.pagoflex.viewmodel.BandejaViewModel
import java.time.DayOfWeek
import java.time.LocalDate

@Composable
fun BandejaScreen(
    codigoAgente: String,
    onVolver: () -> Unit,
    viewModel: BandejaViewModel = viewModel()
) {
    val reportes by viewModel.reportes.collectAsStateWithLifecycle()
    val filtro by viewModel.filtro.collectAsStateWithLifecycle()

    BandejaContenido(
        reportes = reportes,
        filtro = filtro,
        onFiltroCambia = viewModel::onFiltroCambia,
        onTomar = { reporte -> viewModel.onTomar(reporte.codigo, codigoAgente) },
        onVolver = onVolver
    )
}

@Composable
fun BandejaContenido(
    reportes: List<Reporte>,
    filtro: EstadoReporte?,
    onFiltroCambia: (EstadoReporte?) -> Unit,
    onTomar: (Reporte) -> Unit,
    onVolver: () -> Unit
) {
    val visibles = reportes
        .filter { filtro == null || it.estado == filtro }
        .sortedWith(compareBy<Reporte> { it.estado.ordinal }.thenByDescending { it.fecha })

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .safeDrawingPadding()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        TextButton(onClick = onVolver) { Text("← Volver") }

        Text(
            text = "Bandeja de reportes",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground
        )

        Row(
            modifier = Modifier.horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = filtro == null,
                onClick = { onFiltroCambia(null) },
                label = { Text("Todos (${reportes.size})") }
            )
            EstadoReporte.entries.forEach { estado ->
                FilterChip(
                    selected = filtro == estado,
                    onClick = { onFiltroCambia(estado) },
                    label = { Text("${estado.etiqueta} (${reportes.count { it.estado == estado }})") }
                )
            }
        }

        if (visibles.isEmpty()) {
            Text(
                text = "No hay reportes en este estado 🎉",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
            visibles.forEach { reporte ->
                TarjetaReporteAgente(reporte = reporte, onTomar = { onTomar(reporte) })
            }
        }
    }
}

@Composable
fun TarjetaReporteAgente(reporte: Reporte, onTomar: () -> Unit) {

    val compromiso = Repositorio.compromiso(reporte.folio)
    val usuario = DatosPrueba.usuarios.firstOrNull { it.codigo == compromiso?.codigoUsuario }
    val agente = DatosPrueba.usuarios.firstOrNull { it.codigo == reporte.codigoAdministrador }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = reporte.codigo,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                )
                Spacer(Modifier.width(8.dp))
                ChipEstadoReporte(reporte.estado)
            }

            Text(
                text = "${usuario?.nombreUser ?: "Usuario"} · ${compromiso?.empresa?.nombreEmpre ?: ""}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
                text = reporte.motivo.etiqueta,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = "“${reporte.comentario}”",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = reporte.fecha.comoFecha(),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            val dias = diasHabilesDesde(reporte.fecha, DatosPrueba.hoy)
            if (reporte.estado == EstadoReporte.ABIERTO && dias > 2) {
                Text(
                    text = "Lleva $dias días hábiles sin gestionar",
                    style = MaterialTheme.typography.labelLarge,
                    color = EstadoVencido
                )
            }

            if (agente != null) {
                Text(
                    text = "Tomado por ${agente.primerNombre}",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            if (reporte.estado == EstadoReporte.ABIERTO) {
                Button(
                    onClick = onTomar,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Tomar caso")
                }
            }
        }
    }
}

private fun diasHabilesDesde(fecha: LocalDate, hoy: LocalDate): Int {

    var dias = 0
    var dia = fecha.plusDays(1)
    while (!dia.isAfter(hoy)) {
        if (dia.dayOfWeek != DayOfWeek.SATURDAY && dia.dayOfWeek != DayOfWeek.SUNDAY) dias++
        dia = dia.plusDays(1)
    }
    return dias
}

@Preview(showBackground = true, heightDp = 900)
@Composable

fun BandejaPreview() {
    PagoFlexTheme {
        BandejaContenido(
            reportes = DatosPrueba.reportes,
            filtro = null,
            onFiltroCambia = {},
            onTomar = {},
            onVolver = {}
        )
    }
}