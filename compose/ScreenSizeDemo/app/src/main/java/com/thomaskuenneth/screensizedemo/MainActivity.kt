package com.thomaskuenneth.screensizedemo

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.core.util.Consumer
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigate
import androidx.navigation.compose.rememberNavController
import androidx.window.DisplayFeature
import androidx.window.FoldingFeature
import androidx.window.WindowBackend
import androidx.window.WindowManager
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

private var isTwoColumnMode by mutableStateOf(false)

private var weightModuleSelection by mutableStateOf(0.3F)
private var weightModule by mutableStateOf(0.7F)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val wm = WindowManager(this)
        wm.registerLayoutChangeCallback(mainExecutor, {
            it.displayFeatures.forEach { feature ->
                (feature as FoldingFeature).run {
                    if (isSeparating) {
                        weightModuleSelection = 0.5F
                        weightModule = 0.5F
                    }
                }
            }
        })
        setContent {
            ScreenSizeDemo()
        }
    }
}

@Composable
@Preview
fun ScreenSizeDemo() {
    ScreenSizeDemoTheme {
        Scaffold {
//            isTwoColumnMode = (LocalConfiguration.current.orientation
//                    == Configuration.ORIENTATION_LANDSCAPE)
            isTwoColumnMode = LocalConfiguration.current.screenWidthDp >= 600
            if (isTwoColumnMode)
                Landscape(module, weightModuleSelection, weightModule)
            else
                Portrait(module)
        }
    }
}

@Composable
fun Portrait(module: MutableState<Module?>) {
    val navController = rememberNavController()
    NavHost(navController, startDestination = "moduleSelection") {
        composable("moduleSelection") {
            ModuleSelectionList(
                modules,
                callback = {
                    module.value = it
                    navController.navigate("module")
                }
            )
        }
        composable("module") {
            module.value?.let {
                Module(it)
            }
        }
    }
}

@Composable
fun Landscape(
    module: MutableState<Module?>,
    weightModuleSelection: Float,
    weightModule: Float
) {
    Row(modifier = Modifier.fillMaxSize()) {
        ModuleSelection(
            module = module,
            modifier = Modifier.weight(weight = weightModuleSelection)
        )
        module.value?.let {
            Module(
                module = it,
                modifier = Modifier.weight(weightModule)
            )
        }
    }
}

@Composable
fun ModuleSelection(module: MutableState<Module?>, modifier: Modifier = Modifier) {
    ModuleSelectionList(
        modules,
        callback = {
            module.value = it
        },
        modifier = modifier
    )
}

@Composable
fun ModuleSelectionList(
    modules: List<Module>,
    callback: (module: Module) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    ) {
        items(modules) { module ->
            ModuleRow(module, callback)
        }
    }
}

@Composable
fun ModuleRow(module: Module, callback: (module: Module) -> Unit) {
    Column(
        modifier = Modifier
            .clickable(onClick = {
                callback(module)
            })
            .fillMaxWidth()
    ) {
        Text(
            text = module.title,
            style = MaterialTheme.typography.subtitle1
        )
        Text(
            text = module.description,
            style = MaterialTheme.typography.body2
        )
    }
}

@Composable
fun Module(module: Module, modifier: Modifier = Modifier) {
    Text(
        text = module.title,
        style = MaterialTheme.typography.h1,
        modifier = modifier.fillMaxSize()
    )
}