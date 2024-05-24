package com.fleaudie.amvflix.di

import com.fleaudie.amvflix.data.datasource.AnimeListDataSource
import com.fleaudie.amvflix.data.datasource.AuthDataSource
import com.fleaudie.amvflix.data.retrofit.AnimeListApiService
import com.fleaudie.amvflix.repositories.AnimeListRepository
import com.fleaudie.amvflix.repositories.AuthRepository
import com.fleaudie.amvflix.viewmodel.SignUpViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideAuthDataSource(): AuthDataSource {
        return AuthDataSource()
    }

    @Singleton
    @Provides
    fun provideAuthRepository(authDataSource: AuthDataSource): AuthRepository {
        return AuthRepository(authDataSource)
    }

    @Singleton
    @Provides
    fun provideAuthViewModel(authRepository: AuthRepository): SignUpViewModel {
        return SignUpViewModel(authRepository)
    }

    @Singleton
    @Provides
    fun provideAnimeApiService(): AnimeListApiService {
        return Retrofit.Builder()
            .baseUrl("https://raw.githubusercontent.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AnimeListApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideAnimeDataSource(apiService: AnimeListApiService): AnimeListDataSource {
        return AnimeListDataSource(apiService)
    }

    @Singleton
    @Provides
    fun provideAnimeRepository(dataSource: AnimeListDataSource): AnimeListRepository {
        return AnimeListRepository(dataSource)
    }

}