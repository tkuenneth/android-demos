package com.thomaskuenneth.androidbuch.drawingdemo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp

class ShapesDemoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Content()
        }
    }
}

@Composable
fun Content() {
    Column(modifier = Modifier.fillMaxWidth()) {
        RowWithThreeRectangles()
        RowWithThreeCircles()
        RowWithATriangle()
        RowWithADonut()
    }
}

@Composable
fun RowWithThreeRectangles() {
    Row(modifier = Modifier.fillMaxWidth()
    ) {
        Rectangle(color = Color.Red)
        Rectangle(modifier = Modifier.weight(0.3f)
                .height(64.dp),
                color = Color.Blue)
        Rectangle(modifier = Modifier.weight(0.7f),
                color = Color.Green)
    }
}

@Composable
fun RowWithThreeCircles() {
    Row(modifier = Modifier.fillMaxWidth()
    ) {
        Circle(color = Color.Red)
        Circle(color = Color.Blue)
        Circle(color = Color.Green)
    }
}

@Composable
fun RowWithATriangle() {
    Row() {
        Triangle(modifier = Modifier.fillMaxWidth(),
                color = Color.Yellow)
    }
}

@Composable
fun RowWithADonut() {
    Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center) {
        Donut()
    }
}

@Composable
fun Rectangle(color: Color,
              modifier: Modifier = Modifier) {
    Box(
            modifier = modifier.composed {
                preferredSize(32.dp)
                        .clip(RectangleShape)
                        .background(color)
            }
    )
}

@Composable
fun Circle(color: Color,
           modifier: Modifier = Modifier) {
    Box(
            modifier = modifier.composed {
                preferredSize(32.dp)
                        .clip(CircleShape)
                        .background(color)
            }
    )
}

@Composable
fun Triangle(color: Color,
             modifier: Modifier = Modifier) {
    Box(
            modifier = modifier.composed {
                preferredSize(64.dp)
                        .clip(GenericShape { size ->
                            moveTo(size.width / 2, 0f)
                            lineTo(size.width - 1, size.height - 1)
                            relativeLineTo(-size.width, 0f)
                            // relativeLineTo(32f, -32f)
                        })
                        .background(color)
            }
    )
}

@Composable
fun Donut() {
    Surface(modifier = Modifier.preferredSize(100.dp),
            color = Color.Red,
            shape = object : Shape {
                override fun createOutline(size: Size, density: Density): Outline {
                    val thickness = size.height / 4
                    val p1 = Path().apply {
                        addOval(Rect(0f, 0f, size.width - 1, size.height - 1))
                    }
                    val p2 = Path().apply {
                        addOval(Rect(thickness,
                                thickness,
                                size.width - 1 - thickness,
                                size.height - 1 - thickness))
                    }
                    val p3 = Path()
                    p3.op(p1, p2, PathOperation.difference)
                    return Outline.Generic(p3)
                }
            }
    ) {
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Content()
}