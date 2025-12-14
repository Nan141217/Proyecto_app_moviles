package controller

import data.IDataManager
import models.Animal


class AnimalController(private val dataManager: IDataManager) {


    fun obtenerAnimales(): List<Animal> = dataManager.getAllAnimals()


    fun obtenerAnimalPorId(id: Int): Animal? = dataManager.getAnimalById(id)


    fun obtenerAnimalAleatorio(): Animal? {
        val animales = obtenerAnimales()
        return if (animales.isNotEmpty()) {
            animales.random()
        } else {
            null
        }
    }


    fun obtenerAnimalesAleatorios(cantidad: Int): List<Animal> {
        val animales = obtenerAnimales()
        return animales.shuffled().take(cantidad)
    }


    fun generarOpcionesMultiples(animalCorrecto: Animal, numeroOpciones: Int = 4): List<Animal> {
        val animales = obtenerAnimales()
        val opcionesIncorrectas = animales
            .filter { it.id != animalCorrecto.id }
            .shuffled()
            .take(numeroOpciones - 1)

        return (opcionesIncorrectas + animalCorrecto).shuffled()
    }


    fun verificarRespuesta(animalSeleccionado: Animal, animalCorrecto: Animal): Boolean {
        return animalSeleccionado.id == animalCorrecto.id
    }


    fun verificarRespuestaPorId(idSeleccionado: Int, idCorrecto: Int): Boolean {
        return idSeleccionado == idCorrecto
    }


    fun verificarRespuestaPorNombre(nombreSeleccionado: String, animalCorrecto: Animal): Boolean {
        return nombreSeleccionado.trim().equals(animalCorrecto.nombreIngles, ignoreCase = true)
    }


    fun buscarAnimalPorNombre(nombre: String): List<Animal> {
        val nombreBusqueda = nombre.trim().lowercase()
        return obtenerAnimales().filter {
            it.nombreEspanol.lowercase().contains(nombreBusqueda) ||
            it.nombreIngles.lowercase().contains(nombreBusqueda)
        }
    }


    fun obtenerAnimalesPendientes(animalesCompletados: List<Int>): List<Animal> {
        return obtenerAnimales().filter { !animalesCompletados.contains(it.id) }
    }


    fun obtenerAnimalPendienteAleatorio(animalesCompletados: List<Int>): Animal? {
        val pendientes = obtenerAnimalesPendientes(animalesCompletados)
        return if (pendientes.isNotEmpty()) {
            pendientes.random()
        } else {
            null
        }
    }


    fun calcularProgreso(animalesCompletados: List<Int>): Int {
        val total = obtenerAnimales().size
        if (total == 0) return 0

        val completados = animalesCompletados.size
        return ((completados.toFloat() / total) * 100).toInt()
    }


    fun obtenerTotalAnimales(): Int = obtenerAnimales().size


    fun hayAnimalesDisponibles(): Boolean = obtenerAnimales().isNotEmpty()


    fun agregarAnimal(animal: Animal) {
        dataManager.addAnimal(animal)
    }


    fun actualizarAnimal(animal: Animal) {
        dataManager.upgrade(animal)
    }
}



