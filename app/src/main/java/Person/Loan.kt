package Person

import java.util.Date

class Loan {
    var id: Int = 0
    var libro: Book? = null
    var prestatario: String = ""
    var fecha: Date = Date()
    var devuelto: Boolean = false

    constructor(
        id: Int,
        libro: Book,
        prestatario: String,
        fecha: Date,
        devuelto: Boolean
    ) {
        this.id = id
        this.libro = libro
        this.prestatario = prestatario
        this.fecha = fecha
        this.devuelto = devuelto
    }
}