package cl.duocuc.pagoflex.model

import java.time.LocalDateTime

data class Pago(

    val numeroComrprobante: String,
    val folio: String,
    val fechaHora: LocalDateTime,
    val montoPagado: Int,
    val canal: Canal,
    val medioPago: String

)
