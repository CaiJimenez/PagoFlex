package cl.duocuc.pagoflex.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.duocuc.pagoflex.data.Repositorio
import cl.duocuc.pagoflex.model.MotivoReporte
import cl.duocuc.pagoflex.model.Reporte
import cl.duocuc.pagoflex.model.ReportarUiState
import cl.duocuc.pagoflex.ui.comoPesos
import cl.duocuc.pagoflex.ui.theme.theme.EstadoPagado
import cl.duocuc.pagoflex.ui.theme.theme.EstadoPagadoFondo
import cl.duocuc.pagoflex.ui.theme.theme.PagoFlexTheme
import cl.duocuc.pagoflex.viewmodel.ReportarViewModel

@Composable
fun ReportarScreen(

    folio: String,
    onVolver: () -> Unit,
    onTerminar: () -> Unit,
    viewModel: ReportarViewModel = viewModel()
) {
    val estado by viewModel.uiState.collectAsStateWithLifecycle()
    val compromiso = Repositorio.compromiso(folio)
    val resumen = if (compromiso != null) {
        "${compromiso.empresa.nombreEmpre} · ${compromiso.totalAPagar.comoPesos()}"
    } else ""

    val reporte = estado.reporteCreado
    if (reporte != null) {
        ReporteEnviadoContenido(reporte = reporte, onTerminar = onTerminar)
    } else {
        ReportarContenido(
            estado = estado,
            resumen = resumen,
            onMotivoElegido = viewModel::onMotivoElegido,
            onComentarioCambia = viewModel::onComentarioCambia,
            onEnviar = { viewModel.onEnviar(folio) },
            onVolver = onVolver
        )
    }
}

@Composable
fun ReportarContenido(
    estado: ReportarUiState,
    resumen: String,
    onMotivoElegido: (MotivoReporte) -> Unit,
    onComentarioCambia: (String) -> Unit,
    onEnviar: () -> Unit,
    onVolver: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .safeDrawingPadding()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        TextButton(onClick = onVolver) { Text("← Volver") }

        Text(
            text = "Reportar un problema",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = resumen,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Text(
            text = "¿Qué pasó?",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

        Column(modifier = Modifier.selectableGroup()) {
            MotivoReporte.entries.forEach { motivo ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .selectable(
                            selected = estado.motivo == motivo,
                            onClick = { onMotivoElegido(motivo) },
                            role = Role.RadioButton
                        )
                        .padding(vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(selected = estado.motivo == motivo, onClick = null)
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = motivo.etiqueta,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }
        if (estado.errorMotivo != null) {
            Text(
                text = estado.errorMotivo,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.error
            )
        }

        OutlinedTextField(
            value = estado.comentario,
            onValueChange = onComentarioCambia,
            label = { Text("Cuéntanos qué pasó") },
            minLines = 4,
            isError = estado.errorComentario != null,
            supportingText = {
                Text(estado.errorComentario ?: "${estado.comentario.length}/300")
            },
            modifier = Modifier.fillMaxWidth()
        )

        Text(
            text = "Mientras revisamos tu reporte, este cobro quedará \"En revisión\" y no podrás pagarlo.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Button(
            onClick = onEnviar,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
        ) {
            Text("Enviar reporte")
        }
    }
}

@Composable
fun ReporteEnviadoContenido(reporte: Reporte, onTerminar: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .safeDrawingPadding()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
    ) {
        Box(
            modifier = Modifier
                .size(96.dp)
                .background(EstadoPagadoFondo, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text("✓", fontSize = 48.sp, color = EstadoPagado)
        }

        Text(
            text = "¡Recibimos tu reporte!",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )
        Text(
            text = "Tu número de reporte es ${reporte.codigo}. Un agente de PagoFlex lo revisará y te responderá aquí mismo. Puedes seguirlo en \"Mis reportes\".",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        Button(
            onClick = onTerminar,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
        ) {
            Text("Volver a mis compromisos")
        }
    }
}

@Preview(showBackground = true, heightDp = 850)
@Composable
fun ReportarPreview() {
    PagoFlexTheme {
        ReportarContenido(
            estado = ReportarUiState(motivo = MotivoReporte.YA_PAGUE),
            resumen = "Club Los Halcones · \$17.000",
            onMotivoElegido = {},
            onComentarioCambia = {},
            onEnviar = {},
            onVolver = {}
        )
    }
}