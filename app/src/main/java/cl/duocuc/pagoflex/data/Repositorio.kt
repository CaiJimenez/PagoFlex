package cl.duocuc.pagoflex.data

import android.content.Context
import cl.duocuc.pagoflex.model.Compromiso
import cl.duocuc.pagoflex.model.EstadoCompromiso
import cl.duocuc.pagoflex.model.MotivoReporte
import cl.duocuc.pagoflex.model.Reporte
import cl.duocuc.pagoflex.model.Usuario
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object Repositorio {

    private lateinit var db: PagoFlexDbHelper

    private val _compromisos = MutableStateFlow<List<Compromiso>>(emptyList())
    val compromisos: StateFlow<List<Compromiso>> = _compromisos.asStateFlow()

    private val _reportes = MutableStateFlow<List<Reporte>>(emptyList())
    val reportes: StateFlow<List<Reporte>> = _reportes.asStateFlow()

    fun iniciar(context: Context) {
        if (::db.isInitialized) return
        db = PagoFlexDbHelper(context.applicationContext)
        recargar()
    }

    private fun recargar() {
        _compromisos.value = db.listarCompromisos()
        _reportes.value = db.listarReportes()
    }

    fun compromiso(folio: String): Compromiso? =
        _compromisos.value.firstOrNull { it.folio == folio }

    fun buscarUsuarioPorCorreo(correo: String): Usuario? =
        db.buscarUsuarioPorCorreo(correo)

    fun crearReporte(folio: String, motivo: MotivoReporte, comentario: String): Reporte {
        val ultimo = _reportes.value.maxOfOrNull { it.codigo.substringAfter("-").toInt() } ?: 0
        val nuevo = Reporte(
            codigo = "REP-%04d".format(ultimo + 1),
            folio = folio,
            motivo = motivo,
            comentario = comentario,
            fecha = DatosPrueba.hoy
        )
        db.insertarReporte(nuevo)
        db.actualizarEstadoCompromiso(folio, EstadoCompromiso.EN_REVISION)
        recargar()
        return nuevo
    }

    fun tomarReporte(codigoReporte: String, codigoAgente: String) {
        db.tomarReporte(codigoReporte, codigoAgente)
        recargar()
    }

    fun resolverReporte(codigoReporte: String, respuesta: String, seCorrige: Boolean) {
        val reporte = _reportes.value.firstOrNull { it.codigo == codigoReporte } ?: return
        val compromiso = compromiso(reporte.folio)

        db.resolverReporte(codigoReporte, respuesta)

        if (compromiso != null) {
            val nuevoEstado = when {
                seCorrige -> EstadoCompromiso.ANULADO
                compromiso.fechaVencimiento.isBefore(DatosPrueba.hoy) -> EstadoCompromiso.VENCIDO
                else -> EstadoCompromiso.PENDIENTE
            }
            db.actualizarEstadoCompromiso(reporte.folio, nuevoEstado)
        }
        recargar()
    }
}