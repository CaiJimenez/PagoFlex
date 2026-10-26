package cl.duocuc.pagoflex.data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import cl.duocuc.pagoflex.model.Compromiso
import cl.duocuc.pagoflex.model.Empresa
import cl.duocuc.pagoflex.model.EstadoCompromiso
import cl.duocuc.pagoflex.model.EstadoReporte
import cl.duocuc.pagoflex.model.MotivoReporte
import cl.duocuc.pagoflex.model.Reporte
import cl.duocuc.pagoflex.model.Rol
import cl.duocuc.pagoflex.model.Usuario
import java.time.LocalDate

class PagoFlexDbHelper(context: Context) :
    SQLiteOpenHelper(context, NOMBRE_BD, null, VERSION_BD) {

    companion object {
        private const val NOMBRE_BD = "pagoflex.db"
        private const val VERSION_BD = 1

        private const val SQL_CREAR_ROL =
            "CREATE TABLE rol (" +
                    "id_rol INTEGER PRIMARY KEY, " +
                    "nombre TEXT NOT NULL UNIQUE)"

        private const val SQL_CREAR_ESTADO_COMPROMISO =
            "CREATE TABLE estado_compromiso (" +
                    "id_estado_compromiso INTEGER PRIMARY KEY, " +
                    "nombre TEXT NOT NULL UNIQUE)"

        private const val SQL_CREAR_MOTIVO_REPORTE =
            "CREATE TABLE motivo_reporte (" +
                    "id_motivo_reporte INTEGER PRIMARY KEY, " +
                    "nombre TEXT NOT NULL UNIQUE)"

        private const val SQL_CREAR_ESTADO_REPORTE =
            "CREATE TABLE estado_reporte (" +
                    "id_estado_reporte INTEGER PRIMARY KEY, " +
                    "nombre TEXT NOT NULL UNIQUE)"

        private const val SQL_CREAR_EMPRESA =
            "CREATE TABLE empresa_cliente (" +
                    "codigo TEXT PRIMARY KEY, " +
                    "nombre_fantasia TEXT NOT NULL, " +
                    "recargo_atraso INTEGER NOT NULL)"

        private const val SQL_CREAR_USUARIO =
            "CREATE TABLE usuario (" +
                    "codigo TEXT PRIMARY KEY, " +
                    "correo TEXT NOT NULL UNIQUE, " +
                    "nombre TEXT NOT NULL, " +
                    "rut TEXT NOT NULL UNIQUE, " +
                    "id_rol INTEGER NOT NULL REFERENCES rol(id_rol))"

        private const val SQL_CREAR_COMPROMISO =
            "CREATE TABLE compromiso (" +
                    "folio TEXT PRIMARY KEY, " +
                    "codigo_usuario TEXT NOT NULL REFERENCES usuario(codigo), " +
                    "codigo_empresa TEXT NOT NULL REFERENCES empresa_cliente(codigo), " +
                    "concepto TEXT NOT NULL, " +
                    "numero_cuota INTEGER, " +
                    "total_cuotas INTEGER, " +
                    "monto INTEGER NOT NULL, " +
                    "fecha_vencimiento TEXT NOT NULL, " +
                    "id_estado_compromiso INTEGER NOT NULL REFERENCES estado_compromiso(id_estado_compromiso), " +
                    "recargo_aplicado INTEGER NOT NULL DEFAULT 0)"

        private const val SQL_CREAR_REPORTE =
            "CREATE TABLE reporte (" +
                    "numero TEXT PRIMARY KEY, " +
                    "folio TEXT NOT NULL REFERENCES compromiso(folio), " +
                    "id_motivo_reporte INTEGER NOT NULL REFERENCES motivo_reporte(id_motivo_reporte), " +
                    "comentario TEXT NOT NULL, " +
                    "fecha_creacion TEXT NOT NULL, " +
                    "id_estado_reporte INTEGER NOT NULL REFERENCES estado_reporte(id_estado_reporte), " +
                    "codigo_agente TEXT REFERENCES usuario(codigo), " +
                    "respuesta TEXT)"

        private const val SQL_SELECT_COMPROMISOS =
            "SELECT c.*, e.nombre_fantasia, e.recargo_atraso " +
                    "FROM compromiso c " +
                    "INNER JOIN empresa_cliente e ON c.codigo_empresa = e.codigo"
    }

    override fun onConfigure(db: SQLiteDatabase) {
        super.onConfigure(db)
        db.setForeignKeyConstraintsEnabled(true)
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREAR_ROL)
        db.execSQL(SQL_CREAR_ESTADO_COMPROMISO)
        db.execSQL(SQL_CREAR_MOTIVO_REPORTE)
        db.execSQL(SQL_CREAR_ESTADO_REPORTE)
        db.execSQL(SQL_CREAR_EMPRESA)
        db.execSQL(SQL_CREAR_USUARIO)
        db.execSQL(SQL_CREAR_COMPROMISO)
        db.execSQL(SQL_CREAR_REPORTE)
        precargar(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        listOf("reporte", "compromiso", "usuario", "empresa_cliente",
            "estado_reporte", "motivo_reporte", "estado_compromiso", "rol")
            .forEach { db.execSQL("DROP TABLE IF EXISTS $it") }
        onCreate(db)
    }

    private fun precargar(db: SQLiteDatabase) {
        precargarCatalogo(db, "rol", "id_rol", Rol.entries.map { it.etiqueta })
        precargarCatalogo(db, "estado_compromiso", "id_estado_compromiso", EstadoCompromiso.entries.map { it.etiqueta })
        precargarCatalogo(db, "motivo_reporte", "id_motivo_reporte", MotivoReporte.entries.map { it.etiqueta })
        precargarCatalogo(db, "estado_reporte", "id_estado_reporte", EstadoReporte.entries.map { it.etiqueta })

        DatosPrueba.empresas.forEach { e ->
            db.insert("empresa_cliente", null, ContentValues().apply {
                put("codigo", e.codigo)
                put("nombre_fantasia", e.nombreEmpre)
                put("recargo_atraso", e.recargoAtraso)
            })
        }

        DatosPrueba.usuarios.forEach { u ->
            db.insert("usuario", null, ContentValues().apply {
                put("codigo", u.codigo)
                put("correo", u.correo)
                put("nombre", u.nombreUser)
                put("rut", u.rut)
                put("id_rol", u.rol.ordinal + 1)
            })
        }

        DatosPrueba.compromisos.forEach { c -> db.insert("compromiso", null, valoresCompromiso(c)) }
        DatosPrueba.reportes.forEach { r -> db.insert("reporte", null, valoresReporte(r)) }
    }

    private fun precargarCatalogo(db: SQLiteDatabase, tabla: String, columnaId: String, nombres: List<String>) {
        nombres.forEachIndexed { posicion, nombre ->
            db.insert(tabla, null, ContentValues().apply {
                put(columnaId, posicion + 1)
                put("nombre", nombre)
            })
        }
    }

    fun listarCompromisos(): List<Compromiso> {
        val lista = mutableListOf<Compromiso>()
        readableDatabase.rawQuery("$SQL_SELECT_COMPROMISOS ORDER BY c.folio", null).use { cursor ->
            while (cursor.moveToNext()) lista.add(leerCompromiso(cursor))
        }
        return lista
    }

    fun listarReportes(): List<Reporte> {
        val lista = mutableListOf<Reporte>()
        readableDatabase.rawQuery("SELECT * FROM reporte ORDER BY numero DESC", null).use { cursor ->
            while (cursor.moveToNext()) lista.add(leerReporte(cursor))
        }
        return lista
    }

    fun buscarUsuarioPorCorreo(correo: String): Usuario? =
        readableDatabase.rawQuery(
            "SELECT * FROM usuario WHERE LOWER(correo) = LOWER(?)", arrayOf(correo)
        ).use { cursor ->
            if (cursor.moveToFirst()) {
                Usuario(
                    codigo = cursor.texto("codigo"),
                    correo = cursor.texto("correo"),
                    nombreUser = cursor.texto("nombre"),
                    rut = cursor.texto("rut"),
                    rol = Rol.entries[cursor.entero("id_rol") - 1]
                )
            } else null
        }

    fun insertarReporte(reporte: Reporte): Long =
        writableDatabase.insert("reporte", null, valoresReporte(reporte))

    fun actualizarEstadoCompromiso(folio: String, estado: EstadoCompromiso): Int {
        val valores = ContentValues().apply { put("id_estado_compromiso", estado.ordinal + 1) }
        return writableDatabase.update("compromiso", valores, "folio = ?", arrayOf(folio))
    }

    fun tomarReporte(numero: String, codigoAgente: String): Int {
        val valores = ContentValues().apply {
            put("id_estado_reporte", EstadoReporte.EN_GESTION.ordinal + 1)
            put("codigo_agente", codigoAgente)
        }
        return writableDatabase.update("reporte", valores, "numero = ?", arrayOf(numero))
    }

    fun resolverReporte(numero: String, respuesta: String): Int {
        val valores = ContentValues().apply {
            put("id_estado_reporte", EstadoReporte.RESUELTO.ordinal + 1)
            put("respuesta", respuesta)
        }
        return writableDatabase.update("reporte", valores, "numero = ?", arrayOf(numero))
    }

    private fun valoresCompromiso(c: Compromiso) = ContentValues().apply {

        put("folio", c.folio)
        put("codigo_usuario", c.codigoUsuario)
        put("codigo_empresa", c.empresa.codigo)
        put("concepto", c.concepto)
        if (c.numeroCuota != null) put("numero_cuota", c.numeroCuota) else putNull("numero_cuota")
        if (c.totalCuotas != null) put("total_cuotas", c.totalCuotas) else putNull("total_cuotas")
        put("monto", c.monto)
        put("fecha_vencimiento", c.fechaVencimiento.toString())
        put("id_estado_compromiso", c.estado.ordinal + 1)
        put("recargo_aplicado", c.recargoAplicado)
    }

    private fun valoresReporte(r: Reporte) = ContentValues().apply {

        put("numero", r.codigo)
        put("folio", r.folio)
        put("id_motivo_reporte", r.motivo.ordinal + 1)
        put("comentario", r.comentario)
        put("fecha_creacion", r.fecha.toString())
        put("id_estado_reporte", r.estado.ordinal + 1)
        if (r.codigoAdministrador != null) put("codigo_agente", r.codigoAdministrador) else putNull("codigo_agente")
        if (r.respuesta != null) put("respuesta", r.respuesta) else putNull("respuesta")

    }

    private fun leerCompromiso(cursor: Cursor) = Compromiso(

        folio = cursor.texto("folio"),
        codigoUsuario = cursor.texto("codigo_usuario"),
        empresa = Empresa(
            cursor.texto("codigo_empresa"),
            cursor.texto("nombre_fantasia"),
            cursor.entero("recargo_atraso")
        ),
        concepto = cursor.texto("concepto"),
        numeroCuota = cursor.enteroONull("numero_cuota"),
        totalCuotas = cursor.enteroONull("total_cuotas"),
        monto = cursor.entero("monto"),
        fechaVencimiento = LocalDate.parse(cursor.texto("fecha_vencimiento")),
        estado = EstadoCompromiso.entries[cursor.entero("id_estado_compromiso") - 1],
        recargoAplicado = cursor.entero("recargo_aplicado")

    )

    private fun leerReporte(cursor: Cursor) = Reporte(
        codigo = cursor.texto("numero"),
        folio = cursor.texto("folio"),
        motivo = MotivoReporte.entries[cursor.entero("id_motivo_reporte") - 1],
        comentario = cursor.texto("comentario"),
        fecha = LocalDate.parse(cursor.texto("fecha_creacion")),
        estado = EstadoReporte.entries[cursor.entero("id_estado_reporte") - 1],
        codigoAdministrador = cursor.textoONull("codigo_agente"),
        respuesta = cursor.textoONull("respuesta")
    )

    private fun Cursor.texto(columna: String): String = getString(getColumnIndexOrThrow(columna))
    private fun Cursor.entero(columna: String): Int = getInt(getColumnIndexOrThrow(columna))
    private fun Cursor.textoONull(columna: String): String? =
        getColumnIndexOrThrow(columna).let { if (isNull(it)) null else getString(it) }
    private fun Cursor.enteroONull(columna: String): Int? =
        getColumnIndexOrThrow(columna).let { if (isNull(it)) null else getInt(it) }
}