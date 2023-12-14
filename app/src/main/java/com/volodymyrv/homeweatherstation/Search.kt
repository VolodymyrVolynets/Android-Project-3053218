@file:OptIn(ExperimentalMaterial3Api::class)

package com.volodymyrv.homeweatherstation

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.DatePicker
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.volodymyrv.homeweatherstation.ui.theme.HomeWeatherStationTheme
import java.util.Calendar

class Search : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HomeWeatherStationTheme {
                // A surface container using the 'background' color from the theme
                Scaffold(
                    topBar = { MyAppTopBar() }
                ) {
                    Box(modifier = Modifier.padding(it)) {
                        SearchScreen(LocalContext.current)
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
        title = { Text("Search") },
        actions = {
            IconButton(onClick = {
                if (context is ComponentActivity) {
                    context.finish()
                }
            }) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
            }
        }
    )
}

@Composable
fun SearchScreen(context: Context) {
    val context = LocalContext.current
    var datePicked by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf("") }
    var selectDatePressed by remember { mutableStateOf(false) }

    Column {
        if (selectDatePressed) {
            DatePicker(context = context, datePicked = datePicked, onDateSelected = { date ->
                selectedDate = date
                datePicked = true
            })
        }
        Button(onClick = {
            datePicked = false
            selectDatePressed = true
        }) {
            Text("Select Date")
        }

        if (datePicked) {
            val data = remember { mutableStateListOf<SensorDataEntity>() }
            Thread {
                val db = AppDatabase.getDatabase(context)
                val dataList: List<SensorDataEntity> = db.sensorDataDao().getAll()
                for (i in dataList) {
                    data.add(i)
                }
            }.start()

            for (i in data) {
                Column {
                    Row {
                        Text(text = i.sensorType)
                        Text(text = i.dateTime.toString())
                        Text(text = i.value.toString())
                    }
                }
            }
        }
    }
}

//Data picker composable function
@Composable
fun DatePicker(context: Context, datePicked: Boolean, onDateSelected: (String) -> Unit) {
    val calendar = Calendar.getInstance()
    if (!datePicked) {
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(context, { _, year, monthOfYear, dayOfMonth ->
            // Display the selected date by updating the state
            onDateSelected("$dayOfMonth/${monthOfYear + 1}/$year")
        }, year, month, day)

        LaunchedEffect(key1 = context) {
            dpd.show()
        }
    }
}
