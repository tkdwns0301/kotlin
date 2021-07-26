package com.example.calculator.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.calculator.model.History

@Dao
interface HistoryDao {
    @Query("SELECT * FROM history")
    fun getAll(): List<History>

    @Insert
    fun insertHistory(history: History)

    @Query("DELETE FROM history")
    fun deleteAll()

    //하나만 삭제하고 싶을 때
    @Delete
    fun delete(history: History)

    //조건에 부합하는 아이들만 지우고 싶을 때
    @Query("SELECT * FROM history WHERE result LIKE :result LIMIT 1")
    fun findByResult(result: String): List<History>

}