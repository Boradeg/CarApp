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
    fun provideCarDatabase(@ApplicationContext context: Context): CarDatabase {
        return Room.databaseBuilder(
            context,
            CarDatabase::class.java,
            "car_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideCarDao(database: CarDatabase): CarDao {
        return database.roomDao()
    }

}
