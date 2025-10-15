package edu.ucne.registroprestamos.data.repository

import android.util.Log
import edu.ucne.registroprestamos.data.remote.PrestamoApi
import edu.ucne.registroprestamos.data.remote.dto.PrestamoDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PrestamoRepository @Inject constructor(
    private val api: PrestamoApi
) {

    suspend fun getPrestamos(): Result<List<PrestamoDto>> = withContext(Dispatchers.IO) {
        try {
            Log.d("PrestamoRepository", "Intentando obtener préstamos...")
            val prestamos = api.getPrestamos()
            Log.d("PrestamoRepository", "Préstamos obtenidos: ${prestamos.size}")
            Result.success(prestamos)
        } catch (e: IOException) {
            Log.e("PrestamoRepository", "Error de red: ${e.message}", e)
            Result.failure(Exception("Error de conexión. Verifica tu internet."))
        } catch (e: HttpException) {
            Log.e("PrestamoRepository", "Error HTTP ${e.code()}: ${e.message()}")
            Log.e("PrestamoRepository", "Response: ${e.response()?.errorBody()?.string()}")
            Result.failure(Exception("Error del servidor (${e.code()}). Intenta más tarde."))
        } catch (e: Exception) {
            Log.e("PrestamoRepository", "Error desconocido: ${e.message}", e)
            Result.failure(Exception("Error inesperado: ${e.message}"))
        }
    }

    suspend fun createPrestamo(prestamo: PrestamoDto): Result<PrestamoDto> = withContext(Dispatchers.IO) {
        try {
            Log.d("PrestamoRepository", "Intentando crear préstamo: $prestamo")
            val result = api.postPrestamo(prestamo)
            Log.d("PrestamoRepository", "Préstamo creado exitosamente: ${result.nombreCliente}")
            Result.success(result)
        } catch (e: IOException) {
            Log.e("PrestamoRepository", "Error de red al crear: ${e.message}", e)
            Result.failure(Exception("No hay conexión a Internet."))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            Log.e("PrestamoRepository", "Error HTTP ${e.code()} al crear: ${e.message()}")
            Log.e("PrestamoRepository", " Error body: $errorBody")
            Result.failure(Exception("Error al crear el préstamo (${e.code()}): $errorBody"))
        } catch (e: Exception) {
            Log.e("PrestamoRepository", " Error desconocido al crear: ${e.message}", e)
            Result.failure(Exception("Error inesperado: ${e.message}"))
        }
    }

    suspend fun getPrestamo(id: Int): Result<PrestamoDto> = withContext(Dispatchers.IO) {
        try {
            val prestamo = api.getPrestamo(id)
            Result.success(prestamo)
        } catch (e: IOException) {
            Result.failure(Exception("No se pudo conectar al servidor."))
        } catch (e: HttpException) {
            Result.failure(Exception("Error del servidor (${e.code()})"))
        }
    }

    suspend fun updatePrestamo(id: Int, prestamo: PrestamoDto): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            api.putPrestamo(id, prestamo)
            Result.success(Unit)
        } catch (e: IOException) {
            Result.failure(Exception("No hay conexión a Internet."))
        } catch (e: HttpException) {
            Result.failure(Exception("Error al actualizar (${e.code()})"))
        }
    }

    suspend fun deletePrestamo(id: Int): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            api.deletePrestamo(id)
            Log.d("PrestamoRepository", " Préstamo eliminado ID: $id")
            Result.success(Unit)
        } catch (e: IOException) {
            Result.failure(Exception("Error de conexión. No se pudo eliminar."))
        } catch (e: HttpException) {
            Result.failure(Exception("Error del servidor (${e.code()})"))
        }
    }
}