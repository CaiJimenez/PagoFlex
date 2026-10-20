package cl.duocuc.pagoflex.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.duocuc.pagoflex.R
import cl.duocuc.pagoflex.model.SituacionUiState
import cl.duocuc.pagoflex.ui.comoFecha
import cl.duocuc.pagoflex.ui.comoPesos
import cl.duocuc.pagoflex.ui.theme.theme.EstadoVencido
import cl.duocuc.pagoflex.ui.theme.theme.EstadoVencidoFondo
import cl.duocuc.pagoflex.ui.theme.theme.PagoFlexTheme
import cl.duocuc.pagoflex.viewmodel.SituacionViewModel
import java.time.LocalDate

@Composable
fun SituacionScreen(viewModel: SituacionViewModel = viewModel()) {
    val estado by viewModel.uiState.collectAsStateWithLifecycle()
    SituacionContenido(estado)
}

@Composable
fun SituacionContenido(estado: SituacionUiState) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .safeDrawingPadding()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(R.drawable.pagoflex_isotipo),
                contentDescription = "PagoFlex",
                modifier = Modifier.height(28.dp)
            )
            Spacer(Modifier.width(12.dp))
            Text(
                text = "Hola, ${estado.primerNombre}",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        TarjetaResumen(estado)

        // Vencidos, en tono positivo (RN-11): sin "deuda", sin "atrasado"
        if (estado.cantidadVencidos > 0) {
            val texto = if (estado.cantidadVencidos == 1) {
                "Tienes 1 pago para ponerte al día"
            } else {
                "Tienes ${estado.cantidadVencidos} pagos para ponerte al día"
            }
            Text(
                text = texto,
                style = MaterialTheme.typography.labelLarge,
                color = EstadoVencido,
                modifier = Modifier
                    .background(EstadoVencidoFondo, MaterialTheme.shapes.small)
                    .padding(horizontal = 14.dp, vertical = 10.dp)
            )
        }
    }
}

// La tarjeta azul con el total (RF-02)
@Composable
fun TarjetaResumen(estado: SituacionUiState) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = "Total por pagar este mes",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimary
            )
            Text(
                text = estado.totalDelMes.comoPesos(),
                style = MaterialTheme.typography.displaySmall,
                color = MaterialTheme.colorScheme.onPrimary
            )

            val proximo = estado.proximoVencimiento
            Text(
                text = if (proximo != null) {
                    "Próximo vencimiento: ${proximo.comoFecha()}"
                } else {
                    "No tienes vencimientos próximos"
                },
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

// Vista previa en Android Studio, sin abrir el emulador
@Preview(showBackground = true)
@Composable
fun SituacionPreview() {
    PagoFlexTheme() {
        SituacionContenido(
            SituacionUiState(
                primerNombre = "Camila",
                totalDelMes = 633400,
                proximoVencimiento = LocalDate.of(2026, 10, 10),
                cantidadVencidos = 1
            )
        )
    }
}