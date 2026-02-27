package com.galaxyrio.sudokusolver.ui.screen.play

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.galaxyrio.sudokusolver.game.Sudoku
import com.galaxyrio.sudokusolver.game.generator.SudokuGenerator
import com.galaxyrio.sudokusolver.ui.components.GameToolbar
import com.galaxyrio.sudokusolver.ui.components.GameTopBar
import com.galaxyrio.sudokusolver.ui.components.NumberPad
import com.galaxyrio.sudokusolver.ui.components.SudokuBoard
import com.galaxyrio.sudokusolver.ui.screen.Difficulty

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun SudokuGameScreen(
    difficulty: Difficulty,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler(onBack = onBack)

    // Initialize the board using the generator
    // ...existing code...
    var sudoku by remember(difficulty) {
        val clues = when (difficulty) {
            Difficulty.EASY -> 40
            Difficulty.MEDIUM -> 30
            Difficulty.HARD -> 24
        }
        mutableStateOf(SudokuGenerator().generate(clues))
    }

    var selectedRow by remember { mutableStateOf<Int?>(null) }
    var selectedCol by remember { mutableStateOf<Int?>(null) }
    var selectedNumber by remember { mutableStateOf<Int?>(null) }
    var isNoteMode by remember { mutableStateOf(false) }

    // History for Undo
    val history = remember { mutableListOf<Sudoku>() }

    // Hint state
    var showHint by remember { mutableStateOf(false) }

    fun updateSudoku(newSudoku: Sudoku) {
        history.add(sudoku)
        sudoku = newSudoku
    }

    fun undo() {
        if (history.isNotEmpty()) {
            sudoku = history.removeAt(history.lastIndex)
        }
    }

    // ...existing code...
    // Interaction source for the background click to avoid ripple effect if desired
    val interactionSource = remember { MutableInteractionSource() }

    BoxWithConstraints(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        val screenHeight = maxHeight
        val screenWidth = maxWidth

        val topBarHeight = screenHeight / 7
        val boardHeight = screenWidth

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // 1. Top Bar Area
            GameTopBar(
                title = "Sudoku - ${difficulty.name}",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(topBarHeight)
                    .padding(top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding())
            )

            // 2. Board Area
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(boardHeight)
                    .padding(16.dp), // Check if padding is desired inside the "Board Width" constraint or outside?
                    // User said "Board area height is screen width". Usually this implies square aspect ratio.
                    // If we add padding, the internal board might be smaller.
                    // Let's keep a small padding for aesthetics but ensure the container is consistent.
                contentAlignment = Alignment.Center
            ) {
                 SudokuBoard(
                    sudoku = sudoku,
                    onCellClick = { row, col ->
                        selectedRow = row
                        selectedCol = col

                        // If a number is selected in the pad, try to fill it
                        val currentCell = sudoku.getCell(row, col)
                        if (!currentCell.isFixed && selectedNumber != null) {
                            if (isNoteMode) {
                                if (currentCell.value == 0) {
                                    updateSudoku(sudoku.toggleCandidate(row, col, selectedNumber!!))
                                }
                            } else {
                                val newValue = selectedNumber!!
                                // Only update if value is changing
                                if (currentCell.value != newValue) {
                                    updateSudoku(sudoku.setCell(row, col, newValue))
                                }
                            }
                        }
                    },
                    selectedRow = selectedRow,
                    selectedCol = selectedCol,
                    highlightNumber = selectedNumber,
                    modifier = Modifier.fillMaxSize()
                )
            }

            // Remaining space for Number Pad and Tool Bar
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) {
                         // Deselect cell when clicking empty space
                         selectedRow = null
                         selectedCol = null
                    },
                verticalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // 3. Middle Area: Number Pad OR Hint Panel
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f), // Take available space
                    contentAlignment = Alignment.Center
                ) {
                    if (showHint) {
                         com.galaxyrio.sudokusolver.ui.components.GameHintPanel(
                            onBackClick = { showHint = false },
                            onApplyClick = { /* Apply Hint */ },
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                         NumberPad(
                            selectedNumber = selectedNumber,
                            onNumberClick = { number ->
                                // Logic update: If a cell is selected (Cell First Mode), fill it and Clear selection
                                if (selectedRow != null && selectedCol != null) {
                                    val row = selectedRow!!
                                    val col = selectedCol!!
                                    val currentCell = sudoku.getCell(row, col)

                                    if (!currentCell.isFixed) {
                                        if (isNoteMode) {
                                            if (currentCell.value == 0) {
                                                updateSudoku(sudoku.toggleCandidate(row, col, number))
                                            }
                                        } else {
                                            val newValue = number
                                            if (currentCell.value != newValue) {
                                                updateSudoku(sudoku.setCell(row, col, newValue))
                                            }
                                        }
                                    }
                                    // Cell First detected: clear the number selection so user isn't stuck in "fill mode" for this number
                                    selectedNumber = null
                                } else {
                                    // Digit First Mode: Toggle number selection
                                    selectedNumber = if (selectedNumber == number) null else number
                                }
                            }
                        )
                    }
                }

                // 4. Tool Bar Area (Always visible)
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    GameToolbar(
                        isNoteMode = isNoteMode,
                        isHintActive = showHint,
                        onUndoClick = { undo() },
                        onDeleteClick = {
                            if (selectedRow != null && selectedCol != null) {
                                val row = selectedRow!!
                                val col = selectedCol!!
                                val currentCell = sudoku.getCell(row, col)

                                if (!currentCell.isFixed) {
                                    if (currentCell.value != 0 || currentCell.candidates.isNotEmpty()) {
                                        updateSudoku(sudoku.setCell(row, col, 0))
                                    }
                                }
                            }
                        },
                        onNoteModeClick = { isNoteMode = !isNoteMode },
                        onAutoCandidatesClick = {
                            updateSudoku(
                                com.galaxyrio.sudokusolver.game.generator.CandidateCalculator.calculateAllCandidates(sudoku)
                            )
                        },
                        onHintClick = { showHint = !showHint }
                    )

                    // Bottom spacing + Gesture inset
                    val bottomPadding = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
                    Spacer(modifier = Modifier.height(16.dp + bottomPadding))
                }
            }

        }
    }
}
