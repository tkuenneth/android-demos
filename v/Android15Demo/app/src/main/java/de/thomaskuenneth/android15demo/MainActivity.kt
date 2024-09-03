package de.thomaskuenneth.android15demo

import android.app.ActivityManager
import android.os.Bundle
import android.view.WindowManager.SCREEN_RECORDING_STATE_VISIBLE
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import java.util.function.Consumer

class MainActivity : ComponentActivity() {

    private val flow: MutableStateFlow<Boolean> = MutableStateFlow(false)
    private val mCallback: Consumer<Int> =
        Consumer { state -> flow.update { state == SCREEN_RECORDING_STATE_VISIBLE } }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val am: ActivityManager = getSystemService(ActivityManager::class.java)
        val wasForceStopped = am.getHistoricalProcessStartReasons(0).any { it.wasForceStopped() }
        setContent {
            MaterialTheme {
                val screenRecordingActive by flow.collectAsState()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainScreen(
                        modifier = Modifier.padding(innerPadding),
                        wasForceStopped = wasForceStopped,
                        screenRecordingActive = screenRecordingActive
                    )
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val initialState = windowManager.addScreenRecordingCallback(mainExecutor, mCallback)
        mCallback.accept(initialState)
    }

    override fun onStop() {
        super.onStop()
        windowManager.removeScreenRecordingCallback(mCallback)
    }
}

@Composable
fun MainScreen(
    modifier: Modifier = Modifier, wasForceStopped: Boolean, screenRecordingActive: Boolean
) {
    Column(
        modifier = modifier
            .background(color = MaterialTheme.colorScheme.secondaryContainer)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (wasForceStopped) {
            Text(
                text = stringResource(R.string.was_force_stopped),
                style = MaterialTheme.typography.bodyLarge
            )
        }
        if (screenRecordingActive) {
            Text(
                text = stringResource(R.string.screen_recording_active),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}
