package models

class numbers {
    private var id: Int=0
    private var numero: Int=0
    private var nombreIngles: String=""
    private var imagen: Int=0

    constructor(
        id: Int,
        numero: Int,
        nombreIngles: String,
        imagen: Int
    ) {
        this.id = id
        this.numero = numero
        this.nombreIngles = nombreIngles
        this.imagen = imagen
    }

    var Id: Int
        get() = this.id
        set(value) { this.id = value }

    var Numero: Int
        get() = this.numero
        set(value) { this.numero = value }

    var NombreIngles: String
        get() = this.nombreIngles
        set(value) { this.nombreIngles = value }

    var Imagen: Int
        get() = this.imagen
        set(value) { this.imagen = value }

}