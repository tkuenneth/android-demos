package com.thomaskuenneth.screensizedemo

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.thomaskuenneth.screensizedemo.ui.theme.ScreenSizeDemoTheme

data class Module(
    val title: String,
    val description: String
)

private val modules = listOf(
    Module(
        "Title #1",
        "description #1"
    ),
    Module(
        "Title #2",
        "description #2"
    ),
    Module(
        "Title #3",
        "description #3"
    )
)

private val module: MutableState<Module?> = mutableStateOf(null)

private var isLandscape by mutableStateOf(false)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ScreenSizeDemo()
        }
    }

    override fun onBackPressed() {
        if (isLandscape)
            super.onBackPressed()
        else {
            if (module.value == null)
                super.onBackPressed()
            else
                module.value = null
        }
    }
}

@Composable
@Preview
fun ScreenSizeDemo() {
    ScreenSizeDemoTheme {
        Scaffold {
            isLandscape = (LocalConfiguration.current.orientation
                    == Configuration.ORIENTATION_LANDSCAPE)
            if (isLandscape)
                Landscape(module)
            else
                Portrait(module)
        }
    }
}

@Composable
fun Portrait(module: MutableState<Module?>) {
    module.value?.let {
        Module(it)
    } ?: ModuleSelection(module)
}

@Composable
fun Landscape(module: MutableState<Module?>) {
    Row {
        ModuleSelection(module)
        module.value?.let {
            Module(it)
        }
    }
}

@Composable
fun ModuleSelection(module: MutableState<Module?>) {
    ModuleSelectionList(
        modules,
        callback = {
            module.value = it
        }
    )
}

@Composable
fun ModuleSelectionList(modules: List<Module>, callback: (module: Module) -> Unit) {
    LazyColumn(contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)) {
        items(modules) { module ->
            ModuleRow(module, callback)
        }
    }
}

@Composable
fun ModuleRow(module: Module, callback: (module: Module) -> Unit) {
    Column(modifier = Modifier.clickable(onClick = {
        callback(module)
    })) {
        Text(module.title, style = MaterialTheme.typography.subtitle1)
        Text(module.description, style = MaterialTheme.typography.body2)
    }
}

@Composable
fun Module(module: Module) {
    Text(module.title, style = MaterialTheme.typography.h1)
}