package dev.ogabek.durak

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize
import com.ramcosta.composedestinations.DestinationsNavHost
import dagger.hilt.android.AndroidEntryPoint
import dev.ogabek.durak.screens.MainScreen
import dev.ogabek.durak.screens.NavGraphs
import dev.ogabek.durak.screens.PlayScreen
import dev.ogabek.durak.ui.theme.DurakTheme
import dev.ogabek.durak.utils.random
import kotlin.random.Random

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Firebase.initialize(this)

        setContent {
            DurakTheme {
                DestinationsNavHost(navGraph = NavGraphs.root)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    DurakTheme {
        MainActivity()
    }
}