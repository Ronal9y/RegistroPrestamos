package edu.ucne.registroprestamos.presentation.Prestamos

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import java.text.NumberFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrestamoDeleteScreen(
    prestamoId: Int,
    goBack: () -> Unit,
    viewModel: PrestamoViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val currencyFormat = NumberFormat.getCurrencyInstance(Locale.US)

    // Cargar préstamo a eliminar
    LaunchedEffect(prestamoId) {
        viewModel.onEvent(PrestamoEvent.EditPrestamo(prestamoId))
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Eliminar Préstamo") },
                navigationIcon = {
                    IconButton(onClick = goBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Regresar")
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
            if (state.isLoading && state.prestamoEditando == null) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                // Mostrar información del préstamo
                state.prestamoEditando?.let { prestamo ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    Icons.Default.Warning,
                                    contentDescription = "Advertencia",
                                    tint = MaterialTheme.colorScheme.error
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "¿Está seguro de eliminar este préstamo?",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.error
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = "Cliente: ${prestamo.nombreCliente}",
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Text(
                                text = "Concepto: ${prestamo.concepto}",
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Text(
                                text = "Fecha: ${prestamo.fecha}",
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Text(
                                text = "Balance: ${currencyFormat.format(prestamo.balance)}",
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(24.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                Button(
                                    onClick = goBack,
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.surface
                                    )
                                ) {
                                    Text("Cancelar")
                                }

                                Button(
                                    onClick = {
                                        viewModel.onEvent(PrestamoEvent.DeletePrestamo(prestamoId))
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.error
                                    )
                                ) {
                                    Icon(Icons.Default.Delete, contentDescription = "Eliminar")
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Eliminar")
                                }
                            }
                        }
                    }
                }

                state.errorMessage?.let { error ->
                    if (error.isNotEmpty()) {
                        Text(
                            text = error,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                }

                if (state.isSuccess) {
                    LaunchedEffect(state.isSuccess) {
                        // Esperar un momento y regresar
                        kotlinx.coroutines.delay(1000)
                        goBack()
                    }
                    Text(
                        text = "Préstamo eliminado exitosamente",
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            }
        }
    }
}