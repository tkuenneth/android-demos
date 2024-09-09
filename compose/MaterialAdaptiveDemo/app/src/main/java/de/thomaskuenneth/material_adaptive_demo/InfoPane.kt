package de.thomaskuenneth.material_adaptive_demo

import androidx.activity.compose.BackHandler
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.PaneAdaptedValue
import androidx.compose.material3.adaptive.layout.SupportingPaneScaffold
import androidx.compose.material3.adaptive.layout.SupportingPaneScaffoldRole
import androidx.compose.material3.adaptive.layout.ThreePaneScaffoldScope
import androidx.compose.material3.adaptive.layout.calculatePaneScaffoldDirective
import androidx.compose.material3.adaptive.navigation.ThreePaneScaffoldNavigator
import androidx.compose.material3.adaptive.navigation.rememberSupportingPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowWidthSizeClass

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun InfoPane() {
    val navigator = rememberSupportingPaneScaffoldNavigator(
        scaffoldDirective = calculatePaneScaffoldDirective(currentWindowAdaptiveInfo()).copy(
            maxHorizontalPartitions = when (currentWindowAdaptiveInfo().windowSizeClass.windowWidthSizeClass) {
                WindowWidthSizeClass.MEDIUM -> 2
                WindowWidthSizeClass.EXPANDED -> 3
                else -> 1
            }
        )
    )
    BackHandler(navigator.canNavigateBack()) {
        navigator.navigateBack()
    }
    SupportingPaneScaffold(
        directive = navigator.scaffoldDirective,
        mainPane = { MainPane(navigator = navigator) },
        supportingPane = { SupportingPane(navigator = navigator) },
        extraPane = { ExtraPane() },
        value = navigator.scaffoldValue
    )
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun ThreePaneScaffoldScope.MainPane(navigator: ThreePaneScaffoldNavigator<Nothing>) {
    ColoredBox(textColor = MaterialTheme.colorScheme.onPrimaryContainer,
        backgroundColor = MaterialTheme.colorScheme.primaryContainer,
        resIdMessage = R.string.main_pane,
        resIdButton = R.string.show_supporting_pane,
        shouldShowButton = navigator.scaffoldValue[SupportingPaneScaffoldRole.Supporting] == PaneAdaptedValue.Hidden,
        onClick = { navigator.navigateTo(SupportingPaneScaffoldRole.Supporting) })
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun ThreePaneScaffoldScope.SupportingPane(navigator: ThreePaneScaffoldNavigator<Nothing>) {
    ColoredBox(textColor = MaterialTheme.colorScheme.onSecondaryContainer,
        backgroundColor = MaterialTheme.colorScheme.secondaryContainer,
        resIdMessage = R.string.supporting_pane,
        resIdButton = R.string.show_main_pane,
        shouldShowButton = navigator.scaffoldValue[SupportingPaneScaffoldRole.Main] == PaneAdaptedValue.Hidden,
        onClick = { navigator.navigateTo(SupportingPaneScaffoldRole.Main) })
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun ThreePaneScaffoldScope.ExtraPane() {
    ColoredBox(
        textColor = MaterialTheme.colorScheme.onTertiaryContainer,
        backgroundColor = MaterialTheme.colorScheme.tertiaryContainer,
        resIdMessage = R.string.extra_pane,
        resIdButton = -1,
        shouldShowButton = false
    )
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
private fun ThreePaneScaffoldScope.ColoredBox(
    textColor: Color,
    backgroundColor: Color,
    shouldShowButton: Boolean,
    @StringRes resIdMessage: Int,
    @StringRes resIdButton: Int,
    onClick: () -> Unit = {}
) {
    AnimatedPane {
        Box(
            modifier = Modifier.background(backgroundColor), contentAlignment = Alignment.BottomEnd
        ) {
            Text(
                text = stringResource(resIdMessage),
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.align(
                    Alignment.Center
                ),
                color = textColor
            )
            if (shouldShowButton) {
                Button(onClick = onClick, modifier = Modifier.padding(all = 32.dp)) {
                    Text(
                        text = stringResource(resIdButton)
                    )
                }
            }
        }
    }
}
