package Person

import android.graphics.Bitmap

class Book {
    var id: Int = 0
    var titulo: String = ""
    var autor: Author? = null
    var disponible: Boolean = true
    var coverImage: Bitmap? = null

    constructor(
        id: Int,
        titulo: String,
        autor: Author,
        disponible: Boolean,
        coverImage: Bitmap? = null
    ) {
        this.id = id
        this.titulo = titulo
        this.autor = autor
        this.disponible = disponible
        this.coverImage = coverImage
    }
}
