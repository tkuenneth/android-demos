package de.thomaskuenneth.material_adaptive_demo

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.layout.PaneAdaptedValue
import androidx.compose.material3.adaptive.layout.ThreePaneScaffoldScope
import androidx.compose.material3.adaptive.layout.calculatePaneScaffoldDirectiveWithTwoPanesOnMediumWidth
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun HomePane() {
    val navigator = rememberListDetailPaneScaffoldNavigator<Int>(
        scaffoldDirective = calculatePaneScaffoldDirectiveWithTwoPanesOnMediumWidth(
            currentWindowAdaptiveInfo()
        )
    )
    var currentIndex by rememberSaveable { mutableIntStateOf(-1) }
    val onItemClicked: (Int) -> Unit = { id ->
        currentIndex = id
        navigator.navigateTo(
            pane = ListDetailPaneScaffoldRole.Detail, content = id
        )
    }
    val detailVisible = navigator.scaffoldValue.primary == PaneAdaptedValue.Expanded
    if (detailVisible && currentIndex == -1) currentIndex = 0
    BackHandler(navigator.canNavigateBack()) { navigator.navigateBack() }
    ListDetailPaneScaffold(directive = navigator.scaffoldDirective,
        value = navigator.scaffoldValue,
        listPane = {
            MyList(
                onItemClicked = onItemClicked,
                currentIndex = currentIndex,
                detailVisible = detailVisible
            )
        },
        detailPane = {
            MyListDetail(currentIndex = currentIndex,
                listHidden = navigator.scaffoldValue.secondary == PaneAdaptedValue.Hidden,
                onBackClicked = { navigator.navigateBack() })
        })
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun ThreePaneScaffoldScope.MyList(
    onItemClicked: (Int) -> Unit, currentIndex: Int, detailVisible: Boolean
) {
    AnimatedPane {
        LazyColumn(modifier = Modifier.safeContentPadding()) {
            items(20) {
                ListItem(
                    headlineContent = { Text("${it + 1}") },
                    modifier = Modifier.clickable { onItemClicked(it) },
                    colors = when (detailVisible) {
                        false -> ListItemDefaults.colors()
                        true -> if (currentIndex == it) {
                            ListItemDefaults.colors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                headlineColor = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        } else {
                            ListItemDefaults.colors()
                        }
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun ThreePaneScaffoldScope.MyListDetail(
    currentIndex: Int, listHidden: Boolean, onBackClicked: () -> Unit
) {
    AnimatedPane {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.background(MaterialTheme.colorScheme.secondaryContainer)
        ) {
            Text(
                "${currentIndex + 1}",
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
            if (listHidden) {
                IconButton(
                    onClick = onBackClicked,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .safeContentPadding()
                ) {
                    Icon(
                        Icons.AutoMirrored.Outlined.ArrowBack,
                        contentDescription = stringResource(R.string.back)
                    )
                }
            }
        }
    }
}
