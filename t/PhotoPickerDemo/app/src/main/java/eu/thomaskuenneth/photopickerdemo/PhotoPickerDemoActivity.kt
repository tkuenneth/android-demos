package eu.thomaskuenneth.photopickerdemo

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest


class PhotoPickerDemoActivity : ComponentActivity() {

    private lateinit var uri: MutableState<Uri?>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val launcher = registerForActivityResult(
            ActivityResultContracts.PickVisualMedia()
        ) { result ->
            uri.value = result
        }
        setContent {
            MaterialTheme {
                uri = remember { mutableStateOf(null) }
                Content(
                    uri.value
                ) {
                    launcher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                }
            }
        }
    }
}

@Composable
fun Content(uri: Uri?, function: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Button(
            modifier = Modifier.padding(bottom = 16.dp),
            onClick = function
        ) {
            Text(text = stringResource(id = R.string.button))
        }
        Box(
            contentAlignment = Center,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            if (uri == null)
                Text(
                    text = stringResource(id = R.string.no_image),
                    style = MaterialTheme.typography.h4,
                    color = MaterialTheme.colors.primary
                )
            else
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(uri)
                        .crossfade(true)
                        .build(),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                )
        }
    }
}