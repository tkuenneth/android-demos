package eu.thomaskuenneth.hingedemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import androidx.window.layout.*
import eu.thomaskuenneth.hingedemo.ui.theme.HingeDemoTheme


data class HingeDef(
    val hasGap: Boolean,
    val sizeLeft: Dp,
    val sizeRight: Dp,
    val widthGap: Dp
)

class HingeDemoActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launchWhenResumed {
            setContent {
                val layoutInfo by WindowInfoTracker.getOrCreate(this@HingeDemoActivity)
                    .windowLayoutInfo(this@HingeDemoActivity).collectAsState(
                    initial = null
                )
                HingeDemoTheme {
                    HingeDemo(
                        layoutInfo,
                        WindowMetricsCalculator.getOrCreate()
                            .computeCurrentWindowMetrics(this@HingeDemoActivity)
                    )
                }
            }
        }
    }
}

@Composable
fun HingeDemo(
    layoutInfo: WindowLayoutInfo?,
    windowMetrics: WindowMetrics
) {
    var hasGap = false
    var sizeLeft = 0
    var sizeRight = 0
    var widthGap = 0
    layoutInfo?.displayFeatures?.forEach { displayFeature ->
        (displayFeature as FoldingFeature).run {
            hasGap = occlusionType == FoldingFeature.OcclusionType.FULL
                    && orientation == FoldingFeature.Orientation.VERTICAL
            sizeLeft = bounds.left
            sizeRight = windowMetrics.bounds.width() - bounds.right
            widthGap = bounds.width()
        }
    }
    val hingeDef = with(LocalDensity.current) {
        HingeDef(
            hasGap,
            sizeLeft.toDp(),
            sizeRight.toDp(),
            widthGap.toDp()
        )
    }
    Scaffold(topBar = {
        TopBar(hingeDef = hingeDef)
    },
        bottomBar = {
            BottomBar(hingeDef.hasGap)
        }) {
        Content(
            modifier = Modifier.padding(it),
            hingeDef
        )
    }
}

@Composable
fun TopBar(hingeDef: HingeDef) {
    TopAppBar(title = {
        if (hingeDef.hasGap) {
            Text(
                modifier = Modifier.width(hingeDef.sizeLeft - 32.dp),
                text = stringResource(id = R.string.title),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        } else {
            Text(
                text = stringResource(id = R.string.title),
            )
        }
    })
}

@Composable
fun BottomBar(hasGap: Boolean) {
    if (!hasGap)
        BottomNavigation {
            var selected by remember { mutableStateOf(0) }
            for (i in 0..4) BottomNavigationItem(selected = i == selected,
                onClick = { selected = i },
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_android_black_24dp),
                        contentDescription = null
                    )
                },
                label = {
                    Text(text = "#${i + 1}")
                })
        }
}

@Composable
fun Content(
    modifier: Modifier = Modifier,
    hingeDef: HingeDef
) {
    Row(modifier = Modifier.fillMaxSize()) {
        if (hingeDef.hasGap) {
            var selected by remember { mutableStateOf(0) }
            NavigationRail {
                for (i in 0..4) {
                    NavigationRailItem(selected = i == selected,
                        onClick = {
                            selected = i
                        },
                        icon = {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_android_black_24dp),
                                contentDescription = null
                            )
                        },
                        label = {
                            Text(text = "#${i + 1}")
                        })
                }
            }
        }
        Column(modifier = modifier.fillMaxSize()) {
            var selectedIndex by remember { mutableStateOf(0) }
            TabRow(selectedTabIndex = selectedIndex) {
                for (i in (0..2)) {
                    Tab(selected = i == selectedIndex,
                        text = {
                            if (hingeDef.hasGap)
                                Text(
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Left,
                                    text = "Tab #${i + 1}"
                                )
                            else
                                Text(
                                    text = "Tab #${i + 1}"
                                )
                        },
                        onClick = {
                            selectedIndex = i
                        })
                }
            }
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.TopStart
            ) {
                Text(
                    "#${selectedIndex + 1}",
                    style = MaterialTheme.typography.h1
                )
            }
        }
    }
}