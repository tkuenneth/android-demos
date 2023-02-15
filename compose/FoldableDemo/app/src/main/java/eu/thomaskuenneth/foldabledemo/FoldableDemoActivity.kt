package eu.thomaskuenneth.foldabledemo

import android.os.Build
import android.os.Build.VERSION_CODES
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.window.core.layout.WindowWidthSizeClass
import androidx.window.layout.*
import kotlinx.coroutines.launch

class FoldableDemoActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                setContent {
                    val layoutInfo by WindowInfoTracker.getOrCreate(this@FoldableDemoActivity)
                        .windowLayoutInfo(this@FoldableDemoActivity).collectAsState(
                            initial = null
                        )
                    val windowMetrics = WindowMetricsCalculator.getOrCreate()
                        .computeCurrentWindowMetrics(this@FoldableDemoActivity)
                    // might become part of some UIState - kept here for simplicity
                    val foldDef = createFoldDef(layoutInfo, windowMetrics)
                    val hasTopBar =
                        foldDef.windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.COMPACT
                    val hasBottomBar =
                        foldDef.windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.COMPACT
                    val hasNavigationRail = !hasBottomBar
                    val index = rememberSaveable { mutableStateOf(0) }
                    MaterialTheme(
                        content = {
                            Scaffold(
                                topBar = { MyTopBar(hasTopBar = hasTopBar) },
                                bottomBar = {
                                    MyBottomBar(
                                        hasBottomBar = hasBottomBar,
                                        index = index
                                    )
                                }
                            ) { padding ->
                                Content(
                                    foldDef = foldDef,
                                    paddingValues = padding,
                                    hasNavigationRail = hasNavigationRail,
                                    index = index
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
fun MyTopBar(hasTopBar: Boolean) {
    if (hasTopBar)
        TopAppBar(title = {
            Text(stringResource(id = R.string.app_name))
        })
}

@Composable
fun MyBottomBar(hasBottomBar: Boolean, index: MutableState<Int>) {
    if (hasBottomBar)
        NavigationBar {
            for (i in 0..2)
                NavigationBarItem(selected = i == index.value,
                                  onClick = { index.value = i },
                                  icon = {
                                      Icon(
                                          painter = painterResource(id = R.drawable.ic_android_black_24dp),
                                          contentDescription = null
                                      )
                                  },
                                  label = {
                                      MyText(index = i)
                                  }
                )
        }
}

@Composable
fun Content(
    foldDef: FoldDef,
    paddingValues: PaddingValues,
    hasNavigationRail: Boolean,
    index: MutableState<Int>
) {
    Row(modifier = Modifier.fillMaxSize()) {
        if (hasNavigationRail)
            NavigationRail {
                for (i in 0..2)
                    NavigationRailItem(selected = i == index.value,
                                       onClick = {
                                           index.value = i
                                       },
                                       icon = {
                                           Icon(
                                               painter = painterResource(id = R.drawable.ic_android_black_24dp),
                                               contentDescription = null
                                           )
                                       },
                                       label = {
                                           MyText(index = i)
                                       })
            }
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
                        .weight(1.0F)
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

@Composable
fun MyText(index: Int, style: TextStyle = LocalTextStyle.current) {
    Text(
        text = "#${index + 1}",
        style = style
    )
}
