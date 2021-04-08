package com.thomaskuenneth.batterydemo

import android.content.Intent
import android.content.IntentFilter
import android.hardware.input.InputManager
import android.os.BatteryManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.thomaskuenneth.batterydemo.ui.theme.BatteryDemoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getSystemService(BatteryManager::class.java)?.run {
            // Remaining battery capacity as an integer percentage
            // of total capacity
            println(getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY))
            println(
                when (getIntProperty(BatteryManager.BATTERY_PROPERTY_STATUS)) {
                    BatteryManager.BATTERY_STATUS_CHARGING -> "charging"
                    BatteryManager.BATTERY_STATUS_NOT_CHARGING -> "not charging"
                    BatteryManager.BATTERY_STATUS_FULL -> "full"
                    BatteryManager.BATTERY_STATUS_DISCHARGING -> "discharging"
                    else -> "unknwon"
                }
            )
        }

        val filter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        registerReceiver(null, filter)?.run {
            val level = getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
            println(level)
            println(
                when (getIntExtra(
                    BatteryManager.EXTRA_STATUS,
                    BatteryManager.BATTERY_STATUS_UNKNOWN
                )) {
                    BatteryManager.BATTERY_STATUS_CHARGING -> "charging"
                    BatteryManager.BATTERY_STATUS_NOT_CHARGING -> "not charging"
                    BatteryManager.BATTERY_STATUS_FULL -> "full"
                    BatteryManager.BATTERY_STATUS_DISCHARGING -> "discharging"
                    else -> "unknwon"
                }
            )
        }

        val m = getSystemService(InputManager::class.java)
        for (i in m.inputDeviceIds) {
            m.getInputDevice(i)?.run {
                println(name)
                println(battery.javaClass)
            }
        }

        setContent {
            BatteryDemoTheme {
                Surface(color = MaterialTheme.colors.background) {
                    BatteryDemo("Android")
                }
            }
        }
    }
}

@Composable
fun BatteryDemo(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    BatteryDemoTheme {
        BatteryDemo("Android")
    }
}