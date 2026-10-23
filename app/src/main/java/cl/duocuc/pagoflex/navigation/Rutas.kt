package cl.duocuc.pagoflex.navigation

sealed class Rutas(val ruta: String) {

    data object Login : Rutas("login")

    data object Inicio : Rutas("inicio/{codigo}") {
        fun crear(codigo: String) = "inicio/$codigo"
    }

}