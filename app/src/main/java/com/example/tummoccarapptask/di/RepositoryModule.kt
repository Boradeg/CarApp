package com.example.tummoccarapptask.di

import com.example.tummoccarapptask.data.repository.CarRepositoryImpl
import com.example.tummoccarapptask.domain.repository.CarRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindCarRepository(
        impl: CarRepositoryImpl
    ): CarRepository
}