package cl.duocuc.pagoflex.model

data class Usuario(

    val codigo: String,
    val correo: String,
    val nombreUser: String,
    val rut: String,
    val rol: Rol

){

    val primerNombre: String
        get() = nombreUser.substringBefore(" ")

    val rutOculto: String
        get() = "${rut.substringBefore(".")}.*.*-${rut.substringAfter("-")}"

}