package cl.duocuc.pagoflex.model

import java.time.LocalDate

data class SituacionUiState(

    val primerNombre: String = "",
    val compromisos: List<Compromiso> = emptyList(),
    val totalDelMes: Int = 0,
    val proximoVencimiento: LocalDate? = null,
    val cantidadVencidos: Int = 0

)