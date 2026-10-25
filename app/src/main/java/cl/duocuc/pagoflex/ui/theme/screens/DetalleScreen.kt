package cl.duocuc.pagoflex.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.duocuc.pagoflex.data.DatosPrueba
import cl.duocuc.pagoflex.model.Compromiso
import cl.duocuc.pagoflex.model.EstadoCompromiso
import cl.duocuc.pagoflex.ui.comoFecha
import cl.duocuc.pagoflex.ui.comoPesos
import cl.duocuc.pagoflex.ui.theme.components.ChipEstado
import cl.duocuc.pagoflex.ui.theme.theme.PagoFlexTheme
import cl.duocuc.pagoflex.viewmodel.DetalleViewModel

@Composable
fun DetalleScreen(
    folio: String,
    onVolver: () -> Unit,
    onReportar: (String) -> Unit,
    viewModel: DetalleViewModel = viewModel()
) {
    val compromisos by viewModel.compromisos.collectAsStateWithLifecycle()
    val compromiso = compromisos.firstOrNull { it.folio == folio }

    if (compromiso != null) {
        DetalleContenido(
            compromiso = compromiso,
            onVolver = onVolver,
            onReportar = { onReportar(compromiso.folio) }
        )
    }
}

@Composable
fun DetalleContenido(
    compromiso: Compromiso,
    onVolver: () -> Unit,
    onReportar: () -> Unit
) {
    var mostrarAvisoPago by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .safeDrawingPadding()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        TextButton(onClick = onVolver) {
            Text("← Volver")
        }

        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(
                text = compromiso.empresa.nombreEmpre,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground
            )
            val cuota = compromiso.cuotaTexto
            Text(
                text = if (cuota != null) "${compromiso.concepto} · Cuota $cuota" else compromiso.concepto,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            ChipEstado(compromiso.estado)
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium,
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                FilaDetalle("Monto original", compromiso.monto.comoPesos())

                if (compromiso.estado == EstadoCompromiso.VENCIDO && compromiso.recargoAplicado > 0) {
                    FilaDetalle("Recargo por atraso", compromiso.recargoAplicado.comoPesos())
                }

                HorizontalDivider()
                FilaDetalle("Total a pagar", compromiso.totalAPagar.comoPesos(), destacado = true)
            }
        }

        val dias = compromiso.diasAtraso(DatosPrueba.hoy)
        Text(
            text = when {
                compromiso.estado == EstadoCompromiso.VENCIDO && dias == 1L ->
                    "Venció el ${compromiso.fechaVencimiento.comoFecha()} (hace 1 día)"
                compromiso.estado == EstadoCompromiso.VENCIDO ->
                    "Venció el ${compromiso.fechaVencimiento.comoFecha()} (hace $dias días)"
                else ->
                    "Vence el ${compromiso.fechaVencimiento.comoFecha()}"
            },
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground
        )

        if (compromiso.sePuedePagar) {
            Button(
                onClick = { mostrarAvisoPago = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Text("Pagar ${compromiso.totalAPagar.comoPesos()}")
            }

            OutlinedButton(
                onClick = onReportar,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Text("Reportar un problema")
            }
        } else {
            Text(
                text = when (compromiso.estado) {
                    EstadoCompromiso.EN_REVISION ->
                        "Estamos revisando el problema que nos reportaste. Te avisaremos cuando tengamos una respuesta."
                    EstadoCompromiso.ANULADO ->
                        "La empresa dejó sin efecto este cobro. No necesitas pagarlo."
                    else ->
                        "Este compromiso ya está pagado. ¡Gracias!"
                },
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }

    if (mostrarAvisoPago) {
        AlertDialog(
            onDismissRequest = { mostrarAvisoPago = false },
            title = { Text("¡Muy pronto!") },
            text = {
                Text("Pronto podrás pagar desde la app. Por ahora puedes hacerlo en el portal de pagos o con el agente conversacional.")
            },
            confirmButton = {
                TextButton(onClick = { mostrarAvisoPago = false }) { Text("Entendido") }
            }
        )
    }
}

@Composable
fun FilaDetalle(etiqueta: String, valor: String, destacado: Boolean = false) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = etiqueta,
            style = if (destacado) MaterialTheme.typography.titleMedium else MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = valor,
            style = if (destacado) MaterialTheme.typography.titleLarge else MaterialTheme.typography.bodyLarge,
            fontWeight = if (destacado) FontWeight.Bold else FontWeight.Normal,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DetallePreview() {
    PagoFlexTheme {
        DetalleContenido(
            compromiso = DatosPrueba.compromisos.first { it.folio == "CP-2026-0102" },
            onVolver = {},
            onReportar = {}
        )
    }
}