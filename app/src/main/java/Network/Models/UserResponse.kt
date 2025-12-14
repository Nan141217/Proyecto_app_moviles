package Network.Models

import com.google.gson.annotations.SerializedName

data class UserResponse(
    @SerializedName("id")
    val id: Int,

    @SerializedName("username")
    val username: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("lastname")
    val lastname: String?,

    @SerializedName("email")
    val email: String,

    @SerializedName("isActive")
    val isActive: Boolean
)
