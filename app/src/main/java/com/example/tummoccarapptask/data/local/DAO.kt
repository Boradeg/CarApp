package com.example.tummoccarapptask.data.local

import android.content.Context
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import javax.inject.Singleton

@Dao
interface CarDao {

    @Query("SELECT * FROM CarTable")
    fun getCarData(): Flow<List<CarTable>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCar(car: CarTable)
}



