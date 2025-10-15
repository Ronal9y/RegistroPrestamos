package edu.ucne.registroprestamos.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import edu.ucne.registroprestamos.data.remote.PrestamoApi
import edu.ucne.registroprestamos.data.repository.PrestamoRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providePrestamoRepository(api: PrestamoApi): PrestamoRepository {
        return PrestamoRepository(api)
    }
}