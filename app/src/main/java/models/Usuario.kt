package models

import java.util.Date


data class Usuario(
    val id: Int,
    val nombre: String,
    val edad: Int,
    val progresoAnimales: Int = 0,
    val progresoColores: Int = 0,
    val progresoFrutas: Int = 0,
    val progresoNumeros: Int = 0,
    val progresoFrases: Int = 0,
    val puntosTotales: Int = 0,
    val fechaCreacion: Date = Date(),
    val fechaUltimaActividad: Date = Date(),
    val animalesCompletados: List<Int> = emptyList(),
    val coloresCompletados: List<Int> = emptyList(),
    val frutasCompletadas: List<Int> = emptyList(),
    val numerosCompletados: List<Int> = emptyList(),
    val frasesCompletadas: List<Int> = emptyList()
) {

    fun calcularProgresoGeneral(): Int {
        return (progresoAnimales + progresoColores + progresoFrutas +
                progresoNumeros + progresoFrases) / 5
    }


    fun haCompletadoTodasLasCategorias(): Boolean {
        return progresoAnimales == 100 && progresoColores == 100 &&
               progresoFrutas == 100 && progresoNumeros == 100 && progresoFrases == 100
    }


    fun getTotalElementosCompletados(): Int {
        return animalesCompletados.size + coloresCompletados.size +
               frutasCompletadas.size + numerosCompletados.size + frasesCompletadas.size
    }
}
