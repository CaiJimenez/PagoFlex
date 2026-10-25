package cl.duocuc.pagoflex.ui.screens

import androidx.compose.runtime.Composable
import cl.duocuc.pagoflex.data.DatosPrueba
import cl.duocuc.pagoflex.data.Menus

@Composable
fun InicioScreen(

    codigo: String,
    onSalir: () -> Unit,
    onAbrir: (String) -> Unit
) {
    val usuario = DatosPrueba.usuarios.first { it.codigo == codigo }

    MenuScreen(

        usuario = usuario,
        opciones = Menus.para(usuario.rol),
        onAbrir = onAbrir,
        onSalir = onSalir
    )
}