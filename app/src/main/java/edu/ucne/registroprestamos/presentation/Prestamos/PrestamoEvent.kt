package edu.ucne.registroprestamos.presentation.Prestamos

sealed interface PrestamoEvent {
    data class PrestamoIdChange(val prestamoId: Int?): PrestamoEvent
    data class NombreClienteChange(val nombreCliente: String): PrestamoEvent
    data class ConceptoChange(val concepto: String): PrestamoEvent
    data class FechaChange(val fecha: String): PrestamoEvent
    data class BalanceChange(val balance: Double?): PrestamoEvent
    object Save: PrestamoEvent
    object New: PrestamoEvent
    object LoadPrestamos: PrestamoEvent
    data class DeletePrestamo(val id: Int): PrestamoEvent
    data class EditPrestamo(val id: Int): PrestamoEvent
}