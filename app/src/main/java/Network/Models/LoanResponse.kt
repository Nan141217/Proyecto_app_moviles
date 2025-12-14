package Network.Models

import com.google.gson.annotations.SerializedName

data class LoanResponse(
    @SerializedName("id")
    val id: Int,

    @SerializedName("libroId")
    val libroId: Int,

    @SerializedName("prestatario")
    val prestatario: String,

    @SerializedName("fecha")
    val fecha: String,

    @SerializedName("devuelto")
    val devuelto: Boolean,

    @SerializedName("libro")
    val libro: BookResponse? = null
)
