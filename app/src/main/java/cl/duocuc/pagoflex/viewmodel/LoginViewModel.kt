package cl.duocuc.pagoflex.viewmodel

import androidx.lifecycle.ViewModel
import cl.duocuc.pagoflex.data.DatosPrueba
import cl.duocuc.pagoflex.data.Repositorio
import cl.duocuc.pagoflex.model.LoginUiState
import cl.duocuc.pagoflex.model.Usuario
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class LoginViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onCorreoCambia(valor: String) {
        _uiState.update { it.copy(correo = valor.trim(), errorCorreo = null, errorIngreso = null) }
    }

    fun onContrasenaCambia(valor: String) {
        _uiState.update { it.copy(contrasena = valor, errorContrasena = null, errorIngreso = null) }
    }

    fun onAlternarVisibilidad() {
        _uiState.update { it.copy(contrasenaVisible = !it.contrasenaVisible) }
    }

    fun onIngresar(): Usuario? {
        val estado = _uiState.value

        val errorCorreo = if (estado.correo.isBlank()) {
            "Escribe tu correo"
        } else if (!estado.correo.contains("@") || !estado.correo.contains(".")) {
            "Revisa que el correo esté bien escrito"
        } else {
            null
        }

        val errorContrasena = if (estado.contrasena.isBlank()) {
            "Escribe tu contraseña"
        } else if (!estado.contrasenaValida) {
            "La contraseña no cumple los requisitos"
        } else {
            null
        }

        if (errorCorreo != null || errorContrasena != null) {
            _uiState.update { it.copy(errorCorreo = errorCorreo, errorContrasena = errorContrasena) }
            return null
        }

        val usuario = Repositorio.buscarUsuarioPorCorreo(estado.correo)

        if (usuario == null || estado.contrasena.trim() != DatosPrueba.CLAVE_DEMO) {
            _uiState.update { it.copy(errorIngreso = "El correo o la contraseña no coinciden") }
            return null
        }

        return usuario
    }
}