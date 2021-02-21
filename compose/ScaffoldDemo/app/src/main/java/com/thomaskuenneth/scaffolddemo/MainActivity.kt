package com.thomaskuenneth.scaffolddemo

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material.*
import androidx.compose.material.icons.Icons.Default
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import com.thomaskuenneth.scaffolddemo.ui.theme.ScaffoldDemoTheme
import androidx.compose.runtime.*

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
    var menuOpened by remember { mutableStateOf(false) }
    ScaffoldDemoTheme {
        Scaffold(topBar = {
            TopAppBar(title = {
                Column {
                    Text("Title")
                    Text("Subtitle", style = MaterialTheme.typography.subtitle1)
                }
            },
                actions = {
//                    IconButton(onClick = {
//                        println("Icons.Default.Add")
//                    }) {
//                        Icon(Default.Add, null)
//                    }
//                    IconButton(onClick = {
//                        println("Icons.Default.Delete")
//                    }) {
//                        Icon(Default.Delete, null)
//                    }
                    Box {
                        IconButton(onClick = {
                            menuOpened = true
                        }) {
                            Icon(Default.MoreVert, null)
                            DropdownMenu(expanded = menuOpened,
                                onDismissRequest = {
                                    menuOpened = false
                                }) {
                                DropdownMenuItem(onClick = {
                                    menuOpened = false
                                }) {
                                    Text("Item #1")
                                }
                                Divider()
                                DropdownMenuItem(onClick = {
                                    menuOpened = false
                                }) {
                                    Text("Item #2")
                                }
                            }
                        }
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