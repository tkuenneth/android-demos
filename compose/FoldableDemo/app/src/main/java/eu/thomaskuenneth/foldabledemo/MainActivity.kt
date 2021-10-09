package eu.thomaskuenneth.foldabledemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.window.layout.FoldingFeature
import androidx.window.layout.WindowInfoRepository.Companion.windowInfoRepository
import androidx.window.layout.WindowLayoutInfo
import androidx.window.layout.WindowMetrics

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val r = windowInfoRepository()
        super.onCreate(savedInstanceState)
        setContent {
            val m by r.currentWindowMetrics.collectAsState(null)
            val i by r.windowLayoutInfo.collectAsState(null)
            Scaffold(modifier = Modifier.fillMaxSize(),
                topBar = {
                    TopAppBar(title = {
                        Text(stringResource(id = R.string.app_name))
                    })
                }) {
                Content(
                    m,
                    i
                )
            }
        }
    }
}

@Composable
fun Content(
    windowMetrics: WindowMetrics?,
    windowLayoutInfo: WindowLayoutInfo?
) {
    Surface(modifier = Modifier.fillMaxSize()) {
        windowMetrics?.let { wm ->
            val widthDp = with(LocalDensity.current) {
                wm.bounds.width().toDp()
            }
            if (widthDp < 600.dp) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Yellow)
                )
            } else {
                var hasGap = false
                var sizeLeft = 0
                var sizeRight = 0
                var widthGap = 0
                windowLayoutInfo?.displayFeatures?.forEach { displayFeature ->
                    (displayFeature as FoldingFeature).run {
                        hasGap = occlusionType == FoldingFeature.OcclusionType.FULL
                                && orientation == FoldingFeature.Orientation.VERTICAL
                        sizeLeft = bounds.left
                        sizeRight = wm.bounds.width() - bounds.right
                        widthGap = bounds.width()
                    }
                }
                Row(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    if (hasGap) {
                        val width = (wm.bounds.width() - widthGap).toFloat()
                        val weightLeft = sizeLeft.toFloat() / width
                        val weightRight = sizeRight.toFloat() / width
                        ColouredBoxWithWeight(weightLeft, Color.Red)
                        Spacer(
                            modifier = Modifier
                                .requiredWidth(with(LocalDensity.current) {
                                    widthGap.toDp()
                                })
                                .fillMaxHeight()
                        )
                        ColouredBoxWithWeight(weightRight, Color.Green)
                    } else {
                        ColouredBoxWithWeight(0.333F, Color.Red)
                        ColouredBoxWithWeight(0.333F, Color.Green)
                        ColouredBoxWithWeight(0.333F, Color.Blue)
                    }
                }
            }
        }
    }
}

@Composable
fun RowScope.ColouredBoxWithWeight(weight: Float, color: Color) {
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .weight(weight)
            .background(color)
            .border(1.dp, Color.White)
    )
}