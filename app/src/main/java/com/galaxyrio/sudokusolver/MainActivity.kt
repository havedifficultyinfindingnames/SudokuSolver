package com.galaxyrio.sudokusolver

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import com.galaxyrio.sudokusolver.ui.theme.SudokuSolverTheme
import com.galaxyrio.sudokusolver.ui.screen.InfoScreen
import com.galaxyrio.sudokusolver.ui.screen.PlayMenuScreen
import com.galaxyrio.sudokusolver.ui.screen.play.SudokuGameScreen
import com.galaxyrio.sudokusolver.ui.screen.SettingsScreen
import com.galaxyrio.sudokusolver.ui.screen.Difficulty

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SudokuSolverTheme {
                SudokuSolverApp()
            }
        }
    }
}

@PreviewScreenSizes
@Composable
fun SudokuSolverApp() {
    var currentDestination by rememberSaveable { mutableStateOf(AppDestinations.PLAY) }
    var isPlaying by rememberSaveable { mutableStateOf(false) }
    var gameDifficulty by rememberSaveable { mutableStateOf(Difficulty.MEDIUM) }
    var selectedGameId: Long? by rememberSaveable { mutableStateOf(null) }

    val context = LocalContext.current

    if (isPlaying) {
        SudokuGameScreen(
            difficulty = gameDifficulty,
            gameId = selectedGameId,
            onBack = { isPlaying = false },
            modifier = Modifier.fillMaxSize()
        )
    } else {
        NavigationSuiteScaffold(
            navigationSuiteItems = {
                AppDestinations.entries.forEach {
                    item(
                        icon = {
                            Icon(
                                it.icon,
                                contentDescription = it.label
                            )
                        },
                        label = { Text(it.label) },
                        selected = it == currentDestination,
                        onClick = { currentDestination = it }
                    )
                }
            }
        ) {
            val modifier = Modifier.fillMaxSize()
            when (currentDestination) {
                AppDestinations.INFO -> InfoScreen(modifier)
                AppDestinations.PLAY -> PlayMenuScreen(
                    modifier = modifier,
                    initialDifficulty = gameDifficulty,
                    onStartGame = { selectedDifficulty ->
                        gameDifficulty = selectedDifficulty
                        selectedGameId = null // New game
                        isPlaying = true
                    },
                    onContinueGame = { gameId ->
                        // gameId string needs to be parsed to Long
                        selectedGameId = gameId.toLongOrNull()
                        isPlaying = true
                    }
                )
                AppDestinations.SETTINGS -> SettingsScreen(modifier)
            }
        }
    }
}

enum class AppDestinations(
    val label: String,
    val icon: ImageVector,
) {
    INFO("Info", Icons.Default.Info),
    PLAY("Play", Icons.Default.SportsEsports),
    SETTINGS("Setting", Icons.Default.Settings),
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SudokuSolverTheme {
        Greeting("Android")
    }
}