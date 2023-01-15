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

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launchWhenResumed {
            setContent {
                val layoutInfo by WindowInfoTracker.getOrCreate(this@MainActivity)
                    .windowLayoutInfo(this@MainActivity).collectAsState(
                        initial = null
                    )
                val windowMetrics = WindowMetricsCalculator.getOrCreate()
                    .computeCurrentWindowMetrics(this@MainActivity)
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
    val hingeDef = createHingeDef(layoutInfo, windowMetrics)
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues = paddingValues)
    ) {
        if (hingeDef.hasHinge) {
            FoldableScreen(
                hingeDef = hingeDef
            )
        } else if (hingeDef.windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.EXPANDED) {
            LargeScreen(
                hingeDef = hingeDef
            )
        } else {
            SmartphoneScreen(
                hingeDef = hingeDef
            )
        }
    }
}

@Composable
fun SmartphoneScreen(hingeDef: HingeDef) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        YellowBox()
        PortraitOrLandscapeText(hingeDef = hingeDef)
    }
}

@Composable
fun LargeScreen(hingeDef: HingeDef) {
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
                YellowBox()
                GreenBox()
            }
        }
        PortraitOrLandscapeText(hingeDef)
    }
}

@Composable
fun FoldableScreen(hingeDef: HingeDef) {
    val hinge = @Composable {
        Spacer(
            modifier = Modifier
                .width(hingeDef.foldWidth)
                .height(hingeDef.foldHeight)
        )
    }
    val firstComposable = @Composable {
        RedBox()
    }
    val secondComposable = @Composable {
        GreenBox()
    }
    val container = @Composable {
        if (hingeDef.foldOrientation == FoldingFeature.Orientation.VERTICAL) {
            Row(modifier = Modifier.fillMaxSize()) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(hingeDef.widthLeftOrTop)
                ) {
                    firstComposable()
                }
                hinge()
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(hingeDef.widthRightOrBottom)
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
                        .height(hingeDef.heightRightOrBottom)
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
fun PortraitOrLandscapeText(hingeDef: HingeDef) {
    Text(
        text = stringResource(
            id = if (hingeDef.isPortrait)
                R.string.portrait
            else
                R.string.landscape
        ),
        style = MaterialTheme.typography.displayLarge,
        color = Color.Black
    )
}
