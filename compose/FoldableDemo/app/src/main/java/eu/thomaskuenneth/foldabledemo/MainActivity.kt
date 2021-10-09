package eu.thomaskuenneth.foldabledemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
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
    windowLayoutInfo?.displayFeatures?.forEach { displayFeature ->
        (displayFeature as FoldingFeature).run {
        }
    }
    Surface(modifier = Modifier.fillMaxSize()) {

    }
}