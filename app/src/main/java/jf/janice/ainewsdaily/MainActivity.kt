package jf.janice.ainewsdaily

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.Modifier
import dagger.hilt.android.AndroidEntryPoint
import jf.janice.ainewsdaily.navigation.MainScreen
import jf.janice.ainewsdaily.ui.theme.AINewsDailyTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AINewsDailyTheme {
                MainScreen(modifier = Modifier)
            }
        }
    }
}
