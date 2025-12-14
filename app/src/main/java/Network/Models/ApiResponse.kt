package Network.Models

import com.google.gson.annotations.SerializedName

data class ApiResponse<T>(
    @SerializedName("responseCode")
    val responseCode: Int,

    @SerializedName("message")
    val message: String,

    @SerializedName("data")
    val data: T? = null
)
