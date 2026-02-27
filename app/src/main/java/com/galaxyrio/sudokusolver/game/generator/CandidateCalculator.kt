package com.galaxyrio.sudokusolver.game.generator

import com.galaxyrio.sudokusolver.game.Sudoku
import com.galaxyrio.sudokusolver.game.validator.SudokuValidator

object CandidateCalculator {

    /**
     * Calculates and fills valid candidates for all empty cells in the Sudoku board.
     * Existing candidates will be overwritten.
     *
     * @param sudoku The current Sudoku board state.
     * @return A new Sudoku board with updated candidates.
     */
    fun calculateAllCandidates(sudoku: Sudoku): Sudoku {
        val newCells = sudoku.cells.toMutableList()

        for (row in 0 until 9) {
            for (col in 0 until 9) {
                val index = row * 9 + col
                val cell = newCells[index]

                // Only calculate candidates for empty cells
                if (cell.value == 0) {
                    val validCandidates = mutableSetOf<Int>()
                    for (num in 1..9) {
                        // Check if placing 'num' at (row, col) is valid
                        if (!SudokuValidator.checkContradiction(sudoku, row, col, num)) {
                            validCandidates.add(num)
                        }
                    }
                    newCells[index] = cell.copy(candidates = validCandidates)
                }
            }
        }

        return Sudoku(newCells)
    }
}

