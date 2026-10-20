package cl.duocuc.pagoflex.model

import java.time.LocalDate
import java.time.temporal.ChronoUnit

data class Compromiso(

    val folio: String,
    val codigoUsuario: String,
    val empresa: Empresa,
    val concepto: String,
    val numeroCuota: Int? = null,
    val totalCuotas: Int? = null,
    val monto: Int,
    val fechaVencimiento: LocalDate,
    val estado: EstadoCompromiso = EstadoCompromiso.PENDIENTE,
    val recargoAplicado: Int = 0

) {

    val totalAPagar: Int
        get() = if (estado == EstadoCompromiso.VENCIDO) monto + recargoAplicado else monto

    val cuotaTexto: String?
        get() = if (numeroCuota != null) "$numeroCuota/$totalCuotas" else null

    val sePuedePagar: Boolean
        get() = estado == EstadoCompromiso.PENDIENTE || estado == EstadoCompromiso.VENCIDO

    fun diasAtraso(hoy: LocalDate): Long =
        if (estado == EstadoCompromiso.VENCIDO) ChronoUnit.DAYS.between(fechaVencimiento, hoy) else 0

}