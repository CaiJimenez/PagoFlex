package cl.duocuc.pagoflex.ui.theme.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.platform.LocalContext

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

private val EsquemaClaro = lightColorScheme(

    primary = AzulPetroleo,
    onPrimary = Blanco,
    primaryContainer = AzulPetroleoClaro,
    onPrimaryContainer = AzulMarino,
    secondary = VerdePago,
    onSecondary = Blanco,
    secondaryContainer = VerdePagoClaro,
    onSecondaryContainer = VerdePago,
    tertiary = VerdeLima,
    onTertiary = AzulMarino,
    background = FondoClaro,
    onBackground = AzulMarino,
    surface = SuperficieClara,
    onSurface = AzulMarino,
    onSurfaceVariant = TextoSecundarioClaro,
    outline = BordeClaro

)

private val EsquemaOscuro = darkColorScheme(

    primary = AzulPetroleoNoche,
    onPrimary = FondoOscuro,
    primaryContainer = AzulPetroleo,
    onPrimaryContainer = TextoClaro,
    secondary = VerdePagoNoche,
    onSecondary = FondoOscuro,
    tertiary = VerdeLimaNoche,
    onTertiary = FondoOscuro,
    background = FondoOscuro,
    onBackground = TextoClaro,
    surface = SuperficieOscura,
    onSurface = TextoClaro,
    onSurfaceVariant = TextoSecundarioOscuro

)

private val Formas = Shapes(

    small = RoundedCornerShape(10.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(24.dp)

)

@Composable
fun PagoFlexTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) EsquemaOscuro else EsquemaClaro,
        typography = Tipografia,
        shapes = Formas,
        content = content
    )
}