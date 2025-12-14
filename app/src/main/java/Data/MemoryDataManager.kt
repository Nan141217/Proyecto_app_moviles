package Data

import Person.Author
import Person.Book
import Person.Loan

object MemoryDataManager : IDataManager {
    private val booksList = mutableListOf<Book>()
    private val authorsList = mutableListOf<Author>()
    private val loansList = mutableListOf<Loan>()

    init {
        // Agregar algunos autores de ejemplo para que la app no crashee
        authorsList.add(Author(1, "Gabriel García Márquez"))
        authorsList.add(Author(2, "Isabel Allende"))
        authorsList.add(Author(3, "Jorge Luis Borges"))
    }


    // =============================================================
    // ==================   CRUD DE LIBROS   ========================
    // =============================================================

    override fun addBook(libro: Book) {
        if (!booksList.any { it.id == libro.id }) {
            booksList.add(libro)
        }
    }

    override fun getAllBooks(): List<Book> = booksList.toList()

    override fun getBookById(id: Int): Book? = booksList.find { it.id == id }

    override fun updateBook(libro: Book): Boolean {
        val index = booksList.indexOfFirst { it.id == libro.id }
        return if (index != -1) {
            booksList[index] = libro
            true
        } else {
            false
        }
    }

    override fun deleteBook(id: Int): Boolean {
        val libro = getBookById(id)
        return if (libro != null) {
            booksList.remove(libro)
            true
        } else {
            false
        }
    }




    override fun addAuthor(autor: Author) {
        if (!authorsList.any { it.id == autor.id }) {
            authorsList.add(autor)
        }
    }

    override fun getAllAuthors(): List<Author> = authorsList.toList()

    override fun getAuthorById(id: Int): Author? = authorsList.find { it.id == id }

    override fun updateAuthor(autor: Author): Boolean {
        val index = authorsList.indexOfFirst { it.id == autor.id }
        return if (index != -1) {
            authorsList[index] = autor
            true
        } else {
            false
        }
    }

    override fun deleteAuthor(id: Int): Boolean {
        val autor = getAuthorById(id)
        return if (autor != null) {
            authorsList.remove(autor)
            true
        } else {
            false
        }
    }




    override fun addLoan(prestamo: Loan) {
        if (!loansList.any { it.id == prestamo.id }) {
            loansList.add(prestamo)
        }
    }

    override fun getAllLoans(): List<Loan> = loansList.toList()

    override fun getLoanById(id: Int): Loan? = loansList.find { it.id == id }

    override fun updateLoan(prestamo: Loan): Boolean {
        val index = loansList.indexOfFirst { it.id == prestamo.id }
        return if (index != -1) {
            loansList[index] = prestamo
            true
        } else {
            false
        }
    }

    override fun deleteLoan(id: Int): Boolean {
        val prestamo = getLoanById(id)
        return if (prestamo != null) {
            loansList.remove(prestamo)
            true
        } else {
            false
        }
    }




    override fun hayLibros(): Boolean = booksList.isNotEmpty()

    override fun hayAutores(): Boolean = authorsList.isNotEmpty()

    override fun hayPrestamos(): Boolean = loansList.isNotEmpty()




    fun limpiarTodo() {
        booksList.clear()
        authorsList.clear()
        loansList.clear()
    }

    fun limpiarLibros() = booksList.clear()
    fun limpiarAutores() = authorsList.clear()
    fun limpiarPrestamos() = loansList.clear()
}