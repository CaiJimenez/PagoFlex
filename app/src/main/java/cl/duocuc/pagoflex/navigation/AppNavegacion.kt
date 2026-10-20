package cl.duocuc.pagoflex.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import cl.duocuc.pagoflex.model.Rol
import cl.duocuc.pagoflex.ui.screens.EmpresaScreen
import cl.duocuc.pagoflex.ui.screens.EntrarScreen
import cl.duocuc.pagoflex.ui.screens.ReportesScreen
import cl.duocuc.pagoflex.ui.screens.SituacionScreen
import cl.duocuc.pagoflex.viewmodel.EntrarViewModel
import cl.duocuc.pagoflex.viewmodel.SituacionViewModel

@Composable
fun AppNavegacion() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Rutas.Entrar.ruta) {

        composable(Rutas.Entrar.ruta) {
            val entrarViewModel: EntrarViewModel = viewModel()
            EntrarScreen(
                personas = entrarViewModel.personas,
                onElegir = { persona ->
                    val destino = when (persona.rol) {
                        Rol.USUARIO -> Rutas.Situacion.crear(persona.codigo)
                        Rol.AGENTE -> Rutas.Reportes.ruta
                        Rol.EJECUTIVO -> Rutas.Empresa.ruta
                    }
                    navController.navigate(destino)
                }
            )
        }

        composable(Rutas.Situacion.ruta) { entrada ->
            val codigo = entrada.arguments?.getString("codigo") ?: ""
            val situacionViewModel: SituacionViewModel = viewModel()

            LaunchedEffect(codigo) {
                situacionViewModel.cargar(codigo)
            }

            SituacionScreen(viewModel = situacionViewModel)
        }

        composable(Rutas.Reportes.ruta) {
            ReportesScreen(onSalir = { navController.popBackStack() })
        }

        composable(Rutas.Empresa.ruta) {
            EmpresaScreen(onSalir = { navController.popBackStack() })
        }
    }
}
