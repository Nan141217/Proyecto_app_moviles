package Data

import Person.Author
import Person.Book
import Person.Loan

interface IDataManager {
    fun addBook(libro: Book)

    fun getAllBooks(): List<Book>

    fun getBookById(id: Int): Book?

    fun updateBook(libro: Book): Boolean

    fun deleteBook(id: Int): Boolean

    fun addAuthor(autor: Author)

    fun getAllAuthors(): List<Author>

    fun getAuthorById(id: Int): Author?

    fun updateAuthor(autor: Author): Boolean

    fun deleteAuthor(id: Int): Boolean

    fun addLoan(prestamo: Loan)

    fun getAllLoans(): List<Loan>

    fun getLoanById(id: Int): Loan?

    fun updateLoan(prestamo: Loan): Boolean

    fun deleteLoan(id: Int): Boolean

    fun hayLibros(): Boolean

    fun hayAutores(): Boolean

    fun hayPrestamos(): Boolean
}