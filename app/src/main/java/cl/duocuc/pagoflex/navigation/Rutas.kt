package cl.duocuc.pagoflex.navigation

sealed class Rutas(val ruta: String) {

    data object Entrar : Rutas("entrar")

    data object Situacion : Rutas("situacion/{codigo}") {
        fun crear(codigo: String) = "situacion/$codigo"
    }

    data object Reportes : Rutas("reportes")

    data object Empresa : Rutas("empresa")
}
