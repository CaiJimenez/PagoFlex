package cl.duocuc.pagoflex.model

data class LoginUiState(

    val correo: String = "",
    val contrasena: String = "",
    val contrasenaVisible: Boolean = false,
    val errorCorreo: String? = null,
    val errorContrasena: String? = null,
    val errorIngreso: String? = null

) {

    val tieneMayuscula: Boolean
        get() = contrasena.any { it.isUpperCase() }

    val tieneNumero: Boolean
        get() = contrasena.any { it.isDigit() }

    val tieneEspecial: Boolean
        get() = contrasena.any { !it.isLetterOrDigit() }

    val tieneMinimo: Boolean
        get() = contrasena.length >= 8

    val tieneMaximo: Boolean
        get() = contrasena.length <= 15

    val contrasenaValida: Boolean
        get() = tieneMayuscula && tieneNumero && tieneEspecial && tieneMinimo && tieneMaximo

}