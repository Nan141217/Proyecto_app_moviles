package models


class Frase {
    var id: Int = 0
    var fraseEspanol: String = ""
    var fraseIngles: String = ""
    var palabrasDesordenadas: List<String> = emptyList()
    var dificultad: Int = 1
    var categoria: String = ""
    var imagen: Int = 0

    constructor()

    constructor(
        id: Int,
        fraseEspanol: String,
        fraseIngles: String,
        palabrasDesordenadas: List<String>,
        dificultad: Int = 1,
        categoria: String = "",
        imagen: Int = 0
    ) {
        this.id = id
        this.fraseEspanol = fraseEspanol
        this.fraseIngles = fraseIngles
        this.palabrasDesordenadas = palabrasDesordenadas
        this.dificultad = dificultad
        this.categoria = categoria
        this.imagen = imagen
    }

    // Propiedades con getters y setters (manteniendo consistencia con otras clases del proyecto)
    var Id: Int
        get() = this.id
        set(value) { this.id = value }

    var FraseEspanol: String
        get() = this.fraseEspanol
        set(value) { this.fraseEspanol = value }

    var FraseIngles: String
        get() = this.fraseIngles
        set(value) { this.fraseIngles = value }

    var PalabrasDesordenadas: List<String>
        get() = this.palabrasDesordenadas
        set(value) { this.palabrasDesordenadas = value }

    var Dificultad: Int
        get() = this.dificultad
        set(value) { this.dificultad = value }

    var Categoria: String
        get() = this.categoria
        set(value) { this.categoria = value }

    var Imagen: Int
        get() = this.imagen
        set(value) { this.imagen = value }


    fun obtenerPalabrasOrdenadas(): List<String> {
        return fraseIngles.split(" ").filter { it.isNotBlank() }
    }


    fun contarPalabras(): Int {
        return obtenerPalabrasOrdenadas().size
    }


    fun verificarFrase(fraseUsuario: String): Boolean {
        val fraseNormalizada = fraseIngles.trim().lowercase().replace("\\s+".toRegex(), " ")
        val usuarioNormalizado = fraseUsuario.trim().lowercase().replace("\\s+".toRegex(), " ")
        return fraseNormalizada == usuarioNormalizado
    }
}
