package cl.duocuc.pagoflex.data

import cl.duocuc.pagoflex.model.*
import java.time.LocalDate

object DatosPrueba {

    val hoy: LocalDate = LocalDate.of(2026, 10, 6)

    val empresas = listOf(
        Empresa("EC-01", "Altos del Maipo", 8500),
        Empresa("EC-02", "Colegio Bosque Nativo", 5000),
        Empresa("EC-03", "Crédito Andino", 12000),
        Empresa("EC-04", "Horizonte Sur", 3000),
        Empresa("EC-05", "Manos Abiertas", 0),
        Empresa("EC-06", "Club Los Halcones", 2000),
        Empresa("EC-07", "Instituto Cordillera", 6000),
        Empresa("EC-08", "Plaza Oriente", 7500)
    )

    fun empresa(codigo: String): Empresa = empresas.first { it.codigo == codigo }

    val usuarios = listOf(

        Usuario("UF-01", "Camila Andrea Rojas Peña", "15.834.207-3", Rol.USUARIO),
        Usuario("UF-02", "Luis Alberto Muñoz Tapia", "12.476.318-5", Rol.USUARIO),
        Usuario("UF-03", "Rosa Elena Contreras Díaz", "6.893.541-5", Rol.USUARIO),
        Usuario("UF-04", "Matías Ignacio Soto Vergara", "21.045.873-5", Rol.USUARIO),
        Usuario("UF-05", "Fernanda Paz Godoy Lagos", "17.562.904-1", Rol.USUARIO),
        Usuario("UF-06", "Héctor Manuel Pizarro Campos", "10.318.265-4", Rol.USUARIO),
        Usuario("UF-07", "Javiera Constanza Reyes Molina", "19.287.430-0", Rol.USUARIO),
        Usuario("UF-08", "Benjamín Esteban Araya Fuentes", "18.704.396-4", Rol.USUARIO),

        Usuario("AG-01", "Andrés Felipe Lagos Meza", "16.248.317-K", Rol.AGENTE),
        Usuario("AG-02", "Daniela Sofía Ortiz Bravo", "18.352.091-1", Rol.AGENTE),

        Usuario("EJ-01", "Paula Andrea Saavedra Núñez", "14.629.853-2", Rol.EJECUTIVO)

    )

    val compromisos = listOf(

        Compromiso(
            folio = "CP-2026-0101",
            codigoUsuario = "UF-01",
            empresa = empresa("EC-08"),
            concepto = "Arriendo departamento octubre",
            numeroCuota = 10,
            totalCuotas = 12,
            monto = 520000,
            fechaVencimiento = LocalDate.of(2026, 10, 10)
        ),

        Compromiso(
            folio = "CP-2026-0102",
            codigoUsuario = "UF-01",
            empresa = empresa("EC-06"),
            concepto = "Cuota social octubre",
            numeroCuota = 10,
            totalCuotas = 12,
            monto = 15000,
            fechaVencimiento = LocalDate.of(2026, 10, 5),
            estado = EstadoCompromiso.VENCIDO,
            recargoAplicado = 2000
        )

    )

}