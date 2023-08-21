package tech.ritzvincentculanag.intelliquest.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import tech.ritzvincentculanag.intelliquest.model.Quest
import tech.ritzvincentculanag.intelliquest.model.QuestType

@Dao
interface QuestDao {

    @Insert
    suspend fun insert(quest: Quest)

    @Update
    suspend fun update(quest: Quest)

    @Delete
    suspend fun delete(quest: Quest)

    @Query("SELECT * FROM Quests")
    suspend fun getAllQuest(): Flow<List<Quest>>

    @Query("SELECT * FROM Quests WHERE type = :type")
    suspend fun getAllQuestByType(type: QuestType): Flow<List<Quest>>

}