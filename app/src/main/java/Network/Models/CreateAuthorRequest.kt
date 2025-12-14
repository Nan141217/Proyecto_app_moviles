package Network.Models

import com.google.gson.annotations.SerializedName

data class CreateAuthorRequest(
    @SerializedName("nombre")
    val nombre: String
)
