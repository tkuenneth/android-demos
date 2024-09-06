package de.thomaskuenneth.material_adaptive_demo

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.layout.PaneAdaptedValue
import androidx.compose.material3.adaptive.layout.ThreePaneScaffoldScope
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun HomePane() {
    val navigator = rememberListDetailPaneScaffoldNavigator<Int>()
    var currentIndex by remember { mutableIntStateOf(-1) }
    val onItemClicked: (Int) -> Unit = { id ->
        currentIndex = id
        navigator.navigateTo(
            pane = ListDetailPaneScaffoldRole.Detail, content = id
        )
    }
    BackHandler(navigator.canNavigateBack()) { navigator.navigateBack() }
    ListDetailPaneScaffold(directive = navigator.scaffoldDirective,
        value = navigator.scaffoldValue,
        listPane = { MyList(onItemClicked) },
        detailPane = {
            MyListDetail(currentIndex = currentIndex,
                listHidden = navigator.scaffoldValue.secondary == PaneAdaptedValue.Hidden,
                onBackClicked = { navigator.navigateBack() })
        })
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun ThreePaneScaffoldScope.MyList(onItemClicked: (Int) -> Unit) {
    val density = LocalDensity.current
    AnimatedPane {
        LazyColumn {
            with(density) {
                item {
                    Spacer(
                        modifier = Modifier.height(
                            WindowInsets.safeContent.getTop(density).toDp()
                        )
                    )
                }
                items(100) {
                    ListItem(headlineContent = { Text("${it + 1}") },
                        modifier = Modifier.clickable { onItemClicked(it) })
                }
                item {
                    Spacer(
                        modifier = Modifier.height(
                            WindowInsets.safeContent.getBottom(density).toDp()
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun MyListDetail(currentIndex: Int, listHidden: Boolean, onBackClicked: () -> Unit) {
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
