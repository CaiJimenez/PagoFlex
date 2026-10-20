package cl.duocuc.pagoflex.ui

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

private val chile = Locale.forLanguageTag("es-CL")
private val formatoFecha = DateTimeFormatter.ofPattern("dd-MM-yyyy")

fun Int.comoPesos(): String = "$" + String.format(chile, "%,d", this)

fun LocalDate.comoFecha(): String = this.format(formatoFecha)