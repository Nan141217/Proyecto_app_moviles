package Network.Services

import Network.Models.ApiResponse
import Network.Models.CreateLoanRequest
import Network.Models.LoanResponse
import retrofit2.Response
import retrofit2.http.*

interface LoanService {
    @GET("api/loans")
    suspend fun getAllLoans(): Response<ApiResponse<List<LoanResponse>>>

    @GET("api/loans/{id}")
    suspend fun getLoanById(@Path("id") id: Int): Response<ApiResponse<LoanResponse>>

    @POST("api/loans")
    suspend fun createLoan(@Body request: CreateLoanRequest): Response<ApiResponse<LoanResponse>>

    @PUT("api/loans/{id}/return")
    suspend fun returnLoan(@Path("id") id: Int): Response<ApiResponse<LoanResponse>>

    @DELETE("api/loans/{id}")
    suspend fun deleteLoan(@Path("id") id: Int): Response<ApiResponse<Any>>
}
