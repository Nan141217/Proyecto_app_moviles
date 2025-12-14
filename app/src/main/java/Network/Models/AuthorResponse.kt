package Network.Models

import com.google.gson.annotations.SerializedName

data class AuthorResponse(
    @SerializedName("id")
    val id: Int,

    @SerializedName("nombre")
    val nombre: String
)
