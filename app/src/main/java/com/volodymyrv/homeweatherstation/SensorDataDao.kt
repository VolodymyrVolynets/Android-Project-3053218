package com.volodymyrv.homeweatherstation

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import java.util.Date

@Dao
interface SensorDataDao {
    @Insert
    suspend fun insertSensorData(sensorData: SensorDataEntity)

    @Query("SELECT * FROM SensorDataEntity WHERE date(dateTime) = date(:date) AND sensorType = :sensorType")
    fun getSensorDataForSpecificDay(date: Date, sensorType: String): List<SensorDataEntity>

    // Other necessary queries
}
