package eu.thomaskuenneth.foldabledemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import androidx.window.core.layout.WindowWidthSizeClass
import androidx.window.layout.*

class FoldableDemoActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launchWhenResumed {
            setContent {
                val layoutInfo by WindowInfoTracker.getOrCreate(this@FoldableDemoActivity)
                    .windowLayoutInfo(this@FoldableDemoActivity).collectAsState(
                        initial = null
                    )
                val windowMetrics = WindowMetricsCalculator.getOrCreate()
                    .computeCurrentWindowMetrics(this@FoldableDemoActivity)
                MaterialTheme(
                    content = {
                        Scaffold(
                            topBar = {
                                TopAppBar(title = {
                                    Text(stringResource(id = R.string.app_name))
                                })
                            }
                        ) { padding ->
                            Content(
                                layoutInfo = layoutInfo,
                                windowMetrics = windowMetrics,
                                paddingValues = padding
                            )
                        }
                    },
                    colorScheme = if (isSystemInDarkTheme())
                        darkColorScheme()
                    else
                        lightColorScheme()
                )
            }
        }
    }
}

@Composable
fun Content(
    layoutInfo: WindowLayoutInfo?,
    windowMetrics: WindowMetrics,
    paddingValues: PaddingValues
) {
    val foldDef = createFoldDef(layoutInfo, windowMetrics)
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues = paddingValues)
    ) {
        if (foldDef.hasFold) {
            FoldableScreen(
                foldDef = foldDef
            )
        } else if (foldDef.windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.EXPANDED) {
            LargeScreen(
                foldDef = foldDef
            )
        } else {
            SmartphoneScreen(
                foldDef = foldDef
            )
        }
    }
}

@Composable
fun SmartphoneScreen(foldDef: FoldDef) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        YellowBox()
        PortraitOrLandscapeText(foldDef = foldDef)
    }
}

@Composable
fun LargeScreen(foldDef: FoldDef) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
        ) {
            val localModifier = Modifier
                .fillMaxHeight()
                .weight(0.333F)
            Box(modifier = localModifier) {
                RedBox()
            }
            Box(modifier = localModifier) {
                YellowBox()
            }
            Box(modifier = localModifier) {
                GreenBox()
            }
        }
        PortraitOrLandscapeText(foldDef)
    }
}

@Composable
fun FoldableScreen(foldDef: FoldDef) {
    val hinge = @Composable {
        Spacer(
            modifier = Modifier
                .width(foldDef.foldWidth)
                .height(foldDef.foldHeight)
        )
    }
    val firstComposable = @Composable {
        RedBox()
    }
    val secondComposable = @Composable {
        GreenBox()
    }
    val container = @Composable {
        if (foldDef.foldOrientation == FoldingFeature.Orientation.VERTICAL) {
            Row(modifier = Modifier.fillMaxSize()) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(foldDef.widthLeftOrTop)
                ) {
                    firstComposable()
                }
                hinge()
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(foldDef.widthRightOrBottom)
                ) {
                    secondComposable()
                }
            }
        } else {
            Column(modifier = Modifier.fillMaxSize()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1.0F)
                ) {
                    firstComposable()
                }
                hinge()
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(foldDef.heightRightOrBottom)
                ) {
                    secondComposable()
                }
            }
        }
    }
    container()
}

@Composable
fun ColoredBox(modifier: Modifier, color: Color) {
    Box(
        modifier = modifier
            .background(color)
            .border(1.dp, Color.White)
    )
}

@Composable
fun RedBox() {
    ColoredBox(
        modifier = Modifier
            .fillMaxSize(),
        color = Color.Red
    )
}

@Composable
fun YellowBox() {
    ColoredBox(
        modifier = Modifier
            .fillMaxSize(),
        color = Color.Yellow
    )
}

@Composable
fun GreenBox() {
    ColoredBox(
        modifier = Modifier
            .fillMaxSize(),
        color = Color.Green
    )
}

@Composable
fun PortraitOrLandscapeText(foldDef: FoldDef) {
    Text(
        text = stringResource(
            id = if (foldDef.isPortrait)
                R.string.portrait
            else
                R.string.landscape
        ),
        style = MaterialTheme.typography.displayLarge,
        color = Color.Black
    )
}
