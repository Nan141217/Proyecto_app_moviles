package Network.Models

import com.google.gson.annotations.SerializedName

data class CreateBookRequest(
    @SerializedName("titulo")
    val titulo: String,

    @SerializedName("autorId")
    val autorId: Int,

    @SerializedName("disponible")
    val disponible: Boolean = true,

    @SerializedName("coverImageUrl")
    val coverImageUrl: String? = null
)
