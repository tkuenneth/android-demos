package com.thomaskuenneth.palettedemo

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.palette.graphics.Palette
import com.thomaskuenneth.palettedemo.ui.theme.PaletteDemoTheme
import kotlinx.coroutines.launch

private const val REQUEST_GALLERY = 0x2908

class PaletteDemoViewModel : ViewModel() {

    private val _source: MutableLiveData<ImageDecoder.Source> =
        MutableLiveData<ImageDecoder.Source>()

    val source: LiveData<ImageDecoder.Source> = _source

    lateinit var bitmap: Bitmap
    lateinit var palette: Palette

    fun setSource(source: ImageDecoder.Source) {
        bitmap = ImageDecoder.decodeBitmap(source).asShared()
        palette = Palette.Builder(bitmap).generate()
        _source.value = source
    }
}

class PaletteDemoActivity : ComponentActivity() {

    private lateinit var viewModel: PaletteDemoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(PaletteDemoViewModel::class.java)
        setContent {
            PaletteDemoTheme {
                Surface(color = MaterialTheme.colors.background) {
                    PaletteDemo(
                        onClick = { showGallery() }
                    )
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        when (requestCode) {
            REQUEST_GALLERY -> {
                if (resultCode == RESULT_OK) {
                    intent?.let {
                        it.data?.let { uri ->
                            lifecycleScope.launch {
                                viewModel.setSource(
                                    ImageDecoder.createSource(
                                        contentResolver,
                                        uri
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    private fun showGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        val mimeTypes =
            arrayOf("image/jpeg", "image/png")
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
        startActivityForResult(intent, REQUEST_GALLERY)
    }
}

@Composable
fun PaletteDemo(
    viewModel: PaletteDemoViewModel = viewModel(),
    onClick: () -> Unit
) {
    val source = viewModel.source.observeAsState()
    Scaffold(topBar = {
        TopAppBar(title = { Text(stringResource(id = R.string.app_name)) })
    },
        floatingActionButton = {
            FloatingActionButton(onClick = onClick) {
                Icon(
                    Icons.Default.Search,
                    contentDescription = stringResource(id = R.string.select)
                )
            }
        }
    ) {
        source.value?.let {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    bitmap = viewModel.bitmap.asImageBitmap(),
                    contentDescription = null,
                    alignment = Alignment.Center
                )
                viewModel.palette.swatches.forEach {
                    Box(
                        modifier = Modifier
                            .padding(top = 8.dp)
                            .fillMaxWidth()
                            .height(32.dp)
                            .clip(RectangleShape)
                            .background(Color(it.rgb))
                    )
                }
            }
        }
    }
}
