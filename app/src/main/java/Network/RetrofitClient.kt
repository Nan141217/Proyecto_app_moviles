package Network

import Network.Services.AuthorService
import Network.Services.BookService
import Network.Services.LoanService
import Network.Services.UserService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    // Cambia esta URL según tu entorno:
    // - Para emulador Android: "http://10.0.2.2:3000/"
    // - Para dispositivo físico en la misma red: "http://TU_IP_LOCAL:3000/"
    // - Para producción (Render): "https://api-xzoh.onrender.com/"
    private const val BASE_URL = "https://api-xzoh.onrender.com/"

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val authorService: AuthorService by lazy {
        retrofit.create(AuthorService::class.java)
    }

    val bookService: BookService by lazy {
        retrofit.create(BookService::class.java)
    }

    val loanService: LoanService by lazy {
        retrofit.create(LoanService::class.java)
    }

    val userService: UserService by lazy {
        retrofit.create(UserService::class.java)
    }
}
