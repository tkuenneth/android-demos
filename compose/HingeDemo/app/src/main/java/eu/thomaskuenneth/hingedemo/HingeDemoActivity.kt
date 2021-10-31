package eu.thomaskuenneth.hingedemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
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
import androidx.window.layout.FoldingFeature
import androidx.window.layout.WindowInfoRepository.Companion.windowInfoRepository
import androidx.window.layout.WindowLayoutInfo
import androidx.window.layout.WindowMetrics
import androidx.window.layout.WindowMetricsCalculator
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
        val repo = windowInfoRepository()
        setContent {
            val layoutInfo by repo.windowLayoutInfo.collectAsState(null)
            HingeDemoTheme {
                HingeDemo(
                    layoutInfo,
                    WindowMetricsCalculator.getOrCreate().computeCurrentWindowMetrics(this)
                )
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
    println(hingeDef)
    Scaffold(topBar = {
        TopBar(hingeDef = hingeDef)
    },
        bottomBar = {
            BottomBar()
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
fun BottomBar() {
    BottomNavigation() {
        for (i in 1..5) {
            BottomNavigationItem(selected = false,
                onClick = { /*TODO*/ },
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_android_black_24dp),
                        contentDescription = null
                    )
                },
                label = {
                    Text(text = "Navigation #$i")
                })
        }
    }
}

@Composable
fun Content(
    modifier: Modifier = Modifier,
    hingeDef: HingeDef
) {
    var selectedIndex by remember { mutableStateOf(0) }
    Column(modifier = modifier.fillMaxSize()) {
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