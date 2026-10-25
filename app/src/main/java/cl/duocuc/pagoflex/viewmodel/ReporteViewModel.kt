package cl.duocuc.pagoflex.viewmodel

import androidx.lifecycle.ViewModel
import cl.duocuc.pagoflex.data.Repositorio
import cl.duocuc.pagoflex.model.MotivoReporte
import cl.duocuc.pagoflex.model.ReportarUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ReportarViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ReportarUiState())

    val uiState: StateFlow<ReportarUiState> = _uiState.asStateFlow()

    fun onMotivoElegido(motivo: MotivoReporte) {
        _uiState.update { it.copy(motivo = motivo, errorMotivo = null) }
    }

    fun onComentarioCambia(texto: String) {

        _uiState.update { it.copy(comentario = texto.take(300), errorComentario = null) }
    }

    fun onEnviar(folio: String) {
        val estado = _uiState.value
        val motivo = estado.motivo

        val errorMotivo = if (motivo == null) "Elige qué pasó" else null

        val errorComentario = if (estado.comentario.isBlank()) {
            "Cuéntanos brevemente qué pasó"
        } else if (estado.comentario.trim().length < 10) {
            "Escribe un poco más (mínimo 10 caracteres)"
        } else {
            null
        }

        if (motivo == null || errorComentario != null) {
            _uiState.update { it.copy(errorMotivo = errorMotivo, errorComentario = errorComentario) }
            return
        }

        val reporte = Repositorio.crearReporte(folio, motivo, estado.comentario.trim())
        _uiState.update { it.copy(reporteCreado = reporte) }
    }
}