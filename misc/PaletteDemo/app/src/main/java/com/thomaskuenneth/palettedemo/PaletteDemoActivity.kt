package com.thomaskuenneth.palettedemo

import android.content.Intent
import android.graphics.ImageDecoder
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.thomaskuenneth.palettedemo.ui.theme.PaletteDemoTheme

private const val REQUEST_GALLERY = 0x2908

class PaletteDemoViewModel : ViewModel() {

    private val _source: MutableLiveData<ImageDecoder.Source> =
        MutableLiveData<ImageDecoder.Source>()

    val source: LiveData<ImageDecoder.Source> = _source

    fun setSource(source: ImageDecoder.Source) {
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
                            viewModel.setSource(
                                ImageDecoder.createSource(
                                    this.contentResolver,
                                    uri
                                )
                            )
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
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            source.value?.let {
                Image(
                    bitmap = ImageDecoder.decodeBitmap(it).asImageBitmap(),
                    contentDescription = null
                )
            }
        }
    }
}
