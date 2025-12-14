package controller

import data.IDataManager
import models.Fruits


class FrutaController(private val dataManager: IDataManager) {


    fun obtenerFrutas(): List<Fruits> = dataManager.getAllFruits()


    fun obtenerFrutaPorId(id: Int): Fruits? = dataManager.getFruitById(id)


    fun obtenerFrutaAleatoria(): Fruits? {
        val frutas = obtenerFrutas()
        return if (frutas.isNotEmpty()) {
            frutas.random()
        } else {
            null
        }
    }


    fun obtenerFrutasAleatorias(cantidad: Int): List<Fruits> {
        val frutas = obtenerFrutas()
        return frutas.shuffled().take(cantidad)
    }


    fun generarOpcionesMultiples(frutaCorrecta: Fruits, numeroOpciones: Int = 4): List<Fruits> {
        val frutas = obtenerFrutas()
        val opcionesIncorrectas = frutas
            .filter { it.id != frutaCorrecta.id }
            .shuffled()
            .take(numeroOpciones - 1)

        return (opcionesIncorrectas + frutaCorrecta).shuffled()
    }


    fun verificarRespuesta(frutaSeleccionada: Fruits, frutaCorrecta: Fruits): Boolean {
        return frutaSeleccionada.id == frutaCorrecta.id
    }


    fun verificarRespuestaPorId(idSeleccionado: Int, idCorrecto: Int): Boolean {
        return idSeleccionado == idCorrecto
    }


    fun verificarRespuestaPorNombre(nombreSeleccionado: String, frutaCorrecta: Fruits): Boolean {
        return nombreSeleccionado.trim().equals(frutaCorrecta.nombreIngles, ignoreCase = true)
    }


    fun buscarFrutaPorNombre(nombre: String): List<Fruits> {
        val nombreBusqueda = nombre.trim().lowercase()
        return obtenerFrutas().filter {
            it.nombreEspanol.lowercase().contains(nombreBusqueda) ||
            it.nombreIngles.lowercase().contains(nombreBusqueda)
        }
    }


    fun obtenerFrutasPendientes(frutasCompletadas: List<Int>): List<Fruits> {
        return obtenerFrutas().filter { !frutasCompletadas.contains(it.id) }
    }


    fun obtenerFrutaPendienteAleatoria(frutasCompletadas: List<Int>): Fruits? {
        val pendientes = obtenerFrutasPendientes(frutasCompletadas)
        return if (pendientes.isNotEmpty()) {
            pendientes.random()
        } else {
            null
        }
    }


    fun calcularProgreso(frutasCompletadas: List<Int>): Int {
        val total = obtenerFrutas().size
        if (total == 0) return 0

        val completados = frutasCompletadas.size
        return ((completados.toFloat() / total) * 100).toInt()
    }


    fun obtenerTotalFrutas(): Int = obtenerFrutas().size


    fun hayFrutasDisponibles(): Boolean = obtenerFrutas().isNotEmpty()


    fun agregarFruta(fruta: Fruits) {
        dataManager.addFruta(fruta)
    }


    fun actualizarFruta(fruta: Fruits) {
        dataManager.upgradeF(fruta)
    }


    fun agruparPorLetraInicial(): Map<Char, List<Fruits>> {
        return obtenerFrutas()
            .groupBy { it.nombreIngles.first().uppercaseChar() }
    }


    fun filtrarPorLetraInicial(letra: Char): List<Fruits> {
        return obtenerFrutas().filter {
            it.nombreIngles.first().equals(letra, ignoreCase = true)
        }
    }
}
