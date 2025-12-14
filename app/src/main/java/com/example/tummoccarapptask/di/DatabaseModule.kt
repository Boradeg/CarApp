package com.example.tummoccarapptask.di

import android.content.Context
import androidx.room.Room
import com.example.tummoccarapptask.data.local.CarDao
import com.example.tummoccarapptask.data.local.CarDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    // FIX 2: Corrected the return type and the class reference in Room.databaseBuilder
    fun provideCarDatabase(@ApplicationContext context: Context): CarDatabase {
        return Room.databaseBuilder(
            context,
            CarDatabase::class.java, // Changed from Cat::class.java
            "car_database"
        ).build()
    }

    @Provides
    @Singleton
    // FIX 3: Corrected the parameter type to match the provider above
    fun provideCarDao(database: CarDatabase): CarDao {
        return database.roomDao()
    }
}