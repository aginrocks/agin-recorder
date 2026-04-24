package rocks.agin.recorder

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import rocks.agin.recorder.ui.screens.ClapboardScreen
import rocks.agin.recorder.ui.theme.AginRecorderTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AginRecorderTheme {
                ClapboardScreen()
            }
        }
    }
}
