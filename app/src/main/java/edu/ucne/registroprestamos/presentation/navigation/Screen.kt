package edu.ucne.registroprestamos.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Screen {
    @Serializable
    data object List : Screen()

    @Serializable
    data class Edit(val prestamoId: Int) : Screen()

    @Serializable
    data class Delete(val prestamoId: Int) : Screen()

    @Serializable
    data object Register : Screen()
}