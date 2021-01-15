package com.thomaskuenneth.androidbuch.drawingdemo

import android.graphics.Paint
import android.graphics.Typeface
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.preferredHeight
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlin.math.PI
import kotlin.math.sin

private lateinit var customTypeface: Typeface

class CanvasDemoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // See https://style64.org/c64-truetype
            // customTypeface = resources.getFont(R.font.c64_pro_mono_style)
            CanvasContent()
        }
    }
}

@Composable
fun SimpleCanvas() {
    Canvas(modifier = Modifier
        .fillMaxWidth()
        .preferredHeight(128.dp),
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
//            drawCircle(
//                Color.Red, 64f,
//                Offset(size.width / 2, size.height / 2),
//                style = Stroke(width = 8f,
//                    pathEffect = DashPathEffect(floatArrayOf(10f, 10f), 0f)
//                ),
//            )
        })
}

@Composable
fun CanvasWithGradient() {
    Canvas(modifier = Modifier
        .fillMaxWidth()
        .preferredHeight(128.dp),
        onDraw = {
            val gradient = LinearGradient(
                listOf(Color.Blue, Color.Black),
                startX = size.width / 2 - 64, startY = size.height / 2 - 64,
                endX = size.width / 2 + 64, endY = size.height / 2 + 64,
                tileMode = TileMode.Clamp
            )
//            val gradient = RadialGradient(
//                listOf(Color.Black, Color.Blue),
//                centerX = center.x, centerY = center.y,
//                radius = 64f
//            )
            drawCircle(
                gradient, 64f,
            )
        })
}

@Composable
fun SinusPlotter() {
    Canvas(modifier = Modifier.fillMaxSize(),
        onDraw = {
            val middleW = size.width / 2
            val middleH = size.height / 2
            drawLine(Color.Gray, Offset(0f, middleH), Offset(size.width - 1, middleH))
            drawPath(
                path = Path().apply {
                    moveTo(middleW, 0f)
                    relativeLineTo(-20f, 20f)
                    relativeLineTo(40f, -0F)
                    close()
                },
                Color.Gray,
            )
            drawLine(Color.Gray, Offset(middleW, 0f), Offset(middleW, size.height - 1))
            drawPath(
                path = Path().apply {
                    moveTo(size.width - 1, middleH)
                    relativeLineTo(-20f, 20f)
                    relativeLineTo(0f, -40F)
                    close()
                },
                Color.Gray,
            )
            val points = mutableListOf<Offset>()
            for (x in 0 until size.width.toInt()) {
                val y = (sin(x * (2f * PI / size.width)) * middleH + middleH).toFloat()
                points.add(Offset(x.toFloat(), y))
            }
            drawPoints(
                points = points,
                strokeWidth = 4f,
                pointMode = PointMode.Points,
                color = Color.Blue
            )
        }
    )
}

@Composable
fun TextDemo() {
    Canvas(modifier = Modifier
        .fillMaxWidth()
        .preferredHeight(128.dp),
        onDraw = {
            val paint = android.graphics.Paint()
            paint.textAlign = Paint.Align.CENTER
            paint.textSize = 64f
            paint.color = 0xffff0000.toInt()
            drawIntoCanvas {
                it.nativeCanvas.drawText("Hello", center.x, center.y, paint)
            }
//            drawContext.canvas.nativeCanvas.drawText("Hello", center.x, center.y, paint)
        })
}

@Composable
fun C64TextDemo() {
    Canvas(modifier = Modifier
        .fillMaxWidth()
        .preferredHeight(128.dp),
        onDraw = {
            drawRect(Color(0xff525ce6))
            val paint = Paint()
            paint.textAlign = Paint.Align.CENTER
            paint.textSize = 64f
            paint.color = 0xffb0b3ff.toInt()
            paint.typeface = customTypeface
            //paint.flags = Paint.UNDERLINE_TEXT_FLAG
            //paint.flags = Paint.STRIKE_THRU_TEXT_FLAG
            drawContext.canvas.nativeCanvas.drawText(
                "HELLO WORLD!",
                center.x, center.y, paint
            )
        })
}

fun Modifier.drawOnYellow() = this.drawBehind {
    drawRect(Color.Yellow)
}

fun Modifier.drawRedCross() = this.drawWithContent {
    drawContent()
    drawLine(
        Color.Red, Offset(0f, 0f),
        Offset(size.width - 1, size.height - 1),
        blendMode = BlendMode.SrcAtop,
        strokeWidth = 8f
    )
    drawLine(
        Color.Red, Offset(0f, size.height - 1),
        Offset(size.width - 1, 0f),
        blendMode = BlendMode.SrcAtop,
        strokeWidth = 8f
    )
}

@Preview
@Composable
fun CanvasContent() {
    Surface {
        Column() {
            Text(
                modifier = Modifier.drawOnYellow(),
                text = "Hello Compose"
            )
            Text(
                modifier = Modifier.drawRedCross(),
                text = "Imperative"
            )
            //SimpleCanvas()
            //CanvasWithGradient()
            //SinusPlotter()
            //C64TextDemo()
        }
    }
}