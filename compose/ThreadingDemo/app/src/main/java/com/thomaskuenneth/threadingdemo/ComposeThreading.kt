package com.thomaskuenneth.threadingdemo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.res.stringResource
import com.thomaskuenneth.threadingdemo.ui.theme.ThreadingDemoTheme
import java.net.URL

class ComposeThreading : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val txt1 = stringResource(id = R.string.not_clicked)
            val msg = remember { mutableStateOf(txt1) }
            Content(msg) {
                msg.value = getHello()
            }
        }
    }
}

@Composable
fun Content(msg: State<String>, onClick: () -> Unit) {
    ThreadingDemoTheme {
        Surface(color = MaterialTheme.colors.background) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Button(onClick = onClick) {
                    Text(text = stringResource(id = R.string.click))
                }
                Text(text = msg.value, style = MaterialTheme.typography.h4)
            }
        }
    }
}

private fun getHello() = URL("http://10.0.2.2:8080/hello")
    .openStream()
    .bufferedReader()
    .use { it.readText() }
