package cl.duocuc.pagoflex.ui.theme.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cl.duocuc.pagoflex.model.EstadoCompromiso
import cl.duocuc.pagoflex.ui.theme.theme.*

@Composable
fun ChipEstado(estado: EstadoCompromiso) {

    val (colorTexto, colorFondo) = when (estado) {
        EstadoCompromiso.PENDIENTE -> EstadoPendiente to EstadoPendienteFondo
        EstadoCompromiso.VENCIDO -> EstadoVencido to EstadoVencidoFondo
        EstadoCompromiso.PAGADO -> EstadoPagado to EstadoPagadoFondo
        EstadoCompromiso.EN_REVISION -> EstadoEnRevision to EstadoEnRevisionFondo
        EstadoCompromiso.ANULADO -> EstadoAnulado to EstadoAnuladoFondo
    }

    Text(
        text = estado.etiqueta,
        style = MaterialTheme.typography.labelMedium,
        color = colorTexto,
        modifier = Modifier
            .background(colorFondo, CircleShape)
            .padding(horizontal = 10.dp, vertical = 4.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun ChipEstadoPreview() {
    PagoFlexTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            EstadoCompromiso.entries.forEach { ChipEstado(it) }
        }
    }
}