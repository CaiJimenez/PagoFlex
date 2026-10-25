package cl.duocuc.pagoflex.viewmodel

import androidx.lifecycle.ViewModel
import cl.duocuc.pagoflex.data.Repositorio
import cl.duocuc.pagoflex.model.Compromiso
import kotlinx.coroutines.flow.StateFlow

class DetalleViewModel : ViewModel() {

    val compromisos: StateFlow<List<Compromiso>> = Repositorio.compromisos

}