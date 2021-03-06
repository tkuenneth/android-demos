package com.thomaskuenneth.scaffolddemo

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.Icons.Default
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import com.thomaskuenneth.scaffolddemo.ui.theme.ScaffoldDemoTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp

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
                    var height = 0
                    Box(modifier = Modifier.onGloballyPositioned {
                        height = it.size.height
                    }) {
                        IconButton(onClick = {
                            menuOpened = true
                        }) {
                            Icon(Default.MoreVert, null)
                            DropdownMenu(expanded = menuOpened,
                                offset = DpOffset(
                                    0.dp,
                                    -(LocalDensity.current.density * height).dp
                                ),
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
        },
            bottomBar = {
//                BottomAppBar {
//                    IconButton(onClick = { }) {
//                        Icon(Icons.Filled.Menu, contentDescription = null)
//                    }
//                    Spacer(Modifier.weight(1f, true))
//                    IconButton(onClick = { }) {
//                        Icon(Icons.Filled.Favorite, contentDescription = null)
//                    }
//                    IconButton(onClick = { }) {
//                        Icon(Icons.Filled.MoreVert, contentDescription = null)
//                    }
//                }

                BottomNavigation() {
                    BottomNavigationItem(
                        icon = { Icon(Icons.Filled.Info, contentDescription = null) },
                        label = { Text("About") },
                        selected = false,
                        onClick = { }
                    )
                    BottomNavigationItem(
                        icon = { Icon(Icons.Filled.Settings, contentDescription = null) },
                        label = { Text("Settings") },
                        selected = false,
                        onClick = { }
                    )
                }
            }
            ) {
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