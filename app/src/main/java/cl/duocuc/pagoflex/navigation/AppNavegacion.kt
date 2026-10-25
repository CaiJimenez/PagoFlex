package cl.duocuc.pagoflex.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import cl.duocuc.pagoflex.ui.screens.BandejaScreen
import cl.duocuc.pagoflex.ui.screens.DetalleScreen
import cl.duocuc.pagoflex.ui.screens.InicioScreen
import cl.duocuc.pagoflex.ui.screens.LoginScreen
import cl.duocuc.pagoflex.ui.screens.ReportarScreen
import cl.duocuc.pagoflex.ui.screens.SituacionScreen
import cl.duocuc.pagoflex.viewmodel.SituacionViewModel

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
                },
                onAbrir = { id ->
                    when (id) {
                        "compromisos" -> navController.navigate(Rutas.Situacion.crear(codigo))
                        "bandeja" -> navController.navigate(Rutas.Bandeja.crear(codigo))
                    }
                }
            )
        }

        composable(Rutas.Situacion.ruta) { entrada ->
            val codigo = entrada.arguments?.getString("codigo") ?: ""
            val situacionViewModel: SituacionViewModel = viewModel()

            LaunchedEffect(codigo) {
                situacionViewModel.cargar(codigo)
            }

            SituacionScreen(
                viewModel = situacionViewModel,
                onVerCompromiso = { folio -> navController.navigate(Rutas.Detalle.crear(folio)) }
            )
        }

        composable(Rutas.Detalle.ruta) { entrada ->
            val folio = entrada.arguments?.getString("folio") ?: ""
            DetalleScreen(
                folio = folio,
                onVolver = { navController.popBackStack() },
                onReportar = { folioReporte -> navController.navigate(Rutas.Reportar.crear(folioReporte)) }
            )
        }

        composable(Rutas.Bandeja.ruta) { entrada ->
            val codigo = entrada.arguments?.getString("codigo") ?: ""
            BandejaScreen(
                codigoAgente = codigo,
                onVolver = { navController.popBackStack() }
            )
        }

        composable(Rutas.Reportar.ruta) { entrada ->
            val folio = entrada.arguments?.getString("folio") ?: ""
            ReportarScreen(
                folio = folio,
                onVolver = { navController.popBackStack() },
                onTerminar = { navController.popBackStack(Rutas.Situacion.ruta, inclusive = false) }
            )
        }
    }
}