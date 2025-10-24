package controller

import data.IDataManager
import models.Frase


class FraseController(private val dataManager: IDataManager) {


    fun obtenerFrases(): List<Frase> = dataManager.getAllFrases()


    fun obtenerFrasePorId(id: Int): Frase? = dataManager.getFraseById(id)


    fun obtenerFraseAleatoria(): Frase? {
        val frases = obtenerFrases()
        return if (frases.isNotEmpty()) {
            frases.random()
        } else {
            null
        }
    }


    fun obtenerFrasesAleatorias(cantidad: Int): List<Frase> {
        val frases = obtenerFrases()
        return frases.shuffled().take(cantidad)
    }


    fun obtenerFrasesPorDificultad(nivel: Int): List<Frase> {
        return obtenerFrases().filter { it.dificultad == nivel }
    }


    fun obtenerFrasesPorCategoria(categoria: String): List<Frase> {
        return obtenerFrases().filter {
            it.categoria.equals(categoria, ignoreCase = true)
        }
    }


    fun obtenerFrasesPorDificultadYCategoria(nivel: Int, categoria: String): List<Frase> {
        return obtenerFrases().filter {
            it.dificultad == nivel && it.categoria.equals(categoria, ignoreCase = true)
        }
    }


    fun obtenerFraseAleatoriaPorDificultad(nivel: Int): Frase? {
        val frases = obtenerFrasesPorDificultad(nivel)
        return if (frases.isNotEmpty()) {
            frases.random()
        } else {
            null
        }
    }


    fun verificarFraseFormada(frase: Frase, fraseFormada: String): Boolean {
        return frase.verificarFrase(fraseFormada)
    }


    fun verificarFraseFormadaPorId(fraseId: Int, fraseFormada: String): Boolean {
        val frase = obtenerFrasePorId(fraseId) ?: return false
        return frase.verificarFrase(fraseFormada)
    }


    fun generarPalabrasDesordenadas(frase: Frase): List<String> {
        return if (frase.palabrasDesordenadas.isNotEmpty()) {
            // Usar las palabras desordenadas predefinidas
            frase.palabrasDesordenadas.shuffled()
        } else {
            // Generar desordenando las palabras de la frase
            frase.obtenerPalabrasOrdenadas().shuffled()
        }
    }


    fun obtenerCategorias(): List<String> {
        return obtenerFrases()
            .map { it.categoria }
            .filter { it.isNotBlank() }
            .distinct()
            .sorted()
    }


    fun obtenerNivelesDificultad(): List<Int> {
        return obtenerFrases()
            .map { it.dificultad }
            .distinct()
            .sorted()
    }


    fun obtenerFrasesPendientes(frasesCompletadas: List<Int>): List<Frase> {
        return obtenerFrases().filter { !frasesCompletadas.contains(it.id) }
    }


    fun obtenerFrasePendienteAleatoria(frasesCompletadas: List<Int>): Frase? {
        val pendientes = obtenerFrasesPendientes(frasesCompletadas)
        return if (pendientes.isNotEmpty()) {
            pendientes.random()
        } else {
            null
        }
    }


    fun obtenerFrasesPendientesPorDificultad(
        frasesCompletadas: List<Int>,
        nivel: Int
    ): List<Frase> {
        return obtenerFrasesPendientes(frasesCompletadas).filter { it.dificultad == nivel }
    }


    fun calcularProgreso(frasesCompletadas: List<Int>): Int {
        val total = obtenerFrases().size
        if (total == 0) return 0

        val completados = frasesCompletadas.size
        return ((completados.toFloat() / total) * 100).toInt()
    }


    fun calcularProgresoPorNivel(frasesCompletadas: List<Int>, nivel: Int): Int {
        val frasesDelNivel = obtenerFrasesPorDificultad(nivel)
        val total = frasesDelNivel.size
        if (total == 0) return 0

        val completadosDelNivel = frasesDelNivel.count { frasesCompletadas.contains(it.id) }
        return ((completadosDelNivel.toFloat() / total) * 100).toInt()
    }


    fun obtenerTotalFrases(): Int = obtenerFrases().size


    fun hayFrasesDisponibles(): Boolean = obtenerFrases().isNotEmpty()


    fun agregarFrase(frase: Frase) {
        dataManager.addFrase(frase)
    }


    fun actualizarFrase(frase: Frase) {
        dataManager.upgradeFrase(frase)
    }


    fun buscarFrasesPorPalabra(palabra: String): List<Frase> {
        val palabraBusqueda = palabra.trim().lowercase()
        return obtenerFrases().filter {
            it.fraseEspanol.lowercase().contains(palabraBusqueda) ||
            it.fraseIngles.lowercase().contains(palabraBusqueda)
        }
    }


    fun obtenerEstadisticasPorDificultad(): Map<Int, Int> {
        return obtenerFrases()
            .groupBy { it.dificultad }
            .mapValues { it.value.size }
    }


    fun obtenerEstadisticasPorCategoria(): Map<String, Int> {
        return obtenerFrases()
            .filter { it.categoria.isNotBlank() }
            .groupBy { it.categoria }
            .mapValues { it.value.size }
    }


    fun generarPistas(frase: Frase): Pair<String?, String?> {
        val palabras = frase.obtenerPalabrasOrdenadas()
        return if (palabras.isNotEmpty()) {
            Pair(palabras.first(), palabras.last())
        } else {
            Pair(null, null)
        }
    }


    fun validarOrdenPalabras(frase: Frase, palabrasOrdenadas: List<String>): Boolean {
        val palabrasCorrectas = frase.obtenerPalabrasOrdenadas()
        if (palabrasOrdenadas.size != palabrasCorrectas.size) return false

        return palabrasOrdenadas.mapIndexed { index, palabra ->
            palabra.trim().equals(palabrasCorrectas[index], ignoreCase = true)
        }.all { it }
    }
}
