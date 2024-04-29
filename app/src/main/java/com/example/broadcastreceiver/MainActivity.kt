package com.example.broadcastreceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.broadcastreceiver.ui.theme.BroadCastReceiverTheme

class MainActivity : ComponentActivity() {
   // val initialStatus = isAirplaneModeOn(this@MainActivity)

    private  var isAirplaneModeOn: Boolean by mutableStateOf(false)
    private val airplaneModeReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            isAirplaneModeOn = isAirplaneModeOn(context)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BroadCastReceiverTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    //println(isAirplaneModeOn(LocalContext.current))
                    isAirplaneModeOn = isAirplaneModeOn(LocalContext.current)
                    AirplaneModeStatus(isAirplaneModeOn)
                }
            }
        }
    }
    override fun onResume() {
        super.onResume()
        registerAirplaneModeReceiver()
    }
    override fun onPause() {
        super.onPause()
        unregisterAirplaneModeReceiver()
    }
    private fun registerAirplaneModeReceiver() {
        val intentFilter = IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED)
        registerReceiver(airplaneModeReceiver, intentFilter)
    }

    private fun unregisterAirplaneModeReceiver() {
        unregisterReceiver(airplaneModeReceiver)
    }
    @Composable
    fun AirplaneModeStatus(isAirplaneModeOn: Boolean) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Airplane Mode Status:",
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = if (isAirplaneModeOn) "ON" else "OFF",
                fontSize = 24.sp,
                color = if (isAirplaneModeOn) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.secondary
            )
        }
    }
    private fun isAirplaneModeOn(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            Settings.Global.getInt(context.contentResolver, Settings.Global.AIRPLANE_MODE_ON, 0) != 0
        } else {
            Settings.System.getInt(context.contentResolver, Settings.System.AIRPLANE_MODE_ON, 0) != 0
        }
    }
}
