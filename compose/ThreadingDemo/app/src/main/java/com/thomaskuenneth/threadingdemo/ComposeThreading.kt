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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.thomaskuenneth.threadingdemo.ui.theme.ThreadingDemoTheme
import java.net.URL

class ComposeThreading : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Content()
        }
    }
}

@Composable
@Preview
fun Content() {
    val txt1 = stringResource(id = R.string.not_clicked)
    val txt2 = stringResource(id = R.string.greeting)
    val msg = remember { mutableStateOf(txt1) }
    ThreadingDemoTheme {
        Surface(color = MaterialTheme.colors.background) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Button(onClick = {
                    msg.value = txt2
                }) {
                    Text(text = stringResource(id = R.string.click))
                }
                Text(text = msg.value, style = MaterialTheme.typography.h4)
            }
        }
    }
}
