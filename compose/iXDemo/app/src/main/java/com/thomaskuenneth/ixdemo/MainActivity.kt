package com.thomaskuenneth.ixdemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Greeting()
            LayoutDemo()
        }
    }
}

@Preview
@Composable
fun LayoutDemo() {
    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.5F)
        ) {
            Text(text = "1", modifier = Modifier.weight(0.3F))
            Text(text = "2", modifier = Modifier.weight(0.7F))
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.5F)
        ) {
            Text("3")
            Text("4")
        }
    }
}

@Composable
fun Greeting() {
    Text(text = "Hello iX")
}