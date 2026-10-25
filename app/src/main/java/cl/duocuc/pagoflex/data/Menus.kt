package cl.duocuc.pagoflex.data

import cl.duocuc.pagoflex.R
import cl.duocuc.pagoflex.model.OpcionMenu
import cl.duocuc.pagoflex.model.Rol

object Menus {

    fun para(rol: Rol): List<OpcionMenu> = when (rol) {

        Rol.USUARIO -> listOf(

            OpcionMenu("compromisos", "Mis compromisos", "Revisa tus cobros y reporta un problema", R.drawable.ic_compromisos, true),
            OpcionMenu("mis_reportes", "Mis reportes", "Mira en qué va lo que reportaste", R.drawable.ic_reportar, true),
            OpcionMenu("pagar", "Pagar", "Paga tus compromisos desde la app", R.drawable.ic_pagar, false),
            OpcionMenu("comprobantes", "Comprobantes", "Tus comprobantes de pago", R.drawable.ic_comprobante, false),
            OpcionMenu("historial", "Historial", "Todos tus pagos, de todos los canales", R.drawable.ic_historial, false),
            OpcionMenu("indicador", "Mi indicador", "Tu cumplimiento de pagos", R.drawable.ic_indicador, false),
            OpcionMenu("avisos", "Avisos", "Elige cuándo enterarte de tus vencimientos", R.drawable.ic_aviso, false),
            OpcionMenu("recomendar", "Recomendar", "Recomienda una organización a PagoFlex", R.drawable.ic_recomendar, false),
            OpcionMenu("logros", "Mis logros", "Reconocimientos por pagar a tiempo", R.drawable.ic_logro, false)

        )

        Rol.AGENTE -> listOf(

            OpcionMenu("bandeja", "Bandeja de reportes", "Reportes de los usuarios por estado", R.drawable.ic_bandeja, true),
            OpcionMenu("mis_casos", "Mis casos", "Responde y resuelve los reportes que tomaste", R.drawable.ic_responder, true),
            OpcionMenu("recomendaciones", "Recomendaciones", "Organizaciones recomendadas por usuarios", R.drawable.ic_recomendar, false),
            OpcionMenu("tiempos", "Tiempo de resolución", "Promedio de resolución del mes", R.drawable.ic_atrasado, false)

        )

        Rol.EJECUTIVO -> listOf(

            OpcionMenu("compromisos_empresa", "Compromisos", "Compromisos de tu empresa", R.drawable.ic_empresa, false),
            OpcionMenu("registrar", "Registrar", "Registra un compromiso nuevo", R.drawable.ic_registrar, false),
            OpcionMenu("anular", "Anular", "Deja sin efecto un compromiso", R.drawable.ic_anular, false),
            OpcionMenu("cobranza", "Cobranza del mes", "Pagados, pendientes y tasa de cobranza", R.drawable.ic_cobranza, false),
            OpcionMenu("atrasados", "Atrasados", "Personas con compromisos vencidos", R.drawable.ic_estado_vencido, false)

        )
    }
}