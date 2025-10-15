package edu.ucne.registroprestamos.data.remote

import edu.ucne.registroprestamos.data.remote.dto.PrestamoDto
import retrofit2.http.*

interface PrestamoApi {
    @GET("api/Prestamos")
    suspend fun getPrestamos(): List<PrestamoDto>

    @GET("api/Prestamos/{id}")
    suspend fun getPrestamo(@Path("id") id: Int): PrestamoDto

    @POST("api/Prestamos")
    suspend fun postPrestamo(@Body prestamo: PrestamoDto): PrestamoDto

    @PUT("api/Prestamos/{id}")
    suspend fun putPrestamo(@Path("id") id: Int, @Body prestamo: PrestamoDto)

    @DELETE("api/Prestamos/{id}")
    suspend fun deletePrestamo(@Path("id") id: Int)
}