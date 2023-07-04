package eu.thomaskuenneth.foldabledemo

import android.os.Build
import android.os.Build.VERSION_CODES
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.window.core.ExperimentalWindowApi
import androidx.window.core.layout.WindowWidthSizeClass
import androidx.window.layout.FoldingFeature
import androidx.window.layout.WindowInfoTracker
import androidx.window.layout.WindowMetricsCalculator
import kotlinx.coroutines.launch

@OptIn(ExperimentalWindowApi::class)
class FoldableDemoActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                setContent {
                    //region Variables
                    val layoutInfo by WindowInfoTracker.getOrCreate(this@FoldableDemoActivity)
                        .windowLayoutInfo(this@FoldableDemoActivity).collectAsState(
                            initial = null
                        )
                    val windowMetrics = WindowMetricsCalculator.getOrCreate()
                        .computeCurrentWindowMetrics(this@FoldableDemoActivity)
                    val foldDef = createFoldDef(layoutInfo, windowMetrics)
                    val hasTopBar =
                        foldDef.windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.COMPACT
                    val hasBottomBar =
                        foldDef.windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.COMPACT
                    val hasNavigationRail = !hasBottomBar
                    var index by rememberSaveable { mutableStateOf(0) }
                    var offset by remember { mutableStateOf(0.dp) }
                    val localDensity = LocalDensity.current
                    //endregion
                    MaterialTheme(
                        content = {
                            Scaffold(
                                topBar = {
                                    FoldableDemoTopBar(hasTopBar = hasTopBar)
                                },
                                bottomBar = {
                                    Box(modifier = Modifier.onGloballyPositioned {
                                        val bottomOrRight = with(
                                            windowMetrics.getWindowInsets()
                                                .getInsets(WindowInsetsCompat.Type.navigationBars())
                                        ) {
                                            if (foldDef.foldOrientation == FoldingFeature.Orientation.HORIZONTAL)
                                                bottom
                                            else
                                                right
                                        }
                                        offset = with(localDensity) {
                                            (it.size.height + bottomOrRight).toDp()
                                        }
                                    }) {
                                        FoldableDemoBottomBar(
                                            hasBottomBar = hasBottomBar,
                                            index = index,
                                            onClick = { index = it }
                                        )
                                    }
                                }
                            ) { padding ->
                                FoldableDemoContent(
                                    //region Function parameters
                                    foldDef = foldDef,
                                    paddingValues = padding,
                                    hasNavigationRail = hasNavigationRail,
                                    index = index,
                                    onClick = { index = it },
                                    offset = offset
                                    //endregion
                                )
                            }
                        },
                        colorScheme = defaultColorScheme()
                    )
                }
            }
        }
    }
}

@Composable
fun defaultColorScheme() = with(isSystemInDarkTheme()) {
    val hasDynamicColor = Build.VERSION.SDK_INT >= VERSION_CODES.S
    val context = LocalContext.current
    when (this) {
        true -> if (hasDynamicColor) {
            dynamicDarkColorScheme(context)
        } else {
            darkColorScheme()
        }

        false -> if (hasDynamicColor) {
            dynamicLightColorScheme(context)
        } else {
            lightColorScheme()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoldableDemoTopBar(hasTopBar: Boolean) {
    if (hasTopBar)
        TopAppBar(title = {
            Text(stringResource(id = R.string.app_name))
        })
}

@Composable
fun FoldableDemoBottomBar(hasBottomBar: Boolean, index: Int, onClick: (Int) -> Unit) {
    if (hasBottomBar)
        NavigationBar {
            for (i in 0..2)
                NavigationBarItem(selected = i == index,
                    onClick = { onClick(i) },
                    icon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_android_black_24dp),
                            contentDescription = null
                        )
                    },
                    label = {
                        FoldableDemoText(index = i)
                    }
                )
        }
}

@Composable
fun FoldableDemoContent(
    foldDef: FoldDef,
    paddingValues: PaddingValues,
    hasNavigationRail: Boolean,
    index: Int,
    onClick: (Int) -> Unit,
    offset: Dp
) {
    Row(modifier = Modifier.fillMaxSize()) {
        FoldableDemoNavigationRail(
            hasNavigationRail = hasNavigationRail,
            index = index,
            onClick = onClick
        )
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = paddingValues)
        ) {
            if (foldDef.hasFold) {
                FoldableScreen(
                    foldDef = foldDef,
                    offset = offset
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
}

@Composable
fun FoldableDemoNavigationRail(
    hasNavigationRail: Boolean,
    index: Int,
    onClick: (Int) -> Unit
) {
    if (hasNavigationRail)
        NavigationRail {
            for (i in 0..2)
                NavigationRailItem(selected = i == index,
                    onClick = {
                        onClick(i)
                    },
                    icon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_android_black_24dp),
                            contentDescription = null
                        )
                    },
                    label = {
                        FoldableDemoText(index = i)
                    })
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
fun FoldableScreen(foldDef: FoldDef, offset: Dp) {
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
                        .weight(1.0F)
                ) {
                    firstComposable()
                }
                hinge()
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(foldDef.widthRightOrBottom - offset)
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
                        .height(foldDef.heightRightOrBottom - offset)
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

@Composable
fun FoldableDemoText(index: Int, style: TextStyle = LocalTextStyle.current) {
    Text(
        text = "#${index + 1}",
        style = style
    )
}
