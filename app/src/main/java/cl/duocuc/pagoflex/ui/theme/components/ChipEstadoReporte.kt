package cl.duocuc.pagoflex.ui.theme.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cl.duocuc.pagoflex.model.EstadoReporte
import cl.duocuc.pagoflex.ui.theme.theme.*

@Composable

fun ChipEstadoReporte(estado: EstadoReporte) {

    val (colorTexto, colorFondo) = when (estado) {

        EstadoReporte.ABIERTO -> EstadoPendiente to EstadoPendienteFondo
        EstadoReporte.EN_GESTION -> EstadoEnRevision to EstadoEnRevisionFondo
        EstadoReporte.RESUELTO -> EstadoPagado to EstadoPagadoFondo
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