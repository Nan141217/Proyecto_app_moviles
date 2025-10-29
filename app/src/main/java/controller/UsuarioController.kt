package controller

import data.IDataManager
import models.Usuario
import java.util.Date


class UsuarioController(private val dataManager: IDataManager) {

    // ========== Gestión de usuarios ==========

    fun crearUsuario(nombre: String, edad: Int): Usuario? {
        // Generar ID único basado en el timestamp y el tamaño de la lista
        val nuevoId = generarIdUnico()
        val usuario = Usuario(
            id = nuevoId,
            nombre = nombre,
            edad = edad
        )

        return if (dataManager.crearUsuario(usuario)) {
            usuario
        } else {
            null
        }
    }


    fun obtenerUsuario(id: Int): Usuario? = dataManager.obtenerUsuario(id)


    fun obtenerTodosLosUsuarios(): List<Usuario> = dataManager.obtenerTodosLosUsuarios()

    fun eliminarUsuario(id: Int): Boolean = dataManager.eliminarUsuario(id)

    fun iniciarSesion(usuario: Usuario) {
        dataManager.establecerUsuarioActual(usuario)
    }


    fun iniciarSesionPorId(id: Int): Boolean {
        val usuario = dataManager.obtenerUsuario(id)
        return if (usuario != null) {
            dataManager.establecerUsuarioActual(usuario)
            true
        } else {
            false
        }
    }


    fun cerrarSesion() {
        dataManager.cerrarSesion()
    }


    fun obtenerUsuarioActual(): Usuario? = dataManager.obtenerUsuarioActual()


    fun haySesionActiva(): Boolean = dataManager.haySesionActiva()

    // ========== Actualización de progreso ==========

