package cl.duocuc.pagoflex.viewmodel

import androidx.lifecycle.ViewModel
import cl.duocuc.pagoflex.data.Repositorio
import cl.duocuc.pagoflex.model.EstadoReporte
import cl.duocuc.pagoflex.model.Reporte
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class BandejaViewModel : ViewModel() {

    val reportes: StateFlow<List<Reporte>> = Repositorio.reportes

    private val _filtro = MutableStateFlow<EstadoReporte?>(null)
    val filtro: StateFlow<EstadoReporte?> = _filtro.asStateFlow()

    fun onFiltroCambia(estado: EstadoReporte?) {
        _filtro.value = estado
    }

    fun onTomar(codigoReporte: String, codigoAgente: String) {
        Repositorio.tomarReporte(codigoReporte, codigoAgente)
    }

}