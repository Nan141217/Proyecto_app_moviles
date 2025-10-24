package controller

import data.IDataManager
import models.Usuario
import java.util.Date


class ProgressController(
    private val dataManager: IDataManager,
    private val animalController: AnimalController,
    private val colorController: ColorController,
    private val frutaController: FrutaController,
    private val numeroController: NumeroController,
    private val fraseController: FraseController
) {


    data class ProgresoDetallado(
        val progresoGeneral: Int,
        val progresoAnimales: Int,
        val progresoColores: Int,
        val progresoFrutas: Int,
        val progresoNumeros: Int,
        val progresoFrases: Int,
        val totalElementos: Int,
        val elementosCompletados: Int,
        val puntosTotales: Int,
        val categoriaConMayorProgreso: String,
        val categoriaConMenorProgreso: String
    )


    data class EstadisticasCategoria(
        val nombreCategoria: String,
        val totalElementos: Int,
        val elementosCompletados: Int,
        val elementosPendientes: Int,
        val porcentajeCompletado: Int
    )


    fun obtenerProgresoDetallado(): ProgresoDetallado? {
        val usuario = dataManager.obtenerUsuarioActual() ?: return null

        val progresoAnimales = animalController.calcularProgreso(usuario.animalesCompletados)
        val progresoColores = colorController.calcularProgreso(usuario.coloresCompletados)
        val progresoFrutas = frutaController.calcularProgreso(usuario.frutasCompletadas)
        val progresoNumeros = numeroController.calcularProgreso(usuario.numerosCompletados)
        val progresoFrases = fraseController.calcularProgreso(usuario.frasesCompletadas)

        val progresoGeneral = usuario.calcularProgresoGeneral()

        val totalElementos = animalController.obtenerTotalAnimales() +
                colorController.obtenerTotalColores() +
                frutaController.obtenerTotalFrutas() +
                numeroController.obtenerTotalNumeros() +
                fraseController.obtenerTotalFrases()

        val elementosCompletados = usuario.getTotalElementosCompletados()

        // Determinar categorías con mayor y menor progreso
        val progresoPorCategoria = mapOf(
            "Animales" to progresoAnimales,
            "Colores" to progresoColores,
            "Frutas" to progresoFrutas,
            "Números" to progresoNumeros,
            "Frases" to progresoFrases
        )

        val categoriaMax = progresoPorCategoria.maxByOrNull { it.value }?.key ?: "Ninguna"
        val categoriaMin = progresoPorCategoria.minByOrNull { it.value }?.key ?: "Ninguna"

        return ProgresoDetallado(
            progresoGeneral = progresoGeneral,
            progresoAnimales = progresoAnimales,
            progresoColores = progresoColores,
            progresoFrutas = progresoFrutas,
            progresoNumeros = progresoNumeros,
            progresoFrases = progresoFrases,
            totalElementos = totalElementos,
            elementosCompletados = elementosCompletados,
            puntosTotales = usuario.puntosTotales,
            categoriaConMayorProgreso = categoriaMax,
            categoriaConMenorProgreso = categoriaMin
        )
    }


    fun obtenerProgresoGeneral(): Int? {
        return dataManager.obtenerUsuarioActual()?.calcularProgresoGeneral()
    }


    fun obtenerEstadisticasCategoria(categoria: String): EstadisticasCategoria? {
        val usuario = dataManager.obtenerUsuarioActual() ?: return null

        return when (categoria.lowercase()) {
            "animales" -> {
                val total = animalController.obtenerTotalAnimales()
                val completados = usuario.animalesCompletados.size
                EstadisticasCategoria(
                    nombreCategoria = "Animales",
                    totalElementos = total,
                    elementosCompletados = completados,
                    elementosPendientes = total - completados,
                    porcentajeCompletado = animalController.calcularProgreso(usuario.animalesCompletados)
                )
            }
            "colores" -> {
                val total = colorController.obtenerTotalColores()
                val completados = usuario.coloresCompletados.size
                EstadisticasCategoria(
                    nombreCategoria = "Colores",
                    totalElementos = total,
                    elementosCompletados = completados,
                    elementosPendientes = total - completados,
                    porcentajeCompletado = colorController.calcularProgreso(usuario.coloresCompletados)
                )
            }
            "frutas" -> {
                val total = frutaController.obtenerTotalFrutas()
                val completados = usuario.frutasCompletadas.size
                EstadisticasCategoria(
                    nombreCategoria = "Frutas",
                    totalElementos = total,
                    elementosCompletados = completados,
                    elementosPendientes = total - completados,
                    porcentajeCompletado = frutaController.calcularProgreso(usuario.frutasCompletadas)
                )
            }
            "numeros", "números" -> {
                val total = numeroController.obtenerTotalNumeros()
                val completados = usuario.numerosCompletados.size
                EstadisticasCategoria(
                    nombreCategoria = "Números",
                    totalElementos = total,
                    elementosCompletados = completados,
                    elementosPendientes = total - completados,
                    porcentajeCompletado = numeroController.calcularProgreso(usuario.numerosCompletados)
                )
            }
            "frases" -> {
                val total = fraseController.obtenerTotalFrases()
                val completados = usuario.frasesCompletadas.size
                EstadisticasCategoria(
                    nombreCategoria = "Frases",
                    totalElementos = total,
                    elementosCompletados = completados,
                    elementosPendientes = total - completados,
                    porcentajeCompletado = fraseController.calcularProgreso(usuario.frasesCompletadas)
                )
            }
            else -> null
        }
    }


    fun obtenerEstadisticasTodasCategorias(): List<EstadisticasCategoria> {
        if (dataManager.obtenerUsuarioActual() == null) return emptyList()

        return listOf(
            obtenerEstadisticasCategoria("animales"),
            obtenerEstadisticasCategoria("colores"),
            obtenerEstadisticasCategoria("frutas"),
            obtenerEstadisticasCategoria("numeros"),
            obtenerEstadisticasCategoria("frases")
        ).filterNotNull()
    }


    fun registrarElementoCompletado(categoria: String, elementoId: Int, puntos: Int = 10): Usuario? {
        val usuario = dataManager.obtenerUsuarioActual() ?: return null

        // Marcar elemento como completado
        val usuarioActualizado = when (categoria.lowercase()) {
            "animales" -> {
                if (usuario.animalesCompletados.contains(elementoId)) return usuario
                val nuevoProgreso = animalController.calcularProgreso(
                    usuario.animalesCompletados + elementoId
                )
                usuario.copy(
                    animalesCompletados = usuario.animalesCompletados + elementoId,
                    progresoAnimales = nuevoProgreso,
                    puntosTotales = usuario.puntosTotales + puntos,
                    fechaUltimaActividad = Date()
                )
            }
            "colores" -> {
                if (usuario.coloresCompletados.contains(elementoId)) return usuario
                val nuevoProgreso = colorController.calcularProgreso(
                    usuario.coloresCompletados + elementoId
                )
                usuario.copy(
                    coloresCompletados = usuario.coloresCompletados + elementoId,
                    progresoColores = nuevoProgreso,
                    puntosTotales = usuario.puntosTotales + puntos,
                    fechaUltimaActividad = Date()
                )
            }
            "frutas" -> {
                if (usuario.frutasCompletadas.contains(elementoId)) return usuario
                val nuevoProgreso = frutaController.calcularProgreso(
                    usuario.frutasCompletadas + elementoId
                )
                usuario.copy(
                    frutasCompletadas = usuario.frutasCompletadas + elementoId,
                    progresoFrutas = nuevoProgreso,
                    puntosTotales = usuario.puntosTotales + puntos,
                    fechaUltimaActividad = Date()
                )
            }
            "numeros", "números" -> {
                if (usuario.numerosCompletados.contains(elementoId)) return usuario
                val nuevoProgreso = numeroController.calcularProgreso(
                    usuario.numerosCompletados + elementoId
                )
                usuario.copy(
                    numerosCompletados = usuario.numerosCompletados + elementoId,
                    progresoNumeros = nuevoProgreso,
                    puntosTotales = usuario.puntosTotales + puntos,
                    fechaUltimaActividad = Date()
                )
            }
            "frases" -> {
                if (usuario.frasesCompletadas.contains(elementoId)) return usuario
                val nuevoProgreso = fraseController.calcularProgreso(
                    usuario.frasesCompletadas + elementoId
                )
                usuario.copy(
                    frasesCompletadas = usuario.frasesCompletadas + elementoId,
                    progresoFrases = nuevoProgreso,
                    puntosTotales = usuario.puntosTotales + puntos,
                    fechaUltimaActividad = Date()
                )
            }
            else -> return null
        }

        return if (dataManager.actualizarUsuario(usuarioActualizado)) {
            usuarioActualizado
        } else {
            null
        }
    }


    fun actualizarProgresoCategoria(categoria: String, nuevoProgreso: Int): Usuario? {
        val usuario = dataManager.obtenerUsuarioActual() ?: return null
        val progresoValidado = nuevoProgreso.coerceIn(0, 100)

        val usuarioActualizado = when (categoria.lowercase()) {
            "animales" -> usuario.copy(
                progresoAnimales = progresoValidado,
                fechaUltimaActividad = Date()
            )
            "colores" -> usuario.copy(
                progresoColores = progresoValidado,
                fechaUltimaActividad = Date()
            )
            "frutas" -> usuario.copy(
                progresoFrutas = progresoValidado,
                fechaUltimaActividad = Date()
            )
            "numeros", "números" -> usuario.copy(
                progresoNumeros = progresoValidado,
                fechaUltimaActividad = Date()
            )
            "frases" -> usuario.copy(
                progresoFrases = progresoValidado,
                fechaUltimaActividad = Date()
            )
            else -> return null
        }

        return if (dataManager.actualizarUsuario(usuarioActualizado)) {
            usuarioActualizado
        } else {
            null
        }
    }


    fun haCompletadoTodo(): Boolean {
        return dataManager.obtenerUsuarioActual()?.haCompletadoTodasLasCategorias() ?: false
    }


    fun obtenerCategoriaMayorProgreso(): String? {
        val progreso = obtenerProgresoDetallado() ?: return null
        return progreso.categoriaConMayorProgreso
    }


    fun obtenerCategoriaMenorProgreso(): String? {
        val progreso = obtenerProgresoDetallado() ?: return null
        return progreso.categoriaConMenorProgreso
    }


    fun obtenerElementosPendientesTotales(): Int? {
        val progreso = obtenerProgresoDetallado() ?: return null
        return progreso.totalElementos - progreso.elementosCompletados
    }


    fun generarReporteProgreso(): String {
        val progreso = obtenerProgresoDetallado() ?: return "No hay sesión activa"

        return buildString {
            appendLine("===== REPORTE DE PROGRESO =====")
            appendLine("Progreso General: ${progreso.progresoGeneral}%")
            appendLine()
            appendLine("Progreso por Categoría:")
            appendLine("- Animales: ${progreso.progresoAnimales}%")
            appendLine("- Colores: ${progreso.progresoColores}%")
            appendLine("- Frutas: ${progreso.progresoFrutas}%")
            appendLine("- Números: ${progreso.progresoNumeros}%")
            appendLine("- Frases: ${progreso.progresoFrases}%")
            appendLine()
            appendLine("Elementos Completados: ${progreso.elementosCompletados}/${progreso.totalElementos}")
            appendLine("Puntos Totales: ${progreso.puntosTotales}")
            appendLine()
            appendLine("Mejor Categoría: ${progreso.categoriaConMayorProgreso}")
            appendLine("Categoría a Mejorar: ${progreso.categoriaConMenorProgreso}")
            appendLine("===============================")
        }
    }


    fun calcularTiempoEstimadoComplecion(elementosPorDia: Int): Int? {
        val pendientes = obtenerElementosPendientesTotales() ?: return null
        if (elementosPorDia <= 0) return null

        return (pendientes / elementosPorDia) + if (pendientes % elementosPorDia > 0) 1 else 0
    }


    fun obtenerCategoriaRecomendada(): String? {
        val usuario = dataManager.obtenerUsuarioActual() ?: return null

        // Recomendar la categoría con menor progreso que tenga elementos disponibles
        val categorias = mapOf(
            "Animales" to usuario.progresoAnimales,
            "Colores" to usuario.progresoColores,
            "Frutas" to usuario.progresoFrutas,
            "Números" to usuario.progresoNumeros,
            "Frases" to usuario.progresoFrases
        )

        return categorias
            .filter { it.value < 100 } // Solo categorías no completadas
            .minByOrNull { it.value }?.key
    }
}
