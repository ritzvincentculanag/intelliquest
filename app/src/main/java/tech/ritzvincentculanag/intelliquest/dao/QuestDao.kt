package tech.ritzvincentculanag.intelliquest.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import tech.ritzvincentculanag.intelliquest.model.Quest

@Dao
interface QuestDao {

    @Insert
    suspend fun insert(quest: Quest)

    @Update
    suspend fun update(quest: Quest)

    @Delete
    suspend fun delete(quest: Quest)

    @Query("SELECT * FROM Quests WHERE isPublic = 1")
    fun getQuests(): Flow<List<Quest>>

}