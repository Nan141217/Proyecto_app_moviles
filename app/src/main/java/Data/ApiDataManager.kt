package Data

import Network.Models.*
import Network.RetrofitClient
import Person.Author
import Person.Book
import Person.Loan
import android.graphics.Bitmap
import android.util.Log
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.util.*

class ApiDataManager : IDataManager {
    private val TAG = "ApiDataManager"
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())

    // ========== BOOKS ==========

    override fun addBook(libro: Book) {
        runBlocking {
            try {
                val request = CreateBookRequest(
                    titulo = libro.titulo,
                    autorId = libro.autor?.id ?: 0,
                    disponible = libro.disponible,
                    coverImageUrl = null // Por ahora no manejamos URLs de imágenes
                )
                val response = RetrofitClient.bookService.createBook(request)
                if (response.isSuccessful) {
                    response.body()?.data?.let { bookResponse ->
                        libro.id = bookResponse.id
                        // Guardar la imagen en caché si existe
                        libro.coverImage?.let { bitmap ->
                            BookImageCache.saveImage(libro.id, bitmap)
                        }
                    }
                    Log.d(TAG, "Libro creado exitosamente: ${libro.titulo}")
                } else {
                    Log.e(TAG, "Error al crear libro: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Excepción al crear libro: ${e.message}", e)
            }
        }
    }

    override fun getAllBooks(): List<Book> {
        return runBlocking {
            try {
                val response = RetrofitClient.bookService.getAllBooks()
                if (response.isSuccessful) {
                    response.body()?.data?.map { bookResponse ->
                        convertBookResponseToBook(bookResponse)
                    } ?: emptyList()
                } else {
                    Log.e(TAG, "Error al obtener libros: ${response.code()}")
                    emptyList()
                }
            } catch (e: Exception) {
                Log.e(TAG, "Excepción al obtener libros: ${e.message}", e)
                emptyList()
            }
        }
    }

    override fun getBookById(id: Int): Book? {
        return runBlocking {
            try {
                val response = RetrofitClient.bookService.getBookById(id)
                if (response.isSuccessful) {
                    response.body()?.data?.let { bookResponse ->
                        convertBookResponseToBook(bookResponse)
                    }
                } else {
                    Log.e(TAG, "Error al obtener libro: ${response.code()}")
                    null
                }
            } catch (e: Exception) {
                Log.e(TAG, "Excepción al obtener libro: ${e.message}", e)
                null
            }
        }
    }

    override fun updateBook(libro: Book): Boolean {
        return runBlocking {
            try {
                val request = CreateBookRequest(
                    titulo = libro.titulo,
                    autorId = libro.autor?.id ?: 0,
                    disponible = libro.disponible,
                    coverImageUrl = null
                )
                val response = RetrofitClient.bookService.updateBook(libro.id, request)
                if (response.isSuccessful) {
                    // Actualizar la imagen en caché
                    if (libro.coverImage != null) {
                        BookImageCache.saveImage(libro.id, libro.coverImage!!)
                    } else {
                        BookImageCache.removeImage(libro.id)
                    }
                    Log.d(TAG, "Libro actualizado exitosamente: ${libro.titulo}")
                    true
                } else {
                    Log.e(TAG, "Error al actualizar libro: ${response.code()}")
                    false
                }
            } catch (e: Exception) {
                Log.e(TAG, "Excepción al actualizar libro: ${e.message}", e)
                false
            }
        }
    }

    override fun deleteBook(id: Int): Boolean {
        return runBlocking {
            try {
                val response = RetrofitClient.bookService.deleteBook(id)
                if (response.isSuccessful) {
                    // Eliminar la imagen del caché
                    BookImageCache.removeImage(id)
                    Log.d(TAG, "Libro eliminado exitosamente")
                    true
                } else {
                    Log.e(TAG, "Error al eliminar libro: ${response.code()}")
                    false
                }
            } catch (e: Exception) {
                Log.e(TAG, "Excepción al eliminar libro: ${e.message}", e)
                false
            }
        }
    }

    // ========== AUTHORS ==========

    override fun addAuthor(autor: Author) {
        runBlocking {
            try {
                val request = CreateAuthorRequest(nombre = autor.nombre)
                val response = RetrofitClient.authorService.createAuthor(request)
                if (response.isSuccessful) {
                    response.body()?.data?.let { authorResponse ->
                        autor.id = authorResponse.id
                    }
                    Log.d(TAG, "Autor creado exitosamente: ${autor.nombre}")
                } else {
                    Log.e(TAG, "Error al crear autor: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Excepción al crear autor: ${e.message}", e)
            }
        }
    }

    override fun getAllAuthors(): List<Author> {
        return runBlocking {
            try {
                val response = RetrofitClient.authorService.getAllAuthors()
                if (response.isSuccessful) {
                    response.body()?.data?.map { authorResponse ->
                        Author(authorResponse.id, authorResponse.nombre)
                    } ?: emptyList()
                } else {
                    Log.e(TAG, "Error al obtener autores: ${response.code()}")
                    emptyList()
                }
            } catch (e: Exception) {
                Log.e(TAG, "Excepción al obtener autores: ${e.message}", e)
                emptyList()
            }
        }
    }

    override fun getAuthorById(id: Int): Author? {
        return runBlocking {
            try {
                val response = RetrofitClient.authorService.getAuthorById(id)
                if (response.isSuccessful) {
                    response.body()?.data?.let { authorResponse ->
                        Author(authorResponse.id, authorResponse.nombre)
                    }
                } else {
                    Log.e(TAG, "Error al obtener autor: ${response.code()}")
                    null
                }
            } catch (e: Exception) {
                Log.e(TAG, "Excepción al obtener autor: ${e.message}", e)
                null
            }
        }
    }

    override fun updateAuthor(autor: Author): Boolean {
        return runBlocking {
            try {
                val request = CreateAuthorRequest(nombre = autor.nombre)
                val response = RetrofitClient.authorService.updateAuthor(autor.id, request)
                if (response.isSuccessful) {
                    Log.d(TAG, "Autor actualizado exitosamente: ${autor.nombre}")
                    true
                } else {
                    Log.e(TAG, "Error al actualizar autor: ${response.code()}")
                    false
                }
            } catch (e: Exception) {
                Log.e(TAG, "Excepción al actualizar autor: ${e.message}", e)
                false
            }
        }
    }

    override fun deleteAuthor(id: Int): Boolean {
        return runBlocking {
            try {
                val response = RetrofitClient.authorService.deleteAuthor(id)
                if (response.isSuccessful) {
                    Log.d(TAG, "Autor eliminado exitosamente")
                    true
                } else {
                    Log.e(TAG, "Error al eliminar autor: ${response.code()}")
                    false
                }
            } catch (e: Exception) {
                Log.e(TAG, "Excepción al eliminar autor: ${e.message}", e)
                false
            }
        }
    }

    // ========== LOANS ==========

    override fun addLoan(prestamo: Loan) {
        runBlocking {
            try {
                val fechaStr = dateFormat.format(prestamo.fecha)
                val request = CreateLoanRequest(
                    libroId = prestamo.libro?.id ?: 0,
                    prestatario = prestamo.prestatario,
                    fecha = fechaStr
                )
                val response = RetrofitClient.loanService.createLoan(request)
                if (response.isSuccessful) {
                    response.body()?.data?.let { loanResponse ->
                        prestamo.id = loanResponse.id
                    }
                    Log.d(TAG, "Préstamo creado exitosamente")
                } else {
                    Log.e(TAG, "Error al crear préstamo: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Excepción al crear préstamo: ${e.message}", e)
            }
        }
    }

    override fun getAllLoans(): List<Loan> {
        return runBlocking {
            try {
                val response = RetrofitClient.loanService.getAllLoans()
                if (response.isSuccessful) {
                    response.body()?.data?.map { loanResponse ->
                        convertLoanResponseToLoan(loanResponse)
                    } ?: emptyList()
                } else {
                    Log.e(TAG, "Error al obtener préstamos: ${response.code()}")
                    emptyList()
                }
            } catch (e: Exception) {
                Log.e(TAG, "Excepción al obtener préstamos: ${e.message}", e)
                emptyList()
            }
        }
    }

    override fun getLoanById(id: Int): Loan? {
        return runBlocking {
            try {
                val response = RetrofitClient.loanService.getLoanById(id)
                if (response.isSuccessful) {
                    response.body()?.data?.let { loanResponse ->
                        convertLoanResponseToLoan(loanResponse)
                    }
                } else {
                    Log.e(TAG, "Error al obtener préstamo: ${response.code()}")
                    null
                }
            } catch (e: Exception) {
                Log.e(TAG, "Excepción al obtener préstamo: ${e.message}", e)
                null
            }
        }
    }

    override fun updateLoan(prestamo: Loan): Boolean {
        return runBlocking {
            try {
                // Si el préstamo fue devuelto, llamar al endpoint de return
                if (prestamo.devuelto) {
                    val response = RetrofitClient.loanService.returnLoan(prestamo.id)
                    if (response.isSuccessful) {
                        Log.d(TAG, "Préstamo devuelto exitosamente")
                        true
                    } else {
                        Log.e(TAG, "Error al devolver préstamo: ${response.code()}")
                        false
                    }
                } else {
                    // La API no tiene un endpoint general de update para loans
                    // Por ahora solo soportamos marcar como devuelto
                    Log.w(TAG, "Update de préstamo solo soporta marcar como devuelto")
                    false
                }
            } catch (e: Exception) {
                Log.e(TAG, "Excepción al actualizar préstamo: ${e.message}", e)
                false
            }
        }
    }

    override fun deleteLoan(id: Int): Boolean {
        return runBlocking {
            try {
                val response = RetrofitClient.loanService.deleteLoan(id)
                if (response.isSuccessful) {
                    Log.d(TAG, "Préstamo eliminado exitosamente")
                    true
                } else {
                    Log.e(TAG, "Error al eliminar préstamo: ${response.code()}")
                    false
                }
            } catch (e: Exception) {
                Log.e(TAG, "Excepción al eliminar préstamo: ${e.message}", e)
                false
            }
        }
    }

    // ========== UTILITY METHODS ==========

    override fun hayLibros(): Boolean {
        return getAllBooks().isNotEmpty()
    }

    override fun hayAutores(): Boolean {
        return getAllAuthors().isNotEmpty()
    }

    override fun hayPrestamos(): Boolean {
        return getAllLoans().isNotEmpty()
    }

    // ========== CONVERSION METHODS ==========

    private fun convertBookResponseToBook(bookResponse: BookResponse): Book {
        val author = bookResponse.autor?.let { authorResponse ->
            Author(authorResponse.id, authorResponse.nombre)
        }
        // Recuperar imagen del caché si existe
        val cachedImage = BookImageCache.getImage(bookResponse.id)
        return Book(
            id = bookResponse.id,
            titulo = bookResponse.titulo,
            autor = author ?: Author(0, ""),
            disponible = bookResponse.disponible,
            coverImage = cachedImage
        )
    }

    private fun convertLoanResponseToLoan(loanResponse: LoanResponse): Loan {
        val book = loanResponse.libro?.let { bookResponse ->
            convertBookResponseToBook(bookResponse)
        } ?: Book(0, "", Author(0, ""), true, null) // Valor por defecto si no hay libro

        val fecha = try {
            dateFormat.parse(loanResponse.fecha) ?: Date()
        } catch (e: Exception) {
            Date()
        }

        return Loan(
            id = loanResponse.id,
            libro = book,
            prestatario = loanResponse.prestatario,
            fecha = fecha,
            devuelto = loanResponse.devuelto
        )
    }
}
