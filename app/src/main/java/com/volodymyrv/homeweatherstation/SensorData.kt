package com.volodymyrv.homeweatherstation

import java.util.Date

data class SensorData(
    val dateTime: Date,  // This now includes both date and time
    val sensorType: SensorType,
    val value: Float
)

enum class SensorType {
    TEMPERATURE,
    HUMIDITY,
    LUMINOSITY,
    PRESSURE
}