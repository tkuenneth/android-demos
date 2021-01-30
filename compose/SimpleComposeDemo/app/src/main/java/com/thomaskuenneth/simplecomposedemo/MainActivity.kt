package com.thomaskuenneth.simplecomposedemo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.InspectableValue
import androidx.compose.ui.platform.isDebugInspectorInfoEnabled
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.thomaskuenneth.simplecomposedemo.ui.theme.SimpleComposeDemoTheme

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Content()
        }
    }
}

@Composable
@Preview
fun Content() {
    SimpleComposeDemoTheme {
        val text = remember { mutableStateOf("Hello Compose") }
        val selected = remember { mutableStateOf(true) }
        Surface(color = MaterialTheme.colors.background) {
            Column(modifier = Modifier.fillMaxSize()) {
                EditableCheckbox(text, selected)
                TriStateDemo()
                isDebugInspectorInfoEnabled = true
                val myModifier = Modifier.border(
                    width = 12.dp,
                    brush = SolidColor(Color.Green),
                    shape = RoundedCornerShape(32f)
                )
                val inspectable = myModifier as InspectableValue
                inspectable.inspectableElements.forEach {
                    println("${it.name} = ${it.value}")
                }
                Text(
                    modifier = myModifier
                        .padding(16.dp),
                    text = "Hello border"
                )
            }
        }
    }
}

@Composable
fun TriStateDemo() {
    val state = remember { mutableStateOf(ToggleableState.Indeterminate) }
    TriStateCheckbox(state = state.value,
        modifier = Modifier.preferredSize(64.dp),
        onClick = {
            state.value = when (state.value) {
                ToggleableState.On -> ToggleableState.Off
                ToggleableState.Off -> ToggleableState.Indeterminate
                ToggleableState.Indeterminate -> ToggleableState.On
            }
        })
}

@Composable
fun EditableCheckbox(
    text: MutableState<String>,
    checked: MutableState<Boolean>
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = checked.value, onCheckedChange = {
                checked.value = it
            })
        TextField(
            value = text.value,
            backgroundColor = Color.Transparent,
            singleLine = true,
            modifier = Modifier
                .padding(8.dp)
                .weight(1.0f),
            onValueChange = {
                text.value = it
            },
        )
    }
}
