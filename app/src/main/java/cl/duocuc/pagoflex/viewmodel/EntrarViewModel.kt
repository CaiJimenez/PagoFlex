package cl.duocuc.pagoflex.viewmodel

import androidx.lifecycle.ViewModel
import cl.duocuc.pagoflex.data.DatosPrueba
import cl.duocuc.pagoflex.model.Usuario

class EntrarViewModel : ViewModel() {

    private val codigosSugeridos = listOf("UF-01", "AG-01", "EJ-01")

    val personas: List<Usuario> = DatosPrueba.usuarios.filter { it.codigo in codigosSugeridos }
}
