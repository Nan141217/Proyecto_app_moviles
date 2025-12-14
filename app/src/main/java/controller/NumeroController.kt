package controller

import data.IDataManager
import models.numbers


class NumeroController(private val dataManager: IDataManager) {


    fun obtenerNumeros(): List<numbers> = dataManager.getAllNumbers()


    fun obtenerNumeroPorId(id: Int): numbers? = dataManager.getNumberById(id)


    fun obtenerNumeroAleatorio(): numbers? {
        val numeros = obtenerNumeros()
        return if (numeros.isNotEmpty()) {
            numeros.random()
        } else {
            null
        }
    }


    fun obtenerNumerosAleatorios(cantidad: Int): List<numbers> {
        val numeros = obtenerNumeros()
        return numeros.shuffled().take(cantidad)
    }


    fun generarOpcionesMultiples(numeroCorrecto: numbers, numeroOpciones: Int = 4): List<numbers> {
        val numeros = obtenerNumeros()
        val opcionesIncorrectas = numeros
            .filter { it.Id != numeroCorrecto.Id }
            .shuffled()
            .take(numeroOpciones - 1)

        return (opcionesIncorrectas + numeroCorrecto).shuffled()
    }


    fun verificarRespuesta(numeroSeleccionado: numbers, numeroCorrecto: numbers): Boolean {
        return numeroSeleccionado.Id == numeroCorrecto.Id
    }


    fun verificarRespuestaPorId(idSeleccionado: Int, idCorrecto: Int): Boolean {
        return idSeleccionado == idCorrecto
    }


    fun verificarRespuestaPorValor(valorSeleccionado: Int, numeroCorrecto: numbers): Boolean {
        return valorSeleccionado == numeroCorrecto.Numero
    }


    fun verificarRespuestaPorNombre(nombreSeleccionado: String, numeroCorrecto: numbers): Boolean {
        return nombreSeleccionado.trim().equals(numeroCorrecto.NombreIngles, ignoreCase = true)
    }


    fun buscarNumeroPorValor(valor: Int): numbers? {
        return obtenerNumeros().find { it.Numero == valor }
    }


    fun buscarNumeroPorNombre(nombre: String): List<numbers> {
        val nombreBusqueda = nombre.trim().lowercase()
        return obtenerNumeros().filter {
            it.NombreIngles.lowercase().contains(nombreBusqueda)
        }
    }


    fun obtenerNumerosEnRango(inicio: Int, fin: Int): List<numbers> {
        return obtenerNumeros().filter { it.Numero in inicio..fin }
    }


    fun obtenerNumerosPendientes(numerosCompletados: List<Int>): List<numbers> {
        return obtenerNumeros().filter { !numerosCompletados.contains(it.Id) }
    }


    fun obtenerNumeroPendienteAleatorio(numerosCompletados: List<Int>): numbers? {
        val pendientes = obtenerNumerosPendientes(numerosCompletados)
        return if (pendientes.isNotEmpty()) {
            pendientes.random()
        } else {
            null
        }
    }

    fun calcularProgreso(numerosCompletados: List<Int>): Int {
        val total = obtenerNumeros().size
        if (total == 0) return 0

        val completados = numerosCompletados.size
        return ((completados.toFloat() / total) * 100).toInt()
    }


    fun obtenerTotalNumeros(): Int = obtenerNumeros().size


    fun hayNumerosDisponibles(): Boolean = obtenerNumeros().isNotEmpty()


    fun agregarNumero(numero: numbers) {
        dataManager.addNumero(numero)
    }


    fun actualizarNumero(numero: numbers) {
        dataManager.upgradeN(numero)
    }


    fun ordenarAscendente(): List<numbers> {
        return obtenerNumeros().sortedBy { it.Numero }
    }


    fun ordenarDescendente(): List<numbers> {
        return obtenerNumeros().sortedByDescending { it.Numero }
    }


    fun obtenerNumeroMayor(): numbers? {
        return obtenerNumeros().maxByOrNull { it.Numero }
    }


    fun obtenerNumeroMenor(): numbers? {
        return obtenerNumeros().minByOrNull { it.Numero }
    }


    fun obtenerNumerosPares(): List<numbers> {
        return obtenerNumeros().filter { it.Numero % 2 == 0 }
    }


    fun obtenerNumerosImpares(): List<numbers> {
        return obtenerNumeros().filter { it.Numero % 2 != 0 }
    }


    fun generarSecuencia(inicio: Int, cantidad: Int, incremento: Int = 1): List<numbers> {
        val numeros = obtenerNumeros()
        val secuencia = mutableListOf<numbers>()
        var valorActual = inicio

        repeat(cantidad) {
            val numero = numeros.find { it.Numero == valorActual }
            if (numero != null) {
                secuencia.add(numero)
            }
            valorActual += incremento
        }

        return secuencia
    }
}
