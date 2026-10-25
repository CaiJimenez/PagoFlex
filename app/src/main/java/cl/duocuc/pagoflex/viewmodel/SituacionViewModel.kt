package cl.duocuc.pagoflex.viewmodel

import androidx.lifecycle.ViewModel
import cl.duocuc.pagoflex.data.DatosPrueba
import cl.duocuc.pagoflex.data.Repositorio
import cl.duocuc.pagoflex.model.EstadoCompromiso
import cl.duocuc.pagoflex.model.SituacionUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SituacionViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(SituacionUiState())
    val uiState: StateFlow<SituacionUiState> = _uiState.asStateFlow()

    fun cargar(codigoUsuario: String) {

        val usuario = DatosPrueba.usuarios.first { it.codigo == codigoUsuario }

        val vigentes = Repositorio.compromisos.value.filter {

            it.codigoUsuario == codigoUsuario &&
                    it.estado != EstadoCompromiso.PAGADO &&
                    it.estado != EstadoCompromiso.ANULADO

        }

        val total = vigentes
            .filter { it.sePuedePagar }
            .sumOf { it.totalAPagar }

        val proximo = vigentes
            .filter { it.estado == EstadoCompromiso.PENDIENTE }
            .minOfOrNull { it.fechaVencimiento }

        val vencidos = vigentes.count { it.estado == EstadoCompromiso.VENCIDO }

        _uiState.value = SituacionUiState(

            primerNombre = usuario.primerNombre,
            compromisos = vigentes,
            totalDelMes = total,
            proximoVencimiento = proximo,
            cantidadVencidos = vencidos

        )
    }
}