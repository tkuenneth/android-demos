package de.thomaskuenneth.material_adaptive_demo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.window.core.layout.WindowWidthSizeClass

class MaterialAdaptiveDemoActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                MaterialAdaptiveDemo()
            }
        }
    }
}

@Composable
fun MaterialAdaptiveDemo() {
    var currentDestination by remember { mutableStateOf(AppDestinations.Home) }
    val adaptiveInfo = currentWindowAdaptiveInfo()
    val customNavSuiteType =
        if (adaptiveInfo.windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.EXPANDED) {
            NavigationSuiteType.NavigationDrawer
        } else {
            NavigationSuiteScaffoldDefaults.calculateFromAdaptiveInfo(adaptiveInfo)
        }
    NavigationSuiteScaffold(layoutType = customNavSuiteType, navigationSuiteItems = {
        AppDestinations.entries.forEach {
            item(
                selected = it == currentDestination,
                onClick = { currentDestination = it },
                icon = {
                    Icon(
                        imageVector = it.icon,
                        contentDescription = stringResource(it.contentDescription)
                    )
                },
                label = {
                    Text(text = stringResource(it.labelRes))
                },
            )
        }
    }) {
        Text(text = currentDestination.name)
    }
}
