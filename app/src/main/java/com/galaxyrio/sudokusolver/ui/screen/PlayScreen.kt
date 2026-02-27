package com.galaxyrio.sudokusolver.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


enum class Difficulty {
    EASY, MEDIUM, HARD
}

@Composable
fun PlayMenuScreen(
    modifier: Modifier = Modifier,
    initialDifficulty: Difficulty = Difficulty.MEDIUM,
    onStartGame: (Difficulty) -> Unit,
    onContinueGame: () -> Unit
) {
    var difficulty by rememberSaveable(initialDifficulty) { mutableStateOf(initialDifficulty) }
    // Mock state for existing game
    val hasSavedGame by rememberSaveable { mutableStateOf(true) }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Sudoku",
                style = MaterialTheme.typography.displayLarge,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            Text(
                text = "Difficulty",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Row(
                modifier = Modifier.padding(bottom = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Difficulty.entries.forEach { level ->
                    FilterChip(
                        selected = difficulty == level,
                        onClick = { difficulty = level },
                        label = { Text(level.name) }
                    )
                }
            }

            Button(
                onClick = { onStartGame(difficulty) },
                modifier = Modifier.fillMaxWidth(0.7f).padding(bottom = 12.dp)
            ) {
                Text("Start New Game")
            }

            if (hasSavedGame) {
                ElevatedButton(
                    onClick = onContinueGame,
                    modifier = Modifier.fillMaxWidth(0.7f)
                ) {
                    Text("Continue Game")
                }
            }
        }
    }
}
