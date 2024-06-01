package com.thomaskuenneth.palettedemo

import android.graphics.ImageDecoder
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.palette.graphics.Palette
import kotlinx.coroutines.launch

class PaletteDemoActivity : ComponentActivity() {

    private lateinit var viewModel: PaletteDemoViewModel
    private lateinit var launcher: ActivityResultLauncher<PickVisualMediaRequest>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[PaletteDemoViewModel::class.java]
        launcher =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia())
            {
                it?.let { uri ->
                    val source = ImageDecoder.createSource(
                        contentResolver,
                        uri
                    )
                    val bitmap = ImageDecoder.decodeBitmap(
                        source
                    ) { decoder, _, _ ->
                        decoder.allocator = ImageDecoder.ALLOCATOR_SOFTWARE
                        decoder.isMutableRequired = true
                    }
                    viewModel.setBitmap(bitmap)
                    lifecycleScope.launch {
                        viewModel.setPalette(
                            Palette.Builder(bitmap).generate()
                        )
                    }
                }
            }
        enableEdgeToEdge()
        setContent {
            MaterialTheme(colorScheme = defaultColorScheme()) {
                PaletteDemo(
                    onClick = { showGallery() }
                )
            }
        }
    }

    private fun showGallery() {
        launcher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaletteDemo(
    viewModel: PaletteDemoViewModel = viewModel(),
    onClick: () -> Unit
) {
    val bitmap = viewModel.bitmap.observeAsState()
    val palette = viewModel.palette.observeAsState()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(id = R.string.app_name)) },
                scrollBehavior = scrollBehavior
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onClick
            ) {
                Icon(
                    Icons.Default.Search,
                    contentDescription = stringResource(id = R.string.select)
                )
            }
        },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) {
        Column(
            modifier = Modifier
                .consumeWindowInsets(it)
                .verticalScroll(rememberScrollState())
                .padding(it),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            bitmap.value?.run {
                Image(
                    bitmap = asImageBitmap(),
                    contentDescription = null,
                    alignment = Alignment.Center
                )
            }
            palette.value?.run {
                swatches.forEach { swatch ->
                    Box(
                        modifier = Modifier
                            .padding(top = 8.dp)
                            .fillMaxWidth()
                            .height(32.dp)
                            .clip(RectangleShape)
                            .background(Color(swatch.rgb))
                    )
                }
            }
        }
    }
}
