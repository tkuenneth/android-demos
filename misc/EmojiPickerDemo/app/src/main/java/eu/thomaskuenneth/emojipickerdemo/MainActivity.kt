package eu.thomaskuenneth.emojipickerdemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.emoji2.emojipicker.EmojiPickerView

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = MaterialTheme.colorScheme.background),
                contentAlignment = Alignment.Center
            ) {
                val initialMessage = stringResource(id = R.string.no_emoji_picked)
                var isOpen by remember { mutableStateOf(false) }
                var pickedEmoji by remember { mutableStateOf(initialMessage) }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Button(onClick = {
                        isOpen = !isOpen
                    }) {
                        Text(text = stringResource(id = R.string.open))
                    }
                    Text(
                        modifier = Modifier.padding(top = 32.dp),
                        style = MaterialTheme.typography.displayLarge,
                        text = pickedEmoji
                    )
                }
                if (isOpen) {
                    Surface(modifier = Modifier.fillMaxSize()) {
                        AndroidView(
                            factory = { context ->
                                val picker = EmojiPickerView(context = context)
                                picker.setOnEmojiPickedListener {
                                    pickedEmoji = it.emoji
                                    isOpen = false
                                }
                                picker
                            },
                        )
                    }
                }
            }
        }
    }
}
