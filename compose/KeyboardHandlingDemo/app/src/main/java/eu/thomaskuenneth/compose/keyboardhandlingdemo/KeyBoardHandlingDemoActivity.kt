package eu.thomaskuenneth.compose.keyboardhandlingdemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlin.math.pow

@ExperimentalComposeUiApi
class KeyboardHandlingDemoActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KeyboardHandlingDemo3()
        }
    }
}

@Composable
fun KeyboardHandlingDemo1() {
    var text by remember { mutableStateOf(TextFieldValue()) }
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        Box(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize()
                .background(color = MaterialTheme.colors.primary)
                .weight(1.0F),
            contentAlignment = Alignment.BottomCenter
        ) {
            Text(
                modifier = Modifier.padding(bottom = 16.dp),
                text = stringResource(id = R.string.app_name),
                color = MaterialTheme.colors.onPrimary,
                style = MaterialTheme.typography.h5
            )
        }
        TextField(modifier = Modifier.padding(bottom = 16.dp),
            value = text,
            onValueChange = {
                text = it
            })
    }
}

@Composable
fun KeyboardHandlingDemo2() {
    val states = remember {
        mutableStateListOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "10")
    }
    val listState = rememberLazyListState()
    val focusManager = LocalFocusManager.current
    val coroutineScope = rememberCoroutineScope()
    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        itemsIndexed(states) { i, _ ->
            OutlinedTextField(value = states[i],
                modifier = Modifier.padding(top = 16.dp),
                onValueChange = {
                    states[i] = it
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(
                    onNext = {
                        focusManager.moveFocus(FocusDirection.Down)
                        coroutineScope.launch {
                            listState.animateScrollToItem(i)
                        }
                    }
                ),
                label = {
                    Text("Text field ${i + 1}")
                })
        }
    }
}

@ExperimentalComposeUiApi
@Composable
fun KeyboardHandlingDemo3() {
    val kc = LocalSoftwareKeyboardController.current
    var text by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }
    val callback = {
        result = try {
            val num = text.toFloat()
            num.pow(2.0F).toString()
        } catch (ex: NumberFormatException) {
            ""
        }
        kc?.hide()
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row {
            TextField(modifier = Modifier
                .padding(bottom = 16.dp)
                .alignByBaseline(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        callback()
                    }
                ),
                value = text,
                onValueChange = {
                    text = it
                })
            Button(modifier = Modifier
                .padding(start = 8.dp)
                .alignByBaseline(),
                onClick = {
                    callback()
                }) {
                Text(stringResource(id = R.string.calc))
            }
        }
        Text(
            text = result,
            style = MaterialTheme.typography.h4
        )
    }
}