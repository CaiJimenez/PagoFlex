package cl.duocuc.pagoflex.model

enum class MotivoReporte (val etiqueta: String){

    YA_PAGUE("Ya pagué por otro medio"),
    NO_RECONOZCO("No reconozco este cobro"),
    MONTO_INCORRECTO("El monto no corresponde"),
    OTRO("Otro")

}