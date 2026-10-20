package cl.duocuc.pagoflex.model

import java.time.LocalDate

data class Reporte(

    val codigo: String,
    val folio: String,
    val motivo: MotivoReporte,
    val comentario: String,
    val fecha: LocalDate,
    val estado: EstadoReporte = EstadoReporte.ABIERTO,
    val codigoAdministrador: String? = null,
    val respuesta: String? = null

)
