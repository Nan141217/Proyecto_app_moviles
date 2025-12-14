package Network.Services

import Network.Models.ApiResponse
import Network.Models.LoginRequest
import Network.Models.RegisterRequest
import Network.Models.UserResponse
import retrofit2.Response
import retrofit2.http.*

interface UserService {
    @POST("api/users/auth")
    suspend fun login(@Body request: LoginRequest): Response<ApiResponse<UserResponse>>

    @POST("api/users/register")
    suspend fun register(@Body request: RegisterRequest): Response<ApiResponse<UserResponse>>

    @GET("api/users")
    suspend fun getAllUsers(): Response<ApiResponse<List<UserResponse>>>

    @GET("api/users/{id}")
    suspend fun getUserById(@Path("id") id: Int): Response<ApiResponse<UserResponse>>
}
