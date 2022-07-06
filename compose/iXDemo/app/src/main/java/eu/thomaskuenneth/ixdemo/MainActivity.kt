package eu.thomaskuenneth.ixdemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Greeting()
            // LayoutDemo()
            // ButtonDemo()
            // TextFieldDemo()
            // ListDemo()
            AnimationDemo()
        }
    }
}

@Composable
fun AnimationDemo() {
    var visible by remember {
        mutableStateOf(false)
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = {
            visible = !visible
        }) {
            Text(stringResource(id = R.string.click))
        }
        AnimatedVisibility(
            visible = visible
        ) {
            Text(
                text = stringResource(id = R.string.app_name),
                style = MaterialTheme.typography.h1
            )
        }
    }
}

@Composable
fun ListDemo() {
    val callback = { index: Int -> println("$index selected") }
    val list = arrayListOf("1", "2", "3")
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    ) {
        itemsIndexed(list) { index, s ->
            Text(
                text = s,
                modifier = Modifier
                    .fillMaxSize()
                    .clickable { callback(index) },
                style = MaterialTheme.typography.subtitle2
            )
        }
    }
}

@Composable
fun TextFieldDemo() {
    var input by remember { mutableStateOf(TextFieldValue()) }
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = input,
            onValueChange = {
                input = it
            }
        )
        Button(
            onClick = {
                println(input.text)
            },
            enabled = input.text.isNotEmpty()
        ) {
            Text(stringResource(id = R.string.click))
        }
    }
}

@Composable
fun ButtonDemo() {
    Button(onClick = {
        println("clicked")
    }) {
//                Column {
//                    Text("Zeile 1")
//                    Text("Zeile 2")
//                }
        Text(stringResource(id = R.string.click))
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

fun Modifier.drawOnYellow() = this.drawBehind {
    drawRect(color = Color.Yellow)
}

@Composable
fun Greeting() {
//    Text(text = "Hello iX")

    Text(text = "Hello iX",
        modifier = Modifier
            .clickable { println("Hello iX") }
            .border(
                1.dp,
                MaterialTheme.colors.primary
            ))

//    Text(
//        modifier = Modifier.drawOnYellow(),
//        text = "Hello Compose"
//    )

//    Button(onClick = {
//        println("clicked")
//    }) {
//        Text(stringResource(id = R.string.click))
//    }

//    Button(onClick = {
//        println("clicked")
//    }) {
//        Column {
//            Text("Zeile 1")
//            Text("Zeile 2")
//        }
//    }
}