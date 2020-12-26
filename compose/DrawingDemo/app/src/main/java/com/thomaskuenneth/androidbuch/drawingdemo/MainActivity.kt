package com.thomaskuenneth.androidbuch.drawingdemo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.thomaskuenneth.androidbuch.drawingdemo.ui.DrawingDemoTheme

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Content()
        }
    }
}

@Composable
fun Content() {
    DrawingDemoTheme {
        Surface(color = MaterialTheme.colors.background) {
            //RowWithThreeRectangles()
            //RowWithThreeCircles()
            RowWithATriangle()
        }
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
    Row(modifier = Modifier.fillMaxWidth()
    ) {
        Triangle(modifier = Modifier.fillMaxWidth(),
                color = Color.Yellow)
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
                            lineTo(size.width / 2, 0f)
                        })
                        .background(color)
            }
    )
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Content()
}