package Network.Services

import Network.Models.ApiResponse
import Network.Models.AuthorResponse
import Network.Models.CreateAuthorRequest
import retrofit2.Response
import retrofit2.http.*

interface AuthorService {
    @GET("api/authors")
    suspend fun getAllAuthors(): Response<ApiResponse<List<AuthorResponse>>>

    @GET("api/authors/{id}")
    suspend fun getAuthorById(@Path("id") id: Int): Response<ApiResponse<AuthorResponse>>

    @POST("api/authors")
    suspend fun createAuthor(@Body request: CreateAuthorRequest): Response<ApiResponse<AuthorResponse>>

    @PUT("api/authors/{id}")
    suspend fun updateAuthor(
        @Path("id") id: Int,
        @Body request: CreateAuthorRequest
    ): Response<ApiResponse<AuthorResponse>>

    @DELETE("api/authors/{id}")
    suspend fun deleteAuthor(@Path("id") id: Int): Response<ApiResponse<Any>>
}
