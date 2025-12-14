package Controllers

import Data.IDataManager
import Person.Author

class AuthorController(private val dataManager: IDataManager) {
    fun obtenerAutores(): List<Author> = dataManager.getAllAuthors()

    fun obtenerAutorPorId(id: Int): Author? =
        dataManager.getAllAuthors().find { it.id == id }

    fun buscarAutorPorNombre(nombre: String): List<Author> {
        val nombreBusqueda = nombre.trim().lowercase()
        return obtenerAutores().filter {
            it.nombre.lowercase().contains(nombreBusqueda)
        }
    }

    fun agregarAutor(autor: Author) {
        dataManager.addAuthor(autor)
    }

    fun hayAutores(): Boolean = obtenerAutores().isNotEmpty()

    fun obtenerTotalAutores(): Int = obtenerAutores().size
}