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

    private val _bitmap: MutableLiveData<Bitmap> =
        MutableLiveData<Bitmap>()

    val bitmap: LiveData<Bitmap>
        get() = _bitmap

    fun setBitmap(bitmap: Bitmap) {
        _bitmap.value = bitmap
    }

    private val _palette: MutableLiveData<Palette> =
        MutableLiveData<Palette>()

    val palette: LiveData<Palette>
        get() = _palette

    fun setPalette(palette: Palette) {
        _palette.value = palette
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
                            val source = ImageDecoder.createSource(
                                contentResolver,
                                uri
                            )
                            val bitmap = ImageDecoder.decodeBitmap(source).asShared()
                            viewModel.setBitmap(bitmap)
                            lifecycleScope.launch {
                                viewModel.setPalette(
                                    Palette.Builder(bitmap).generate()
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
    val bitmap = viewModel.bitmap.observeAsState()
    val palette = viewModel.palette.observeAsState()
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
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            bitmap.value?.run {
                Image(
                    bitmap = asImageBitmap(),
                    contentDescription = null,
                    alignment = Alignment.Center
                )
            }
            palette.value?.run {
                swatches.forEach {
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
