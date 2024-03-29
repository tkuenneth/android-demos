package eu.thomaskuenneth.compose.badgedemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle

@ExperimentalMaterial3Api
class BadgeDemoActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BadgeDemo()
        }
    }
}

@ExperimentalMaterial3Api
@Composable
fun BadgeDemo() {
    val index = rememberSaveable { mutableStateOf(0) }
    Scaffold(topBar = {
        SmallTopAppBar(title = {
            Text(text = stringResource(id = R.string.app_name))
        })
    },
        bottomBar = {
            BottomBar(index)
        }) {
        Content(index)
    }
}

@Composable
fun BottomBar(index: MutableState<Int>) {
    NavigationBar() {
        for (i in 0..2)
            NavigationBarItem(selected = i == index.value,
                onClick = { index.value = i },
                icon = {
                    if (i != 1)
                        Icon(
                            painter = painterResource(id = R.drawable.ic_baseline_android_24),
                            contentDescription = null
                        )
                    else {
                        BadgedBox(badge = {
                            Badge {
                                Text("New")
                            }
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_baseline_android_24),
                                contentDescription = null
                            )
                        }
                    }
                },
                label = {
                    MyText(index = i)
                }
            )
    }
}

@Composable
fun Content(index: MutableState<Int>) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.surface),
        contentAlignment = Alignment.Center
    ) {
        MyText(
            index = index.value,
            style = MaterialTheme.typography.headlineMedium
        )
    }
}

@Composable
fun MyText(index: Int, style: TextStyle = LocalTextStyle.current) {
    Text(
        text = "#${index + 1}",
        style = style
    )
}