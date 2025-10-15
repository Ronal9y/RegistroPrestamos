package edu.ucne.registroprestamos.presentation.Prestamos

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun PrestamoEditScreen(
    prestamoId: Int,
    goBack: () -> Unit,
    viewModel: PrestamoViewModel = hiltViewModel()
) {
    PrestamoScreen(
        prestamoId = prestamoId,
        goBack = goBack,
        viewModel = viewModel
    )
}