package cl.duocuc.pagoflex.viewmodel

import androidx.lifecycle.ViewModel
import cl.duocuc.pagoflex.data.Repositorio
import cl.duocuc.pagoflex.model.Reporte
import kotlinx.coroutines.flow.StateFlow

class MisReportesViewModel : ViewModel() {
    val reportes: StateFlow<List<Reporte>> = Repositorio.reportes
}