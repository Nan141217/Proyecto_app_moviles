package Network.Models

import com.google.gson.annotations.SerializedName

data class CreateLoanRequest(
    @SerializedName("libroId")
    val libroId: Int,

    @SerializedName("prestatario")
    val prestatario: String,

    @SerializedName("fecha")
    val fecha: String? = null
)
