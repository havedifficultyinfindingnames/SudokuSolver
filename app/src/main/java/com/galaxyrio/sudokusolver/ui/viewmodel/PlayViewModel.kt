package com.galaxyrio.sudokusolver.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.galaxyrio.sudokusolver.database.AppDatabase
import com.galaxyrio.sudokusolver.database.SudokuEntity
import com.galaxyrio.sudokusolver.ui.screen.Difficulty
import com.galaxyrio.sudokusolver.ui.screen.SavedGame
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PlayViewModel(application: Application) : AndroidViewModel(application) {
    private val sudokuDao = AppDatabase.getDatabase(application).sudokuDao()

    private val _savedGames = MutableStateFlow<List<SavedGame>>(emptyList())
    val savedGames: StateFlow<List<SavedGame>> = _savedGames.asStateFlow()

    init {
        loadSavedGames()
    }

    fun loadSavedGames() {
        viewModelScope.launch {
            val entities = sudokuDao.getAllGames()
            val games = entities.map { entity ->
                SavedGame(
                    id = entity.id.toString(),
                    date = formatLastPlayed(entity.lastPlayed),
                    difficulty = entity.difficulty,
                    completionPercentage = calculateCompletion(entity)
                )
            }
            _savedGames.value = games
        }
    }

    private fun formatLastPlayed(timestamp: Long): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }

    private fun calculateCompletion(entity: SudokuEntity): Int {
        val cells = entity.sudoku.cells
        val solvedCount = cells.count { it.value != 0 }
        return (solvedCount * 100) / 81
    }
}
