package cl.duocuc.pagoflex.data

import cl.duocuc.pagoflex.model.*
import java.time.LocalDate

object DatosPrueba {

    val hoy: LocalDate = LocalDate.of(2026, 10, 6)

    const val CLAVE_DEMO = "PagoFlex#2026"

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

        Usuario("UF-01", "crojas@correo.ejemplo", "Camila Andrea Rojas Peña", "15.834.207-3", Rol.USUARIO),
        Usuario("UF-02", "lmunoz@correo.ejemplo", "Luis Alberto Muñoz Tapia", "12.476.318-5", Rol.USUARIO),
        Usuario("UF-03", "rcontreras@correo.ejemplo", "Rosa Elena Contreras Díaz", "6.893.541-5", Rol.USUARIO),
        Usuario("UF-04", "msoto@correo.ejemplo", "Matías Ignacio Soto Vergara", "21.045.873-5", Rol.USUARIO),
        Usuario("UF-05", "fgodoy@correo.ejemplo", "Fernanda Paz Godoy Lagos", "17.562.904-1", Rol.USUARIO),
        Usuario("UF-06", "hpizarro@correo.ejemplo", "Héctor Manuel Pizarro Campos", "10.318.265-4", Rol.USUARIO),
        Usuario("UF-07", "jreyes@correo.ejemplo", "Javiera Constanza Reyes Molina", "19.287.430-0", Rol.USUARIO),
        Usuario("UF-08", "baraya@correo.ejemplo", "Benjamín Esteban Araya Fuentes", "18.704.396-4", Rol.USUARIO),

        Usuario("AG-01", "alagos@pagoflex.ejemplo", "Andrés Felipe Lagos Meza", "16.248.317-K", Rol.AGENTE),
        Usuario("AG-02", "dortiz@pagoflex.ejemplo", "Daniela Sofía Ortiz Bravo", "18.352.091-1", Rol.AGENTE),

        Usuario("EJ-01", "psaavedra@altosmaipo.ejemplo", "Paula Andrea Saavedra Núñez", "14.629.853-2", Rol.EJECUTIVO)
    )

    val compromisos = listOf(
        Compromiso(folio = "CP-2026-0101", codigoUsuario = "UF-01", empresa = empresa("EC-08"),
            concepto = "Arriendo departamento octubre", numeroCuota = 10, totalCuotas = 12,
            monto = 520000, fechaVencimiento = LocalDate.of(2026, 10, 10)),
        Compromiso(folio = "CP-2026-0102", codigoUsuario = "UF-01", empresa = empresa("EC-06"),
            concepto = "Cuota social octubre", numeroCuota = 10, totalCuotas = 12,
            monto = 15000, fechaVencimiento = LocalDate.of(2026, 10, 5),
            estado = EstadoCompromiso.VENCIDO, recargoAplicado = 2000),
        Compromiso(folio = "CP-2026-0103", codigoUsuario = "UF-01", empresa = empresa("EC-05"),
            concepto = "Aporte mensual octubre",
            monto = 10000, fechaVencimiento = LocalDate.of(2026, 10, 15)),
        Compromiso(folio = "CP-2026-0104", codigoUsuario = "UF-01", empresa = empresa("EC-03"),
            concepto = "Crédito de consumo", numeroCuota = 7, totalCuotas = 24,
            monto = 86400, fechaVencimiento = LocalDate.of(2026, 9, 25),
            estado = EstadoCompromiso.PAGADO),
        Compromiso(folio = "CP-2026-0105", codigoUsuario = "UF-01", empresa = empresa("EC-03"),
            concepto = "Crédito de consumo", numeroCuota = 8, totalCuotas = 24,
            monto = 86400, fechaVencimiento = LocalDate.of(2026, 10, 25)),

        Compromiso(folio = "CP-2026-0106", codigoUsuario = "UF-02", empresa = empresa("EC-01"),
            concepto = "Cuota pie departamento", numeroCuota = 14, totalCuotas = 36,
            monto = 310000, fechaVencimiento = LocalDate.of(2026, 9, 30),
            estado = EstadoCompromiso.VENCIDO, recargoAplicado = 8500),
        Compromiso(folio = "CP-2026-0107", codigoUsuario = "UF-02", empresa = empresa("EC-01"),
            concepto = "Cuota pie departamento", numeroCuota = 15, totalCuotas = 36,
            monto = 310000, fechaVencimiento = LocalDate.of(2026, 10, 30)),

        Compromiso(folio = "CP-2026-0108", codigoUsuario = "UF-03", empresa = empresa("EC-04"),
            concepto = "Seguro de hogar octubre", numeroCuota = 10, totalCuotas = 12,
            monto = 18900, fechaVencimiento = LocalDate.of(2026, 10, 1),
            estado = EstadoCompromiso.PAGADO),
        Compromiso(folio = "CP-2026-0109", codigoUsuario = "UF-04", empresa = empresa("EC-07"),
            concepto = "Arancel octubre", numeroCuota = 8, totalCuotas = 10,
            monto = 142000, fechaVencimiento = LocalDate.of(2026, 10, 5),
            estado = EstadoCompromiso.EN_REVISION),
        Compromiso(folio = "CP-2026-0110", codigoUsuario = "UF-05", empresa = empresa("EC-02"),
            concepto = "Mensualidad octubre", numeroCuota = 8, totalCuotas = 10,
            monto = 185000, fechaVencimiento = LocalDate.of(2026, 10, 10)),
        Compromiso(folio = "CP-2026-0111", codigoUsuario = "UF-06", empresa = empresa("EC-03"),
            concepto = "Crédito de consumo", numeroCuota = 19, totalCuotas = 36,
            monto = 54200, fechaVencimiento = LocalDate.of(2026, 9, 20),
            estado = EstadoCompromiso.ANULADO),
        Compromiso(folio = "CP-2026-0112", codigoUsuario = "UF-07", empresa = empresa("EC-06"),
            concepto = "Cuota social octubre", numeroCuota = 10, totalCuotas = 12,
            monto = 15000, fechaVencimiento = LocalDate.of(2026, 10, 5),
            estado = EstadoCompromiso.PAGADO)
    )

    val reportes = listOf(
        Reporte(codigo = "REP-0012", folio = "CP-2026-0106", motivo = MotivoReporte.MONTO_INCORRECTO,
            comentario = "No sé por qué me cobran \$318.500.",
            fecha = LocalDate.of(2026, 10, 5)),
        Reporte(codigo = "REP-0011", folio = "CP-2026-0109", motivo = MotivoReporte.YA_PAGUE,
            comentario = "Pagué por transferencia directa al instituto el 03-10.",
            fecha = LocalDate.of(2026, 10, 3),
            estado = EstadoReporte.EN_GESTION, codigoAdministrador = "AG-02"),
        Reporte(codigo = "REP-0009", folio = "CP-2026-0111", motivo = MotivoReporte.MONTO_INCORRECTO,
            comentario = "Repacté el crédito y me siguen cobrando la cuota antigua.",
            fecha = LocalDate.of(2026, 9, 18),
            estado = EstadoReporte.RESUELTO, codigoAdministrador = "AG-01",
            respuesta = "La empresa anuló la cuota por repactación (22-09-2026).")
    )

}