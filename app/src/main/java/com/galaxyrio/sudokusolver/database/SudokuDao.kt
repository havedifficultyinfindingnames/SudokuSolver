package com.galaxyrio.sudokusolver.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.galaxyrio.sudokusolver.ui.screen.Difficulty

@Dao
interface SudokuDao {
    @Query("SELECT * FROM games WHERE difficulty = :difficulty")
    suspend fun getGame(difficulty: Difficulty): SudokuEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveGame(game: SudokuEntity)

    @Query("DELETE FROM games WHERE difficulty = :difficulty")
    suspend fun deleteGame(difficulty: Difficulty)
}

