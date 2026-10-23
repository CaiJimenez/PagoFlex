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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.duocuc.pagoflex.R
import cl.duocuc.pagoflex.model.LoginUiState
import cl.duocuc.pagoflex.model.Usuario
import cl.duocuc.pagoflex.ui.theme.theme.EstadoPagado
import cl.duocuc.pagoflex.ui.theme.theme.PagoFlexTheme
import cl.duocuc.pagoflex.viewmodel.LoginViewModel

@Composable
fun LoginScreen(
    onIngresoExitoso: (Usuario) -> Unit,
    viewModel: LoginViewModel = viewModel()
) {
    val estado by viewModel.uiState.collectAsStateWithLifecycle()

    LoginContenido(
        estado = estado,
        onCorreoCambia = viewModel::onCorreoCambia,
        onContrasenaCambia = viewModel::onContrasenaCambia,
        onAlternarVisibilidad = viewModel::onAlternarVisibilidad,
        onIngresar = {
            val usuario = viewModel.onIngresar()
            if (usuario != null) onIngresoExitoso(usuario)
        }
    )
}

@Composable
fun LoginContenido(
    estado: LoginUiState,
    onCorreoCambia: (String) -> Unit,
    onContrasenaCambia: (String) -> Unit,
    onAlternarVisibilidad: () -> Unit,
    onIngresar: () -> Unit
) {
    var mostrarAyuda by remember { mutableStateOf(false) }

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
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Spacer(Modifier.height(24.dp))

        Image(
            painter = painterResource(logo),
            contentDescription = "PagoFlex",
            modifier = Modifier.fillMaxWidth(0.75f)
        )

        Text(
            text = "Hola, qué bueno verte de nuevo",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )

        OutlinedTextField(
            value = estado.correo,
            onValueChange = onCorreoCambia,
            label = { Text("Correo") },
            singleLine = true,
            isError = estado.errorCorreo != null,
            supportingText = estado.errorCorreo?.let { { Text(it) } },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = estado.contrasena,
            onValueChange = onContrasenaCambia,
            label = { Text("Contraseña") },
            singleLine = true,
            isError = estado.errorContrasena != null,
            supportingText = estado.errorContrasena?.let { { Text(it) } },
            visualTransformation = if (estado.contrasenaVisible) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            trailingIcon = {
                TextButton(onClick = onAlternarVisibilidad) {
                    Text(if (estado.contrasenaVisible) "Ocultar" else "Mostrar")
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            modifier = Modifier.fillMaxWidth()
        )

        if (estado.contrasena.isNotEmpty()) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Requisito("Una mayúscula", estado.tieneMayuscula)
                Requisito("Un número", estado.tieneNumero)
                Requisito("Un carácter especial (# @ ! \$)", estado.tieneEspecial)
                Requisito("Mínimo 8 caracteres", estado.tieneMinimo)
                Requisito("Máximo 15 caracteres", estado.tieneMaximo)
            }
        }

        if (estado.errorIngreso != null) {
            Text(
                text = estado.errorIngreso,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.error,
                textAlign = TextAlign.Center
            )
        }

        Button(
            onClick = onIngresar,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
        ) {
            Text("Ingresar")
        }

        TextButton(onClick = { mostrarAyuda = true }) {
            Text("¿Olvidaste tu contraseña?")
        }
    }

    if (mostrarAyuda) {
        AlertDialog(
            onDismissRequest = { mostrarAyuda = false },
            title = { Text("Recuperar contraseña") },
            text = { Text("Escríbenos a soporte@pagoflex.ejemplo y te ayudaremos a recuperar tu acceso.") },
            confirmButton = {
                TextButton(onClick = { mostrarAyuda = false }) { Text("Entendido") }
            }
        )
    }
}

@Composable
fun Requisito(texto: String, cumplido: Boolean) {
    Text(
        text = (if (cumplido) "✓  " else "○  ") + texto,
        style = MaterialTheme.typography.bodyMedium,
        color = if (cumplido) EstadoPagado else MaterialTheme.colorScheme.onSurfaceVariant
    )
}

@Preview(showBackground = true)
@Composable
fun LoginPreview() {
    PagoFlexTheme {
        LoginContenido(
            estado = LoginUiState(correo = "crojas@correo.ejemplo", contrasena = "PagoFlex2026"),
            onCorreoCambia = {},
            onContrasenaCambia = {},
            onAlternarVisibilidad = {},
            onIngresar = {}
        )
    }
}