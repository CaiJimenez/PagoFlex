package cl.duocuc.pagoflex.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cl.duocuc.pagoflex.R
import cl.duocuc.pagoflex.data.DatosPrueba
import cl.duocuc.pagoflex.model.Usuario
import cl.duocuc.pagoflex.ui.theme.theme.PagoFlexTheme

@Composable
fun EntrarScreen(
    personas: List<Usuario>,
    onElegir: (Usuario) -> Unit
) {
    val logo = if (isSystemInDarkTheme()) {
        R.drawable.pagoflex_logo_horizontal_fondo_oscuro
    } else {
        R.drawable.pagoflex_logo_horizontal
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .safeDrawingPadding()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Spacer(Modifier.height(32.dp))

        Image(
            painter = painterResource(logo),
            contentDescription = "PagoFlex",
            modifier = Modifier.fillMaxWidth(0.8f)
        )

        Spacer(Modifier.height(16.dp))

        Text(
            text = "Elige con quién entrar",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground
        )

        personas.forEach { persona ->
            TarjetaPersona(
                persona = persona,
                onClick = { onElegir(persona) }
            )
        }
    }
}

@Composable
fun TarjetaPersona(persona: Usuario, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = persona.nombreUser,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "${persona.rol.etiqueta} · RUT ${persona.rutOculto}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EntrarPreview() {
    PagoFlexTheme {
        EntrarScreen(
            personas = DatosPrueba.usuarios.filter { it.codigo in listOf("UF-01", "AG-01", "EJ-01") },
            onElegir = {}
        )
    }
}