    fun actualizarProgreso(categoria: String, nuevoProgreso: Int): Usuario? {
        val usuarioActual = dataManager.obtenerUsuarioActual() ?: return null

        val progresoValidado = nuevoProgreso.coerceIn(0, 100)

        val usuarioActualizado = when (categoria.lowercase()) {
            "animales" -> usuarioActual.copy(
                progresoAnimales = progresoValidado,
                fechaUltimaActividad = Date()
            )
            "colores" -> usuarioActual.copy(
                progresoColores = progresoValidado,
                fechaUltimaActividad = Date()
            )
            "frutas" -> usuarioActual.copy(
                progresoFrutas = progresoValidado,
                fechaUltimaActividad = Date()
            )
            "numeros" -> usuarioActual.copy(
                progresoNumeros = progresoValidado,
                fechaUltimaActividad = Date()
            )
            "frases" -> usuarioActual.copy(
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


    fun marcarElementoCompletado(categoria: String, elementoId: Int, puntos: Int = 10): Usuario? {
        val usuarioActual = dataManager.obtenerUsuarioActual() ?: return null

        val usuarioActualizado = when (categoria.lowercase()) {
            "animales" -> {
                if (usuarioActual.animalesCompletados.contains(elementoId)) {
                    return usuarioActual // Ya estaba completado
                }
                usuarioActual.copy(
                    animalesCompletados = usuarioActual.animalesCompletados + elementoId,
                    puntosTotales = usuarioActual.puntosTotales + puntos,
                    fechaUltimaActividad = Date()
                )
            }
            "colores" -> {
                if (usuarioActual.coloresCompletados.contains(elementoId)) {
                    return usuarioActual
                }
                usuarioActual.copy(
                    coloresCompletados = usuarioActual.coloresCompletados + elementoId,
                    puntosTotales = usuarioActual.puntosTotales + puntos,
                    fechaUltimaActividad = Date()
                )
            }
            "frutas" -> {
                if (usuarioActual.frutasCompletadas.contains(elementoId)) {
                    return usuarioActual
                }
                usuarioActual.copy(
                    frutasCompletadas = usuarioActual.frutasCompletadas + elementoId,
                    puntosTotales = usuarioActual.puntosTotales + puntos,
                    fechaUltimaActividad = Date()
                )
            }
            "numeros" -> {
                if (usuarioActual.numerosCompletados.contains(elementoId)) {
                    return usuarioActual
                }
                usuarioActual.copy(
                    numerosCompletados = usuarioActual.numerosCompletados + elementoId,
                    puntosTotales = usuarioActual.puntosTotales + puntos,
                    fechaUltimaActividad = Date()
                )
            }
            "frases" -> {
                if (usuarioActual.frasesCompletadas.contains(elementoId)) {
                    return usuarioActual
                }
                usuarioActual.copy(
                    frasesCompletadas = usuarioActual.frasesCompletadas + elementoId,
                    puntosTotales = usuarioActual.puntosTotales + puntos,
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


    fun sumarPuntos(puntos: Int): Usuario? {
        val usuarioActual = dataManager.obtenerUsuarioActual() ?: return null

        val usuarioActualizado = usuarioActual.copy(
            puntosTotales = usuarioActual.puntosTotales + puntos,
            fechaUltimaActividad = Date()
        )

        return if (dataManager.actualizarUsuario(usuarioActualizado)) {
            usuarioActualizado
        } else {
            null
        }
    }


    fun reiniciarProgresoCategoría(categoria: String): Usuario? {
        val usuarioActual = dataManager.obtenerUsuarioActual() ?: return null

        val usuarioActualizado = when (categoria.lowercase()) {
            "animales" -> usuarioActual.copy(
                progresoAnimales = 0,
                animalesCompletados = emptyList()
            )
            "colores" -> usuarioActual.copy(
                progresoColores = 0,
                coloresCompletados = emptyList()
            )
            "frutas" -> usuarioActual.copy(
                progresoFrutas = 0,
                frutasCompletadas = emptyList()
            )
            "numeros" -> usuarioActual.copy(
                progresoNumeros = 0,
                numerosCompletados = emptyList()
            )
            "frases" -> usuarioActual.copy(
                progresoFrases = 0,
                frasesCompletadas = emptyList()
            )
            else -> return null
        }

        return if (dataManager.actualizarUsuario(usuarioActualizado)) {
            usuarioActualizado
        } else {
            null
        }
    }


    fun reiniciarProgresoTotal(): Usuario? {
        val usuarioActual = dataManager.obtenerUsuarioActual() ?: return null

        val usuarioReiniciado = usuarioActual.copy(
            progresoAnimales = 0,
            progresoColores = 0,
            progresoFrutas = 0,
            progresoNumeros = 0,
            progresoFrases = 0,
            puntosTotales = 0,
            animalesCompletados = emptyList(),
            coloresCompletados = emptyList(),
            frutasCompletadas = emptyList(),
            numerosCompletados = emptyList(),
            frasesCompletadas = emptyList()
        )

        return if (dataManager.actualizarUsuario(usuarioReiniciado)) {
            usuarioReiniciado
        } else {
            null
        }
    }


    fun elementoCompletado(categoria: String, elementoId: Int): Boolean {
        val usuario = dataManager.obtenerUsuarioActual() ?: return false

        return when (categoria.lowercase()) {
            "animales" -> usuario.animalesCompletados.contains(elementoId)
            "colores" -> usuario.coloresCompletados.contains(elementoId)
            "frutas" -> usuario.frutasCompletadas.contains(elementoId)
            "numeros" -> usuario.numerosCompletados.contains(elementoId)
            "frases" -> usuario.frasesCompletadas.contains(elementoId)
            else -> false
        }
    }


    fun obtenerProgresoGeneral(): Int? {
        return dataManager.obtenerUsuarioActual()?.calcularProgresoGeneral()
    }


    fun haCompletadoTodo(): Boolean {
        return dataManager.obtenerUsuarioActual()?.haCompletadoTodasLasCategorias() ?: false
    }

    private fun generarIdUnico(): Int {
        val usuarios = dataManager.obtenerTodosLosUsuarios()
        val maxId = usuarios.maxOfOrNull { it.id } ?: 0
        return maxId + 1
    }
}
