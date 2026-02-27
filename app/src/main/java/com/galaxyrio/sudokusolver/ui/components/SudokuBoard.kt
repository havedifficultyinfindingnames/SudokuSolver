package com.galaxyrio.sudokusolver.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.galaxyrio.sudokusolver.game.Sudoku
import com.galaxyrio.sudokusolver.game.Cell
import com.galaxyrio.sudokusolver.game.validator.SudokuValidator

@Composable
fun SudokuBoard(
    sudoku: Sudoku,
    onCellClick: (row: Int, col: Int) -> Unit,
    modifier: Modifier = Modifier,
    selectedRow: Int? = null,
    selectedCol: Int? = null,
    highlightNumber: Int? = null
) {
    Box(
        modifier = modifier
            .aspectRatio(1f)
            .border(2.dp, MaterialTheme.colorScheme.onSurface)
    ) {
        // Overlay for 3x3 grid lines
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
        ) {
            repeat(3) {
                Row(modifier = Modifier.weight(1f)) {
                    repeat(3) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .border(1.dp, MaterialTheme.colorScheme.onSurface)
                        )
                    }
                }
            }
        }

        // Content layer (on top to receive clicks)
        Column(modifier = Modifier.fillMaxWidth().aspectRatio(1f)) {
            repeat(9) { row ->
                Row(modifier = Modifier.weight(1f)) {
                    repeat(9) { col ->
                        val cell = sudoku.getCell(row, col)
                        val isSelected = (row == selectedRow && col == selectedCol)
                        val isError = !cell.isFixed && cell.value != 0 && SudokuValidator.checkContradiction(sudoku, row, col)
                        val isValueHighlighted = (highlightNumber != null && cell.value == highlightNumber)

                        val errorCandidates = if (cell.value == 0) {
                            cell.candidates.filter { candidate ->
                                SudokuValidator.checkContradiction(sudoku, row, col, candidate)
                            }.toSet()
                        } else {
                            emptySet()
                        }

                        SudokuCell(
                            value = if (cell.value == 0) null else cell.value,
                            candidates = if (cell.value == 0) cell.candidates else emptySet(),
                            errorCandidates = errorCandidates,
                            isFixed = cell.isFixed,
                            isSelected = isSelected,
                            isError = isError,
                            highlightNumber = highlightNumber,
                            isValueHighlighted = isValueHighlighted,
                            onClick = { onCellClick(row, col) },
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SudokuCell(
    value: Int?,
    candidates: Set<Int> = emptySet(),
    errorCandidates: Set<Int> = emptySet(),
    isFixed: Boolean,
    isSelected: Boolean,
    isError: Boolean = false,
    highlightNumber: Int? = null,
    isValueHighlighted: Boolean = false,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = when {
        isSelected -> MaterialTheme.colorScheme.primaryContainer
        isValueHighlighted -> MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f)
        else -> Color.Transparent
    }

    Box(
        modifier = modifier
            .clickable(onClick = onClick)
            .background(backgroundColor)
            .border(
                width = 0.5.dp,
                color = MaterialTheme.colorScheme.outlineVariant
            ),
        contentAlignment = Alignment.Center
    ) {
        if (value != null && value != 0) {
            val textColor = when {
                isError -> MaterialTheme.colorScheme.error
                isFixed -> MaterialTheme.colorScheme.onSurface
                else -> MaterialTheme.colorScheme.primary
            }

            Text(
                text = value.toString(),
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = if (isFixed) FontWeight.Bold else FontWeight.Normal,
                    fontSize = 30.sp
                ),
                color = textColor
            )
        } else if (candidates.isNotEmpty()) {
            Column(
                modifier = Modifier.fillMaxWidth().aspectRatio(1f).padding(1.dp),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                (0..2).forEach { rowOffset ->
                    Row(
                        modifier = Modifier.weight(1f).fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        (1..3).forEach { colOffset ->
                            val candidateNum = rowOffset * 3 + colOffset
                            val isCandidateHighlighted = (highlightNumber != null && candidateNum == highlightNumber)

                            Box(contentAlignment = Alignment.Center, modifier = Modifier.weight(1f)) {
                                if (candidates.contains(candidateNum)) {
                                    val candidateColor = when {
                                        errorCandidates.contains(candidateNum) -> MaterialTheme.colorScheme.error
                                        isCandidateHighlighted -> MaterialTheme.colorScheme.primary
                                        else -> MaterialTheme.colorScheme.secondary
                                    }
                                    val fontWeight = if (isCandidateHighlighted) FontWeight.Bold else FontWeight.Normal

                                    Text(
                                        text = candidateNum.toString(),
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            fontSize = 10.sp,
                                            lineHeight = 10.sp,
                                            fontWeight = fontWeight
                                        ),
                                        color = candidateColor
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

@Preview(showBackground = true)
@Composable
fun SudokuBoardPreview() {
    val sampleCells = List(81) { i ->
        Cell(
            value = if (i % 5 == 0) (i % 9) + 1 else 0,
            isFixed = (i % 5 == 0)
        )
    }
    val sampleSudoku = Sudoku(sampleCells)

    SudokuBoard(
        sudoku = sampleSudoku,
        onCellClick = { _, _ -> },
        modifier = Modifier.padding(16.dp)
    )
}
