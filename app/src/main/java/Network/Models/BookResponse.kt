package Network.Models

import com.google.gson.annotations.SerializedName

data class BookResponse(
    @SerializedName("id")
    val id: Int,

    @SerializedName("titulo")
    val titulo: String,

    @SerializedName("autorId")
    val autorId: Int,

    @SerializedName("disponible")
    val disponible: Boolean,

    @SerializedName("coverImageUrl")
    val coverImageUrl: String? = null,

    @SerializedName("autor")
    val autor: AuthorResponse? = null
)
