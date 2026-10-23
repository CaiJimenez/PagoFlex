package cl.duocuc.pagoflex.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import cl.duocuc.pagoflex.ui.screens.InicioScreen
import cl.duocuc.pagoflex.ui.screens.LoginScreen

@Composable
fun AppNavegacion() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Rutas.Login.ruta) {

        composable(Rutas.Login.ruta) {
            LoginScreen(
                onIngresoExitoso = { usuario ->
                    navController.navigate(Rutas.Inicio.crear(usuario.codigo)) {
                        popUpTo(Rutas.Login.ruta) { inclusive = true }
                    }
                }
            )
        }

        composable(Rutas.Inicio.ruta) { entrada ->
            val codigo = entrada.arguments?.getString("codigo") ?: ""
            InicioScreen(
                codigo = codigo,
                onSalir = {
                    navController.navigate(Rutas.Login.ruta) {
                        popUpTo(navController.graph.id) { inclusive = true }
                    }
                }
            )
        }
    }
}