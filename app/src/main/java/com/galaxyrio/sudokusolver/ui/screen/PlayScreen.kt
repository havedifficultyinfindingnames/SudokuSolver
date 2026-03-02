package com.galaxyrio.sudokusolver.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.galaxyrio.sudokusolver.ui.viewmodel.PlayViewModel


enum class Difficulty {
    EASY, MEDIUM, HARD
}

data class SavedGame(
    val id: String,
    val date: String,
    val difficulty: Difficulty,
    val completionPercentage: Int
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayMenuScreen(
    modifier: Modifier = Modifier,
    initialDifficulty: Difficulty = Difficulty.MEDIUM,
    onStartGame: (Difficulty) -> Unit,
    onContinueGame: (String) -> Unit,
    viewModel: PlayViewModel = viewModel()
) {
    var difficulty by rememberSaveable(initialDifficulty) { mutableStateOf(initialDifficulty) }
    // Real state for existing game
    val savedGames by viewModel.savedGames.collectAsState()

    var isSavedGamesExpanded by rememberSaveable { mutableStateOf(false) }

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

            SingleChoiceSegmentedButtonRow(
                modifier = Modifier.padding(bottom = 24.dp)
            ) {
                Difficulty.entries.forEachIndexed { index, level ->
                    SegmentedButton(
                        selected = difficulty == level,
                        onClick = { difficulty = level },
                        shape = SegmentedButtonDefaults.itemShape(
                            index = index,
                            count = Difficulty.entries.size
                        ),
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

            if (savedGames.isNotEmpty()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .padding(top = 8.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        ListItem(
                            headlineContent = { Text("Continue Saved Game") },
                            trailingContent = {
                                Icon(
                                    imageVector = if (isSavedGamesExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                    contentDescription = if (isSavedGamesExpanded) "Collapse" else "Expand"
                                )
                            },
                            modifier = Modifier.clickable { isSavedGamesExpanded = !isSavedGamesExpanded }
                        )

                        if (isSavedGamesExpanded) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 8.dp)
                            ) {
                                savedGames.forEach { game ->
                                    ListItem(
                                        headlineContent = { Text("Game ${game.date}") },
                                        supportingContent = { Text("${game.difficulty} - ${game.completionPercentage}% Complete") },
                                        modifier = Modifier.clickable { onContinueGame(game.id) }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
