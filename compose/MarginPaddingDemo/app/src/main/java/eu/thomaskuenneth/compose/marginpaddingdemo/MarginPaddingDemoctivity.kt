package eu.thomaskuenneth.compose.marginpaddingdemo

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import eu.thomaskuenneth.compose.marginpaddingdemo.ui.theme.MarginPaddingDemoTheme
import eu.thomaskuenneth.compose.marginpaddingdemo.ui.theme.Purple200
import eu.thomaskuenneth.compose.marginpaddingdemo.ui.theme.Purple500
import eu.thomaskuenneth.compose.marginpaddingdemo.ui.theme.Teal700

class MarginPaddingDemoctivity : ComponentActivity() {
    @SuppressLint("InflateParams")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LayoutInflater.from(this).inflate(R.layout.layout, null).let {
            setContent {
                MainScreen(it)
            }
        }
    }
}

@Composable
fun MainScreen(layout: View) {
    MarginPaddingDemoTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                AndroidView(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.5f),
                    factory = {
                        layout
                    }
                )
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.5f)
                ) {
                    Buttons()
                }
            }
        }
    }
}

@Composable
@Preview
fun Buttons() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .background(color = Teal700)
            .padding(32.dp)
    ) {
        OutlinedButton(modifier = Modifier
            .background(Purple200)
            .padding(32.dp),
            onClick = {}) {
            Text("Button #1")
        }
        Box(
            modifier = Modifier
                .background(color = Purple500)
                .padding(32.dp),
            contentAlignment = Alignment.Center,
        ) {
            OutlinedButton(
                onClick = {}) {
                Text(
                    text = "Button #2",
                    modifier = Modifier.padding(
                        32.dp
                    )
                )
            }
        }
    }
}