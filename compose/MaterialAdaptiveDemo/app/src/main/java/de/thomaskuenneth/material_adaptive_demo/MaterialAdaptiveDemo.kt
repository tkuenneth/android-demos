package de.thomaskuenneth.material_adaptive_demo

import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.window.core.layout.WindowWidthSizeClass

@Composable
fun MaterialAdaptiveDemo() {
    var currentDestination by rememberSaveable { mutableStateOf(AppDestinations.Home) }
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
        when (currentDestination) {
            AppDestinations.Home -> {
                HomePane()
            }

            AppDestinations.Info -> {
                InfoPane()
            }
        }
    }
}
