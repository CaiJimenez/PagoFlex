package cl.duocuc.pagoflex.model

data class ReportarUiState(

    val motivo: MotivoReporte? = null,
    val comentario: String = "",
    val errorMotivo: String? = null,
    val errorComentario: String? = null,
    val reporteCreado: Reporte? = null

)