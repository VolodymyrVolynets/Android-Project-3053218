package com.volodymyrv.homeweatherstation

import java.util.Date

class DatabaseManager(private val db: AppDatabase) {

    suspend fun saveSensorData(sensorData: SensorData) {
        db.sensorDataDao().insertSensorData(SensorDataEntity(
            dateTime = sensorData.dateTime,
            sensorType = sensorData.sensorType.name,
            value = sensorData.value
        ))
    }

    suspend fun getSensorDataForSpecificDayAndType(dateTime: Date, sensorType: SensorType): List<SensorData> {
        return db.sensorDataDao().getSensorDataForSpecificDay(dateTime, sensorType.name).map {
            SensorData(it.dateTime, SensorType.valueOf(it.sensorType), it.value)
        }
    }
}