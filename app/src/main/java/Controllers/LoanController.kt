package Controllers

import Data.IDataManager
import Person.Book
import Person.Loan
import java.util.Date

class LoanController(private val dataManager: IDataManager) {
    fun obtenerPrestamos(): List<Loan> = dataManager.getAllLoans()

    fun obtenerPrestamoPorId(id: Int): Loan? =
        dataManager.getAllLoans().find { it.id == id }

    fun obtenerPrestamosActivos(): List<Loan> =
        dataManager.getAllLoans().filter { !it.devuelto }

    fun obtenerPrestamosCompletados(): List<Loan> =
        dataManager.getAllLoans().filter { it.devuelto }

    fun obtenerPrestamosPorUsuario(nombreUsuario: String): List<Loan> {
        val nombre = nombreUsuario.trim().lowercase()
        return obtenerPrestamos().filter {
            it.prestatario.lowercase().contains(nombre)
        }
    }

    fun agregarPrestamo(libro: Book, prestatario: String) {
        if (!libro.disponible) return

        val id = obtenerPrestamos().size + 1
        val nuevoPrestamo = Loan(id, libro, prestatario, Date(), false)
        dataManager.addLoan(nuevoPrestamo)

        // marcar libro como no disponible
        libro.disponible = false
        dataManager.updateBook(libro)
    }

    fun devolverLibro(prestamo: Loan) {
        prestamo.devuelto = true
        prestamo.libro?.disponible = true
        dataManager.updateBook(prestamo.libro!!)
    }

    fun calcularProgresoPrestamos(): Int {
        val total = obtenerPrestamos().size
        if (total == 0) return 0
        val completados = obtenerPrestamosCompletados().size
        return ((completados.toFloat() / total) * 100).toInt()
    }

    fun obtenerTotalPrestamos(): Int = obtenerPrestamos().size

    fun hayPrestamos(): Boolean = obtenerPrestamos().isNotEmpty()
}
