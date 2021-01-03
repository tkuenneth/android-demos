package com.thomaskuenneth.androidbuch.drawingdemo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.preferredHeight
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

class CanvasDemoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CanvasContent()
        }
    }
}

@Composable
fun SimpleCanvas() {
    Canvas(modifier = Modifier.fillMaxWidth().preferredHeight(128.dp),
        onDraw = {
            drawLine(
                Color.Black, Offset(0f, 0f),
                Offset(size.width - 1, size.height - 1)
            )
            drawLine(
                Color.Black, Offset(0f, size.height - 1),
                Offset(size.width - 1, 0f)
            )
            drawCircle(
                Color.Red, 64f,
                Offset(size.width / 2, size.height / 2)
            )
        })
}

@Preview
@Composable
fun CanvasContent() {
    Surface {
        SimpleCanvas()
    }
}