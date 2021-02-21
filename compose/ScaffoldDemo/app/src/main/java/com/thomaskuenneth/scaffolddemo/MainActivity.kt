package com.thomaskuenneth.scaffolddemo

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.material.*
import androidx.compose.material.icons.Icons.Default
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.thomaskuenneth.scaffolddemo.ui.theme.ScaffoldDemoTheme


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
    ScaffoldDemoTheme {
        Scaffold(topBar = {
            TopAppBar(title = {
                Column {
                    Text("Title")
                    Text("Subtitle", style = MaterialTheme.typography.subtitle1)
                }
            },
                actions = {
                    IconButton(onClick = {
                        println("Icons.Default.Add")
                    }) {
                        Icon(Default.Add, null)
                    }
                    IconButton(onClick = {
                        println("Icons.Default.Delete")
                    }) {
                        Icon(Default.Delete, null)
                    }
                })
        }) {
            MyContent()
        }
    }
}

@Composable
fun MyContent() {
    Text(text = "Hello!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Content()
}