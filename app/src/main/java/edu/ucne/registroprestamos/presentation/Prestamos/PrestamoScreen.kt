package edu.ucne.registroprestamos.presentation.Prestamos

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrestamoScreen(
    prestamoId: Int?,
    goBack: () -> Unit,
    viewModel: PrestamoViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    var balanceText by remember { mutableStateOf("") }


    LaunchedEffect(state.balance) {
        if (state.balance != null) {
            balanceText = state.balance.toString()
        }
    }

    // Cargar préstamo si estamos editando
    LaunchedEffect(prestamoId) {
        if (prestamoId != null) {
            viewModel.onEvent(PrestamoEvent.EditPrestamo(prestamoId))
        } else {
            viewModel.onEvent(PrestamoEvent.New)
            // Establecer fecha actual por defecto
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val currentDate = dateFormat.format(Date())
            viewModel.onEvent(PrestamoEvent.FechaChange(currentDate))
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        if (prestamoId != null) "Editar Préstamo" else "Nuevo Préstamo"
                    )
                },
                navigationIcon = {
                    IconButton(onClick = goBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Regresar")
                    }
                },
                actions = {
                    IconButton(
                        onClick = { viewModel.onEvent(PrestamoEvent.Save) },
                        enabled = state.nombreCliente.isNotEmpty() &&
                                state.concepto.isNotEmpty() &&
                                state.fecha.isNotEmpty() &&
                                state.balance != null &&
                                !state.isLoading
                    ) {
                        Icon(Icons.Default.Save, contentDescription = "Guardar")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            if (state.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                OutlinedTextField(
                    value = state.nombreCliente,
                    onValueChange = { viewModel.onEvent(PrestamoEvent.NombreClienteChange(it)) },
                    label = { Text("Nombre del Cliente *") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                )

                OutlinedTextField(
                    value = state.concepto,
                    onValueChange = { viewModel.onEvent(PrestamoEvent.ConceptoChange(it)) },
                    label = { Text("Concepto *") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                )

                OutlinedTextField(
                    value = state.fecha,
                    onValueChange = { viewModel.onEvent(PrestamoEvent.FechaChange(it)) },
                    label = { Text("Fecha (YYYY-MM-DD) *") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                )

                OutlinedTextField(
                    value = balanceText,
                    onValueChange = { newValue ->
                        balanceText = newValue
                        val balanceValue = newValue.toDoubleOrNull()
                        viewModel.onEvent(PrestamoEvent.BalanceChange(balanceValue))
                    },
                    label = { Text("Balance *") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    placeholder = { Text("Ej: 1000.50") }
                )

                // Mensajes de estado
                if (state.balance == null && balanceText.isNotEmpty()) {
                    Text(
                        text = "Ingrese un valor numérico válido",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }

                state.errorMessage?.let { error ->
                    if (error.isNotEmpty()) {
                        Text(
                            text = " $error",
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                }

                if (state.isSuccess) {
                    LaunchedEffect(state.isSuccess) {
                        if (state.isSuccess) {

                            kotlinx.coroutines.delay(1000)
                            goBack()
                        }
                    }
                    Text(
                        text = " Préstamo guardado exitosamente",
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            }
        }
    }
}