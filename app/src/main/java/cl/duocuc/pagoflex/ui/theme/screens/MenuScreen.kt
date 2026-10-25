package cl.duocuc.pagoflex.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cl.duocuc.pagoflex.R
import cl.duocuc.pagoflex.data.DatosPrueba
import cl.duocuc.pagoflex.data.Menus
import cl.duocuc.pagoflex.model.OpcionMenu
import cl.duocuc.pagoflex.model.Usuario
import cl.duocuc.pagoflex.ui.theme.theme.PagoFlexTheme

@Composable
fun MenuScreen(
    usuario: Usuario,
    opciones: List<OpcionMenu>,
    onAbrir: (String) -> Unit,
    onSalir: () -> Unit
) {
    var opcionBloqueada by remember { mutableStateOf<OpcionMenu?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .safeDrawingPadding()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(R.drawable.pagoflex_isotipo),
                contentDescription = "PagoFlex",
                modifier = Modifier.height(32.dp)
            )
            Spacer(Modifier.width(12.dp))
            Column {
                Text(
                    text = "Hola, ${usuario.primerNombre}",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = usuario.rol.etiqueta,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(Modifier.height(4.dp))

        opciones.chunked(2).forEach { fila ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                fila.forEach { opcion ->
                    TarjetaOpcion(
                        opcion = opcion,
                        onClick = {
                            if (opcion.disponible) onAbrir(opcion.id) else opcionBloqueada = opcion
                        },
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                    )
                }
                // Si la fila tiene una sola opción, se rellena el espacio vacío
                if (fila.size == 1) Spacer(Modifier.weight(1f))
            }
        }

        Spacer(Modifier.height(8.dp))

        OutlinedButton(
            onClick = onSalir,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
        ) {
            Text("Cerrar sesión")
        }
    }

    opcionBloqueada?.let { opcion ->
        AlertDialog(
            onDismissRequest = { opcionBloqueada = null },
            title = { Text(opcion.titulo) },
            text = { Text("¡Muy pronto vas a poder usar esta función en la app!") },
            confirmButton = {
                TextButton(onClick = { opcionBloqueada = null }) { Text("Entendido") }
            }
        )
    }
}

@Composable

fun TarjetaOpcion(opcion: OpcionMenu, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val colorIcono = if (opcion.disponible) MaterialTheme.colorScheme.primary
    else MaterialTheme.colorScheme.onSurfaceVariant

    Card(

        onClick = onClick,
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = if (opcion.disponible) 2.dp else 0.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(
                painter = painterResource(opcion.icono),
                contentDescription = null,
                tint = colorIcono,
                modifier = Modifier.size(30.dp)
            )
            Text(
                text = opcion.titulo,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = opcion.descripcion,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            if (!opcion.disponible) {

                Text(
                    text = "Próximamente",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.background, CircleShape)
                        .padding(horizontal = 10.dp, vertical = 3.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true, heightDp = 900)

@Composable
fun MenuPreview() {

    PagoFlexTheme {
        val camila = DatosPrueba.usuarios.first { it.codigo == "UF-01" }
        MenuScreen(
            usuario = camila,
            opciones = Menus.para(camila.rol),
            onAbrir = {},
            onSalir = {}
        )
    }
}