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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.duocuc.pagoflex.data.DatosPrueba
import cl.duocuc.pagoflex.data.Repositorio
import cl.duocuc.pagoflex.model.EstadoReporte
import cl.duocuc.pagoflex.model.Reporte
import cl.duocuc.pagoflex.ui.comoFecha
import cl.duocuc.pagoflex.ui.theme.components.ChipEstadoReporte
import cl.duocuc.pagoflex.viewmodel.MisCasosViewModel

@Composable
fun MisCasosScreen(
    codigoAgente: String,
    onVolver: () -> Unit,
    viewModel: MisCasosViewModel = viewModel()
) {
    val reportes by viewModel.reportes.collectAsStateWithLifecycle()

    val misCasos = reportes
        .filter { it.codigoAdministrador == codigoAgente }
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
            text = "Mis casos",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground
        )

        if (misCasos.isEmpty()) {
            Text(
                text = "Todavía no tienes casos. Toma uno desde la Bandeja de reportes.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
            misCasos.forEach { reporte ->
                TarjetaCaso(
                    reporte = reporte,
                    onResolver = { respuesta, seCorrige ->
                        viewModel.onResolver(reporte.codigo, respuesta, seCorrige)
                    }
                )
            }
        }
    }
}

@Composable
fun TarjetaCaso(reporte: Reporte, onResolver: (String, Boolean) -> Unit) {
    val compromiso = Repositorio.compromiso(reporte.folio)
    val usuario = DatosPrueba.usuarios.firstOrNull { it.codigo == compromiso?.codigoUsuario }

    var respuesta by rememberSaveable(reporte.codigo) { mutableStateOf("") }
    var error by remember(reporte.codigo) { mutableStateOf<String?>(null) }

    fun resolver(seCorrige: Boolean) {
        if (respuesta.trim().length < 10) {
            error = "Escribe una respuesta para el usuario (mínimo 10 caracteres)"
        } else {
            onResolver(respuesta.trim(), seCorrige)
        }
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
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
                text = "“${reporte.comentario}” · ${reporte.fecha.comoFecha()}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            if (reporte.estado == EstadoReporte.EN_GESTION) {
                OutlinedTextField(
                    value = respuesta,
                    onValueChange = { respuesta = it.take(300); error = null },
                    label = { Text("Respuesta para el usuario") },
                    minLines = 3,
                    isError = error != null,
                    supportingText = { Text(error ?: "${respuesta.length}/300") },
                    modifier = Modifier.fillMaxWidth()
                )

                Text(
                    text = "¿Cuál fue el resultado?",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Button(
                    onClick = { resolver(seCorrige = true) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Se corrige el cobro")
                }
                OutlinedButton(
                    onClick = { resolver(seCorrige = false) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Se mantiene el cobro")
                }
            } else if (reporte.estado == EstadoReporte.RESUELTO) {
                Text(
                    text = "Tu respuesta: ${reporte.respuesta ?: ""}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}