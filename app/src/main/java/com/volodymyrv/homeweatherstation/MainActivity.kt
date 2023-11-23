@file:OptIn(ExperimentalMaterial3Api::class)

package com.volodymyrv.homeweatherstation

import android.R
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import com.volodymyrv.homeweatherstation.ui.theme.HomeWeatherStationTheme
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {

    private var temperature by mutableStateOf(0f)
    private var humidity by mutableStateOf(0f)
    private var pressure by mutableStateOf(0)
    private var luminosity by mutableStateOf(0)

    //variable are not used in UI
    private var isSaveTemperature = true
    private var isSaveHumidity = true
    private var isSavePressure = true
    private var isSaveLuminosity = true
    private var howOftenSave = 30f

    //Sensors
    private val sensorEventListener: SensorEventListener = object: SensorEventListener {
        override fun onSensorChanged(event: SensorEvent?) {
            if (event?.sensor?.type == Sensor.TYPE_AMBIENT_TEMPERATURE) {
                temperature = event.values[0]
            } else if (event?.sensor?.type == Sensor.TYPE_RELATIVE_HUMIDITY) {
                humidity = event.values[0]
            } else if (event?.sensor?.type == Sensor.TYPE_PRESSURE) {
                pressure = event.values[0].toInt()
            } else if (event?.sensor?.type == Sensor.TYPE_LIGHT) {
                luminosity = event.values[0].toInt()
            }
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        }
    }

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val context = LocalContext.current
            val sensorManager: SensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
            val temperatureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE)
            val humiditySensor = sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY)
            val pressureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE)
            val lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
            sensorManager.registerListener(sensorEventListener, temperatureSensor, SensorManager.SENSOR_DELAY_NORMAL)
            sensorManager.registerListener(sensorEventListener, humiditySensor, SensorManager.SENSOR_DELAY_NORMAL)
            sensorManager.registerListener(sensorEventListener, pressureSensor, SensorManager.SENSOR_DELAY_NORMAL)
            sensorManager.registerListener(sensorEventListener, lightSensor, SensorManager.SENSOR_DELAY_NORMAL)

            HomeWeatherStationTheme {
                Scaffold(
                    topBar = { MyAppTopBar() },
                    modifier = Modifier.fillMaxSize()
                ) {
                    Box(modifier = Modifier.padding(it)) {
                        MainScreen(temperature, humidity, pressure, luminosity)
                    }
                }
            }
        }
    }

    // this called after child activity finishes.
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                // Get the result from intent
                if (data == null) return
                isSaveTemperature = data.getBooleanExtra("isSaveTemperature", true)
                isSaveHumidity = data.getBooleanExtra("isSaveHumidity", true)
                isSavePressure = data.getBooleanExtra("isSavePressure", true)
                isSaveLuminosity = data.getBooleanExtra("isSaveLuminosity", true)
                howOftenSave = data.getFloatExtra("howOftenSave", 30f)
            }
        }
    }

    @Composable
    private fun MainScreen(temperature: Float, humidity: Float, pressure: Int, luminosity: Int) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top
        ) {
            MyCard("Temperature: $temperature °С")
            MyCard("Humidity: $humidity %")
            MyCard("Pressure: $pressure hPa")
            MyCard("Luminosity: $luminosity lux")
        }
    }

    @Composable
    private fun MyCard(string: String) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Row(
                modifier = Modifier.padding(10.dp)
            ) {
                Text(string, fontSize = 30.sp, fontWeight = FontWeight.Bold)
            }
        }
    }

    @Composable
    private fun MyAppTopBar() {
        val context: Context = LocalContext.current

        TopAppBar(
            title = { Text("Home Weather Station") },
            actions = {
                IconButton(onClick = {
                    val intent = Intent(context, Search::class.java)
                    context.startActivity(intent)
                }) {
                    Icon(Icons.Filled.Search, contentDescription = "Search")
                }

                IconButton(onClick = {
                    val intent = Intent(context, Settings::class.java)
                    intent.putExtra("isSaveTemperature", isSaveTemperature)
                    intent.putExtra("isSaveHumidity", isSaveHumidity)
                    intent.putExtra("isSavePressure", isSavePressure)
                    intent.putExtra("isSaveLuminosity", isSaveLuminosity)
                    intent.putExtra("howOftenSave", howOftenSave)
                    startActivityForResult(intent, 0)
                }) {
                    Icon(Icons.Filled.Settings, contentDescription = "Settings")
                }
            }
        )
    }
}
