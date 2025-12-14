package models

class Fruits {
    var id: Int=0
    var nombreEspanol: String=""
    var nombreIngles: String=""
    var imagen: Int=0

    constructor(
        id: Int,
        nombreEspanol: String,
        nombreIngles: String,
        imagen: Int
    ) {
        this.id = id
        this.nombreEspanol = nombreEspanol
        this.nombreIngles = nombreIngles
        this.imagen = imagen
    }

    var Id: Int
        get() = this.id
        set(value) { this.id = value }

    var NombreEspanol: String
        get() = this.nombreEspanol
        set(value) { this.nombreEspanol = value }

    var NombreIngles: String
        get() = this.nombreIngles
        set(value) { this.nombreIngles = value }

    var Imagen: Int
        get() = this.imagen
        set(value) { this.imagen = value }
}