package edu.ucne.registroprestamos.presentation.Prestamos

import edu.ucne.registroprestamos.data.remote.dto.PrestamoDto

data class PrestamoState(
    val prestamoId: Int? = null,
    val nombreCliente: String = "",
    val concepto: String = "",
    val fecha: String = "",
    val balance: Double? = null,
    val isSuccess: Boolean = false,
    val isLoading: Boolean = false,
    val errorCargar: String? = "",
    val errorMessage: String? = "",
    val prestamos: List<PrestamoDto> = emptyList(),
    val prestamoEditando: PrestamoDto? = null
)
