package Network.Models

import com.google.gson.annotations.SerializedName

data class RegisterRequest(
    @SerializedName("username")
    val username: String,

    @SerializedName("password")
    val password: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("lastname")
    val lastname: String,

    @SerializedName("email")
    val email: String
)
