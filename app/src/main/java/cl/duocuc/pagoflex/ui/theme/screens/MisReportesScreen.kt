package cl.duocuc.pagoflex.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.duocuc.pagoflex.data.Repositorio
import cl.duocuc.pagoflex.model.EstadoReporte
import cl.duocuc.pagoflex.model.Reporte
import cl.duocuc.pagoflex.ui.comoFecha
import cl.duocuc.pagoflex.ui.theme.components.ChipEstadoReporte
import cl.duocuc.pagoflex.ui.theme.theme.EstadoPagadoFondo
import cl.duocuc.pagoflex.viewmodel.MisReportesViewModel

@Composable
fun MisReportesScreen(
    codigoUsuario: String,
    onVolver: () -> Unit,
    viewModel: MisReportesViewModel = viewModel()
) {
    val reportes by viewModel.reportes.collectAsStateWithLifecycle()

    val misReportes = reportes
        .filter { Repositorio.compromiso(it.folio)?.codigoUsuario == codigoUsuario }
        .sortedByDescending { it.codigo }

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
            text = "Mis reportes",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground
        )

        if (misReportes.isEmpty()) {
            Text(
                text = "No has reportado problemas. Si algo no calza con un cobro, puedes reportarlo desde su detalle.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
            misReportes.forEach { TarjetaMiReporte(it) }
        }
    }
}

@Composable
fun TarjetaMiReporte(reporte: Reporte) {
    val compromiso = Repositorio.compromiso(reporte.folio)

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
                text = "${compromiso?.empresa?.nombreEmpre ?: ""} · ${compromiso?.concepto ?: ""}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = reporte.motivo.etiqueta,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "Reportado el ${reporte.fecha.comoFecha()}",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            when (reporte.estado) {
                EstadoReporte.ABIERTO -> Text(
                    text = "Recibimos tu reporte. Pronto un agente lo va a revisar.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                EstadoReporte.EN_GESTION -> Text(
                    text = "Un agente de PagoFlex está revisando tu caso.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                EstadoReporte.RESUELTO -> Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(EstadoPagadoFondo, MaterialTheme.shapes.small)
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "Respuesta de PagoFlex",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = reporte.respuesta ?: "",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}