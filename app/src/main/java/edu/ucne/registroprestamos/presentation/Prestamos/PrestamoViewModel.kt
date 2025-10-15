package edu.ucne.registroprestamos.presentation.Prestamos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.ucne.registroprestamos.data.repository.PrestamoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PrestamoViewModel @Inject constructor(
    private val repository: PrestamoRepository
) : ViewModel() {

    private val _state = MutableStateFlow(PrestamoState())
    val state: StateFlow<PrestamoState> = _state.asStateFlow()

    fun onEvent(event: PrestamoEvent) {
        when (event) {
            is PrestamoEvent.PrestamoIdChange -> {
                _state.update { it.copy(prestamoId = event.prestamoId) }
            }
            is PrestamoEvent.NombreClienteChange -> {
                _state.update { it.copy(nombreCliente = event.nombreCliente) }
            }
            is PrestamoEvent.ConceptoChange -> {
                _state.update { it.copy(concepto = event.concepto) }
            }
            is PrestamoEvent.FechaChange -> {
                _state.update { it.copy(fecha = event.fecha) }
            }
            is PrestamoEvent.BalanceChange -> {
                _state.update { it.copy(balance = event.balance) }
            }
            PrestamoEvent.Save -> {
                savePrestamo()
            }
            PrestamoEvent.New -> {
                _state.update {
                    it.copy(
                        prestamoId = null,
                        nombreCliente = "",
                        concepto = "",
                        fecha = "",
                        balance = null,
                        errorMessage = "",
                        isSuccess = false
                    )
                }
            }
            PrestamoEvent.LoadPrestamos -> {
                loadPrestamos()
            }
            is PrestamoEvent.DeletePrestamo -> {
                deletePrestamo(event.id)
            }
            is PrestamoEvent.EditPrestamo -> {
                editPrestamo(event.id)
            }
        }
    }

    private fun savePrestamo() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = "", isSuccess = false) }

            // Validaciones
            if (_state.value.nombreCliente.isBlank() || _state.value.concepto.isBlank() ||
                _state.value.fecha.isBlank() || _state.value.balance == null) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Todos los campos son requeridos"
                    )
                }
                return@launch
            }

            try {
                val prestamoDto = edu.ucne.registroprestamos.data.remote.dto.PrestamoDto(
                    prestamoId = _state.value.prestamoId ?: 0,
                    nombreCliente = _state.value.nombreCliente.trim(),
                    concepto = _state.value.concepto.trim(),
                    fecha = _state.value.fecha.trim(),
                    balance = _state.value.balance ?: 0.0
                )

                val result = if (prestamoDto.prestamoId == 0) {
                    repository.createPrestamo(prestamoDto)
                } else {
                    repository.updatePrestamo(prestamoDto.prestamoId, prestamoDto)
                }

                result.fold(
                    onSuccess = {
                        _state.update {
                            it.copy(
                                isSuccess = true,
                                isLoading = false,
                                errorMessage = "",
                                prestamoId = null,
                                nombreCliente = "",
                                concepto = "",
                                fecha = "",
                                balance = null
                            )
                        }
                        loadPrestamos() // Recargar lista
                    },
                    onFailure = { error ->
                        _state.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = error.message ?: "Error desconocido al guardar"
                            )
                        }
                    }
                )
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Error: ${e.message}"
                    )
                }
            }
        }
    }

    private fun loadPrestamos() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorCargar = "") }

            repository.getPrestamos().fold(
                onSuccess = { prestamos ->
                    _state.update {
                        it.copy(
                            prestamos = prestamos,
                            isLoading = false,
                            errorCargar = ""
                        )
                    }
                },
                onFailure = { error ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            errorCargar = error.message ?: "Error al cargar préstamos"
                        )
                    }
                }
            )
        }
    }

    private fun deletePrestamo(id: Int) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = "") }

            repository.deletePrestamo(id).fold(
                onSuccess = {
                    _state.update { it.copy(isLoading = false, isSuccess = true) }
                    loadPrestamos() // Recargar lista
                },
                onFailure = { error ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = error.message ?: "Error al eliminar"
                        )
                    }
                }
            )
        }
    }

    private fun editPrestamo(id: Int) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = "") }

            repository.getPrestamo(id).fold(
                onSuccess = { prestamo ->
                    _state.update {
                        it.copy(
                            prestamoId = prestamo.prestamoId,
                            nombreCliente = prestamo.nombreCliente,
                            concepto = prestamo.concepto,
                            fecha = prestamo.fecha,
                            balance = prestamo.balance,
                            isLoading = false,
                            prestamoEditando = prestamo
                        )
                    }
                },
                onFailure = { error ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = error.message ?: "Error al cargar préstamo"
                        )
                    }
                }
            )
        }
    }
}