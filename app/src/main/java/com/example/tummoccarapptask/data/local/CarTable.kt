package com.example.tummoccarapptask.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "CarTable")
data class CarTable(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    val model: String,
    val brand: String,
    val number: String,
    val fuelType: String,
    val year: String,
    val age: String
)