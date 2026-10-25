package cl.duocuc.pagoflex.data

import cl.duocuc.pagoflex.model.Compromiso
import cl.duocuc.pagoflex.model.EstadoCompromiso
import cl.duocuc.pagoflex.model.EstadoReporte
import cl.duocuc.pagoflex.model.MotivoReporte
import cl.duocuc.pagoflex.model.Reporte
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

object Repositorio {

    private val _compromisos = MutableStateFlow(DatosPrueba.compromisos)
    val compromisos: StateFlow<List<Compromiso>> = _compromisos.asStateFlow()

    private val _reportes = MutableStateFlow(DatosPrueba.reportes)
    val reportes: StateFlow<List<Reporte>> = _reportes.asStateFlow()

    fun compromiso(folio: String): Compromiso? =
        _compromisos.value.firstOrNull { it.folio == folio }

    fun crearReporte(folio: String, motivo: MotivoReporte, comentario: String): Reporte {
        val ultimo = _reportes.value.maxOfOrNull { it.codigo.substringAfter("-").toInt() } ?: 0
        val nuevo = Reporte(
            codigo = "REP-%04d".format(ultimo + 1),
            folio = folio,
            motivo = motivo,
            comentario = comentario,
            fecha = DatosPrueba.hoy
        )
        _reportes.update { lista -> listOf(nuevo) + lista }

        _compromisos.update { lista ->
            lista.map { if (it.folio == folio) it.copy(estado = EstadoCompromiso.EN_REVISION) else it }
        }
        return nuevo
    }

    fun tomarReporte(codigoReporte: String, codigoAgente: String) {
        _reportes.update { lista ->
            lista.map {
                if (it.codigo == codigoReporte) {
                    it.copy(estado = EstadoReporte.EN_GESTION, codigoAdministrador = codigoAgente)
                } else it
            }
        }
    }

    fun resolverReporte(codigoReporte: String, respuesta: String) {
        _reportes.update { lista ->
            lista.map {
                if (it.codigo == codigoReporte) {
                    it.copy(estado = EstadoReporte.RESUELTO, respuesta = respuesta)
                } else it
            }
        }
    }
}