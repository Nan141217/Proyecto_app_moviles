package Network.Services

import Network.Models.ApiResponse
import Network.Models.BookResponse
import Network.Models.CreateBookRequest
import retrofit2.Response
import retrofit2.http.*

interface BookService {
    @GET("api/books")
    suspend fun getAllBooks(): Response<ApiResponse<List<BookResponse>>>

    @GET("api/books/{id}")
    suspend fun getBookById(@Path("id") id: Int): Response<ApiResponse<BookResponse>>

    @POST("api/books")
    suspend fun createBook(@Body request: CreateBookRequest): Response<ApiResponse<BookResponse>>

    @PUT("api/books/{id}")
    suspend fun updateBook(
        @Path("id") id: Int,
        @Body request: CreateBookRequest
    ): Response<ApiResponse<BookResponse>>

    @DELETE("api/books/{id}")
    suspend fun deleteBook(@Path("id") id: Int): Response<ApiResponse<Any>>
}
