package com.galaxyrio.sudokusolver.game

data class Cell(
    val value: Int = 0,
    val candidates: Set<Int> = (1..9).toSet(),
    val isFixed: Boolean = false
) {
    fun isSolved(): Boolean = value != 0

    override fun toString(): String {
        return if (value == 0) "." else value.toString()
    }
}

class Sudoku(val cells: List<Cell>) {

    constructor() : this(List(81) { Cell() })

    fun getCell(row: Int, col: Int): Cell {
        return cells[row * 9 + col]
    }

    fun setCell(row: Int, col: Int, value: Int, isFixed: Boolean = false): Sudoku {
        val index = row * 9 + col
        val newCells = cells.toMutableList()

        // If value is set (not 0), we clear candidates for this cell.
        // If value is 0 (clearing), we just update the value (candidates are effectively reset/ignored for now,
        // though typically clearing a number might restore candidates, but here we start clean).
        newCells[index] = Cell(value, emptySet(), isFixed)

        // Automatic Candidate Removal:
        // If we are setting a number (value != 0), remove this number from candidates in peers.
        if (value != 0) {
            // Row
            for (c in 0 until 9) {
                if (c != col) {
                    val pIndex = row * 9 + c
                    val pCell = newCells[pIndex]
                    if (pCell.value == 0 && pCell.candidates.contains(value)) {
                        newCells[pIndex] = pCell.copy(candidates = pCell.candidates - value)
                    }
                }
            }
            // Column
            for (r in 0 until 9) {
                if (r != row) {
                    val pIndex = r * 9 + col
                    val pCell = newCells[pIndex]
                    if (pCell.value == 0 && pCell.candidates.contains(value)) {
                        newCells[pIndex] = pCell.copy(candidates = pCell.candidates - value)
                    }
                }
            }
            // Box
            val boxStartRow = (row / 3) * 3
            val boxStartCol = (col / 3) * 3
            for (r in boxStartRow until boxStartRow + 3) {
                for (c in boxStartCol until boxStartCol + 3) {
                    if (r != row || c != col) {
                        val pIndex = r * 9 + c
                        val pCell = newCells[pIndex]
                        if (pCell.value == 0 && pCell.candidates.contains(value)) {
                            newCells[pIndex] = pCell.copy(candidates = pCell.candidates - value)
                        }
                    }
                }
            }
        }

        return Sudoku(newCells)
    }

    fun toggleCandidate(row: Int, col: Int, candidate: Int): Sudoku {
        val index = row * 9 + col
        val currentCell = cells[index]
        if (currentCell.value != 0 || currentCell.isFixed) return this // Cannot add notes to filled cells

        val newCandidates = if (currentCell.candidates.contains(candidate)) {
            currentCell.candidates - candidate
        } else {
            currentCell.candidates + candidate
        }

        val newCells = cells.toMutableList()
        newCells[index] = currentCell.copy(candidates = newCandidates)
        return Sudoku(newCells)
    }

    fun copy(): Sudoku {
        return Sudoku(cells)
    }

    override fun toString(): String {
        val sb = StringBuilder()
        for (r in 0 until 9) {
            for (c in 0 until 9) {
                sb.append(getCell(r, c).toString())
                if (c == 2 || c == 5) sb.append(" ")
            }
            sb.append("\n")
            if (r == 2 || r == 5) sb.append("\n")
        }
        return sb.toString()
    }

    fun toGridString(): String {
        return cells.joinToString("") { if (it.value == 0) "." else it.value.toString() }
    }

    companion object {
        fun fromGridString(str: String): Sudoku {
            val cells = str.map {
                if (it.isDigit() && it != '0') Cell(it.digitToInt(), isFixed = true)
                else Cell()
            }
            return Sudoku(cells)
        }
    }
}
