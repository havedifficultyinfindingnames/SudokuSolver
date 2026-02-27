package com.galaxyrio.sudokusolver.game.validator

import com.galaxyrio.sudokusolver.game.Sudoku

object SudokuValidator {

    /**
     * Checks if placing a value at the given row and column would cause any conflict
     * with other cells in the same row, column, or 3x3 box.
     *
     * @param sudoku The Sudoku board state.
     * @param row The row index (0-8).
     * @param col The column index (0-8).
     * @param value The value to check (1-9).
     * @return True if there is a conflict (invalid move), False if valid.
     */
    fun checkContradiction(sudoku: Sudoku, row: Int, col: Int, value: Int): Boolean {
        // If the value is 0 (clearing a cell), it cannot cause a conflict
        if (value == 0) return false

        // Check Row
        for (c in 0 until 9) {
            if (c != col) {
                if (sudoku.getCell(row, c).value == value) {
                    return true
                }
            }
        }

        // Check Column
        for (r in 0 until 9) {
            if (r != row) {
                if (sudoku.getCell(r, col).value == value) {
                    return true
                }
            }
        }

        // Check 3x3 Box
        val boxStartRow = (row / 3) * 3
        val boxStartCol = (col / 3) * 3

        for (r in boxStartRow until boxStartRow + 3) {
            for (c in boxStartCol until boxStartCol + 3) {
                if (r != row || c != col) {
                    if (sudoku.getCell(r, c).value == value) {
                        return true
                    }
                }
            }
        }

        return false
    }

    /**
     * Checks if the value at the given row and column causes any conflict
     * with other cells in the same row, column, or 3x3 box.

     *
     * @param sudoku The Sudoku board state.
     * @param row The row index (0-8).
     * @param col The column index (0-8).
     * @return True if there is a conflict (invalid move), False if valid.
     */
    fun checkContradiction(sudoku: Sudoku, row: Int, col: Int): Boolean {
        val cell = sudoku.getCell(row, col)
        val value = cell.value

        // If the cell is empty, it cannot cause a conflict
        if (value == 0) return false

        // Check Row
        for (c in 0 until 9) {
            if (c != col) {
                if (sudoku.getCell(row, c).value == value) {
                    return true
                }
            }
        }

        // Check Column
        for (r in 0 until 9) {
            if (r != row) {
                if (sudoku.getCell(r, col).value == value) {
                    return true
                }
            }
        }

        // Check 3x3 Box
        val boxStartRow = (row / 3) * 3
        val boxStartCol = (col / 3) * 3

        for (r in boxStartRow until boxStartRow + 3) {
            for (c in boxStartCol until boxStartCol + 3) {
                if (r != row || c != col) {
                    if (sudoku.getCell(r, c).value == value) {
                        return true
                    }
                }
            }
        }

        return false
    }

    /**
     * Checks if the entire board is valid (no contradictions).
     */
    fun isBoardValid(sudoku: Sudoku): Boolean {
        for (r in 0 until 9) {
            for (c in 0 until 9) {
                if (checkContradiction(sudoku, r, c)) {
                    return false
                }
            }
        }
        return true
    }
}

