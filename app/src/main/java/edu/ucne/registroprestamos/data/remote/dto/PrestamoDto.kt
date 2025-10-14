package edu.ucne.registroprestamos.data.remote.dto

data class PrestamoDto(
    val prestamoId: Int = 0,
    val nombreCliente: String = "",
    val concepto: String = "",
    val fecha: String = "",
    val balance: Double = 0.0
)
