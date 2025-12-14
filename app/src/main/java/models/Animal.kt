package models

class Animal {
    var id: Int=0
    var nombreEspanol: String=""
    var nombreIngles: String=""
    var imagen: Int=0   // lo puse asi para preguntar despues
    var sonido: Int=0    // lo puse asi para preguntar despues


    constructor(
        id: Int,
        nombreEspanol: String,
        nombreIngles: String,
        imagen: Int,
        sonido: Int
    ) {
        this.id = id
        this.nombreEspanol = nombreEspanol
        this.nombreIngles = nombreIngles
        this.imagen = imagen
        this.sonido = sonido
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

    var Sonido: Int
        get() = this.sonido
        set(value) { this.sonido = value }





}