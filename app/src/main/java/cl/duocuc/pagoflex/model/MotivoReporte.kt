package cl.duocuc.pagoflex.model

enum class MotivoReporte (val etiqueta: String){

    YA_PAGUE("Ya pague mi compromiso"),
    NO_RECONOZCO("No reconozco este cargo"),
    MONTO_INCORRECTO("El monto no es el que corresponde"),
    OTRO("Otro")

}