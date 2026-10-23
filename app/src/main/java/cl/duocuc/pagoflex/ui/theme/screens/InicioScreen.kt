package cl.duocuc.pagoflex.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.duocuc.pagoflex.data.DatosPrueba
import cl.duocuc.pagoflex.model.Rol

import cl.duocuc.pagoflex.viewmodel.SituacionViewModel


@Composable
fun InicioScreen(codigo: String, onSalir: () -> Unit) {

    val usuario = DatosPrueba.usuarios.first { it.codigo == codigo }

    when (usuario.rol) {

        Rol.USUARIO -> {
            val situacionViewModel: SituacionViewModel = viewModel ()

            LaunchedEffect(codigo) {
                situacionViewModel.cargar(codigo)
            }

            SituacionScreen(viewModel = situacionViewModel)
        }

        Rol.AGENTE -> {
            ReportesScreen (onSalir = onSalir)
        }

        Rol.EJECUTIVO -> {
            EmpresaScreen (onSalir = onSalir)
        }
    }
}