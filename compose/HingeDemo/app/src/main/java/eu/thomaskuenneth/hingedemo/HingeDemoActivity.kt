package eu.thomaskuenneth.hingedemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import eu.thomaskuenneth.hingedemo.ui.theme.HingeDemoTheme

class HingeDemoActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HingeDemoTheme {
                HingeDemo()
            }
        }
    }
}

@Composable
fun HingeDemo() {
    Scaffold(topBar = {
        TopBar()
    },
        bottomBar = {
            BottomBar()
        }) {
        Content(modifier = Modifier.padding(it))
    }
}

@Composable
fun TopBar() {
    TopAppBar(title = {
        Text(text = stringResource(id = R.string.title))
    })
}

@Composable
fun BottomBar() {
    BottomNavigation() {
        for (i in 1..5) {
            BottomNavigationItem(selected = false,
                onClick = { /*TODO*/ },
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_android_black_24dp),
                        contentDescription = null
                    )
                },
                label = {
                    Text(text = "Navigation #$i")
                })
        }
    }
}

@Composable
fun Content(modifier: Modifier = Modifier) {
    var selectedIndex by remember { mutableStateOf(0) }
    Column(modifier = modifier.fillMaxSize()) {
        TabRow(selectedTabIndex = selectedIndex) {
            for (i in (0..2)) {
                Tab(selected = i == selectedIndex,
                    text = {
                        Text(text = "Tab #${i + 1}")
                    },
                    onClick = {
                        selectedIndex = i
                    })
            }
        }
        Box(modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopStart) {
            Text("#${selectedIndex + 1}",
            style = MaterialTheme.typography.h1)
        }
    }
}