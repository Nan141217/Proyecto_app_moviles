package models

class colors {
    var id: Int=0
    var nombreEspanol: String=""
    var nombreIngles: String=""
    var colorHex: String=""

    constructor(
        id: Int,
        nombreEspanol: String,
        nombreIngles: String,
        colorHex: String
    ) {
        this.id = id
        this.nombreEspanol = nombreEspanol
        this.nombreIngles = nombreIngles
        this.colorHex = colorHex
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

    var ColorHex: String
        get() = this.colorHex
        set(value) { this.colorHex = value }
}