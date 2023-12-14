package com.volodymyrv.homeweatherstation

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity
data class SensorDataEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,

    val dateTime: Date,  // This includes both date and time
    val sensorType: String,
    val value: Float
)