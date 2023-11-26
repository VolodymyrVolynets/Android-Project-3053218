@file:OptIn(ExperimentalMaterial3Api::class)

package com.volodymyrv.homeweatherstation

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.volodymyrv.homeweatherstation.ui.theme.HomeWeatherStationTheme


class Settings : ComponentActivity() {

    //setting variables
    private var isSaveTemperature by mutableStateOf(true)
    private var isSaveHumidity by mutableStateOf(true)
    private var isSavePressure by mutableStateOf(true)
    private var isSaveLuminosity by mutableStateOf(true)
    var howOftenSave by mutableStateOf(30f)


    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            //gets data from intent
            isSaveTemperature = intent.getBooleanExtra("isSaveTemperature", true)
            isSaveHumidity = intent.getBooleanExtra("isSaveHumidity", true)
            isSavePressure = intent.getBooleanExtra("isSavePressure", true)
            isSaveLuminosity = intent.getBooleanExtra("isSaveLuminosity", true)
            howOftenSave = intent.getFloatExtra("howOftenSave", 30f)

            HomeWeatherStationTheme {
                // A surface container using the 'background' color from the theme
                Scaffold(
                    topBar = { MyAppTopBar() }
                ) {
                    Box(modifier = Modifier.padding(it)) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(15.dp)
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                            ) {
                                Text("Save Temperature")
                                
                                Switch(checked = isSaveTemperature, onCheckedChange = {
                                    isSaveTemperature = it
                                })
                            }

                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                            ) {
                                Text("Save Humidity")

                                Switch(checked = isSaveHumidity, onCheckedChange = {
                                    isSaveHumidity = it
                                })
                            }

                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                            ) {
                                Text("Save Pressure")

                                Switch(checked = isSavePressure, onCheckedChange = {
                                    isSavePressure = it
                                })
                            }

                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                            ) {
                                Text("Save Luminosity")

                                Switch(checked = isSaveLuminosity, onCheckedChange = {
                                    isSaveLuminosity = it
                                })
                            }

                            Column(
                                modifier = Modifier
                                .fillMaxWidth()
                            ) {
                                Text("How often save data on your devise: ${howOftenSave.toInt()}S")
                                Slider(
                                    value = howOftenSave,
                                    onValueChange = { howOftenSave = it },
                                    colors = SliderDefaults.colors(
                                        thumbColor = MaterialTheme.colorScheme.secondary,
                                        activeTrackColor = MaterialTheme.colorScheme.secondary,
                                        inactiveTrackColor = MaterialTheme.colorScheme.secondaryContainer,
                                    ),
                                    steps = 27,
                                    valueRange = 30f..300f
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun MyAppTopBar() {
        val context: Context = LocalContext.current

        TopAppBar(
            title = { Text("Settings") },
            actions = {
                //if back button clicked create intent and pass data back
                IconButton(onClick = {
                    if (context is ComponentActivity) {
                        val intent = Intent()
                        intent.putExtra("isSaveTemperature", isSaveTemperature)
                        intent.putExtra("isSaveHumidity", isSaveHumidity)
                        intent.putExtra("isSavePressure", isSavePressure)
                        intent.putExtra("isSaveLuminosity", isSaveLuminosity)
                        intent.putExtra("howOftenSave", howOftenSave)
                        setResult(RESULT_OK, intent)

                        finish()
                    }
                }) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                }
            }
        )
    }
}