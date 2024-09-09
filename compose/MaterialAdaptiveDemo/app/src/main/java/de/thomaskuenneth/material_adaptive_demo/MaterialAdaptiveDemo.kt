package de.thomaskuenneth.material_adaptive_demo

import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.currentWindowSize
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable
fun MaterialAdaptiveDemo() {
    var currentDestination by rememberSaveable { mutableStateOf(AppDestinations.Home) }
    val adaptiveInfo = currentWindowAdaptiveInfo()
    val density = LocalDensity.current
    val customNavSuiteType =
        // if (adaptiveInfo.windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.EXPANDED) {
        if (with(density) { currentWindowSize().width.toDp() >= 1200.dp }) {
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
