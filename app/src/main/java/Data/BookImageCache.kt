package Data

import android.graphics.Bitmap

object BookImageCache {
    private val imageCache = mutableMapOf<Int, Bitmap>()

    fun saveImage(bookId: Int, bitmap: Bitmap) {
        imageCache[bookId] = bitmap
    }

    fun getImage(bookId: Int): Bitmap? {
        return imageCache[bookId]
    }

    fun removeImage(bookId: Int) {
        imageCache.remove(bookId)
    }

    fun clear() {
        imageCache.clear()
    }

    fun hasImage(bookId: Int): Boolean {
        return imageCache.containsKey(bookId)
    }
}
