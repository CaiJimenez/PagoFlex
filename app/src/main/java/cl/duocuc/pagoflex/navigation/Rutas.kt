package cl.duocuc.pagoflex.navigation

sealed class Rutas(val ruta: String) {

    data object Login : Rutas("login")

    data object Inicio : Rutas("inicio/{codigo}") {
        fun crear(codigo: String) = "inicio/$codigo"
    }

    data object Situacion : Rutas("situacion/{codigo}") {
        fun crear(codigo: String) = "situacion/$codigo"
    }

    data object Detalle : Rutas("detalle/{folio}") {
        fun crear(folio: String) = "detalle/$folio"
    }

    data object Bandeja : Rutas("bandeja/{codigo}") {
        fun crear(codigo: String) = "bandeja/$codigo"
    }
}