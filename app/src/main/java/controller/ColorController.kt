package controller

import data.IDataManager
import models.colors


class ColorController(private val dataManager: IDataManager) {


    fun obtenerColores(): List<colors> = dataManager.getAllColors()


    fun obtenerColorPorId(id: Int): colors? = dataManager.getColorById(id)


    fun obtenerColorAleatorio(): colors? {
        val colores = obtenerColores()
        return if (colores.isNotEmpty()) {
            colores.random()
        } else {
            null
        }
    }


    fun obtenerColoresAleatorios(cantidad: Int): List<colors> {
        val colores = obtenerColores()
        return colores.shuffled().take(cantidad)
    }


    fun generarOpcionesMultiples(colorCorrecto: colors, numeroOpciones: Int = 4): List<colors> {
        val colores = obtenerColores()
        val opcionesIncorrectas = colores
            .filter { it.id != colorCorrecto.id }
            .shuffled()
            .take(numeroOpciones - 1)

        return (opcionesIncorrectas + colorCorrecto).shuffled()
    }


    fun verificarRespuesta(colorSeleccionado: colors, colorCorrecto: colors): Boolean {
        return colorSeleccionado.id == colorCorrecto.id
    }


    fun verificarRespuestaPorId(idSeleccionado: Int, idCorrecto: Int): Boolean {
        return idSeleccionado == idCorrecto
    }


    fun verificarRespuestaPorNombre(nombreSeleccionado: String, colorCorrecto: colors): Boolean {
        return nombreSeleccionado.trim().equals(colorCorrecto.nombreIngles, ignoreCase = true)
    }


    fun verificarRespuestaPorHex(hexSeleccionado: String, colorCorrecto: colors): Boolean {
        return hexSeleccionado.trim().equals(colorCorrecto.colorHex, ignoreCase = true)
    }


    fun buscarColorPorNombre(nombre: String): List<colors> {
        val nombreBusqueda = nombre.trim().lowercase()
        return obtenerColores().filter {
            it.nombreEspanol.lowercase().contains(nombreBusqueda) ||
            it.nombreIngles.lowercase().contains(nombreBusqueda)
        }
    }


    fun buscarColorPorHex(hex: String): colors? {
        val hexBusqueda = hex.trim().lowercase()
        return obtenerColores().find {
            it.colorHex.lowercase() == hexBusqueda
        }
    }


    fun obtenerColoresPendientes(coloresCompletados: List<Int>): List<colors> {
        return obtenerColores().filter { !coloresCompletados.contains(it.id) }
    }


    fun obtenerColorPendienteAleatorio(coloresCompletados: List<Int>): colors? {
        val pendientes = obtenerColoresPendientes(coloresCompletados)
        return if (pendientes.isNotEmpty()) {
            pendientes.random()
        } else {
            null
        }
    }


    fun calcularProgreso(coloresCompletados: List<Int>): Int {
        val total = obtenerColores().size
        if (total == 0) return 0

        val completados = coloresCompletados.size
        return ((completados.toFloat() / total) * 100).toInt()
    }


    fun obtenerTotalColores(): Int = obtenerColores().size


    fun hayColoresDisponibles(): Boolean = obtenerColores().isNotEmpty()


    fun agregarColor(color: colors) {
        dataManager.addColor(color)
    }


    fun actualizarColor(color: colors) {
        dataManager.upgradeC(color)
    }


    fun hexToRgb(hex: String): Triple<Int, Int, Int>? {
        val hexLimpio = hex.trim().removePrefix("#")
        if (hexLimpio.length != 6) return null

        return try {
            val r = hexLimpio.substring(0, 2).toInt(16)
            val g = hexLimpio.substring(2, 4).toInt(16)
            val b = hexLimpio.substring(4, 6).toInt(16)
            Triple(r, g, b)
        } catch (e: NumberFormatException) {
            null
        }
    }
}
