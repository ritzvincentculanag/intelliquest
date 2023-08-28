package tech.ritzvincentculanag.intelliquest.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import tech.ritzvincentculanag.intelliquest.model.Quest
import tech.ritzvincentculanag.intelliquest.model.relationship.QuestChallenge

@Dao
interface QuestDao {

    @Insert
    suspend fun insert(quest: Quest): Long

    @Update
    suspend fun update(quest: Quest): Int

    @Delete
    suspend fun delete(quest: Quest)

    @Query("SELECT * FROM Quests WHERE isPublic = 1")
    fun getQuests(): Flow<List<Quest>>

    @Transaction
    @Query("SELECT * FROM Quests")
    fun getQuestChallenges(): Flow<List<QuestChallenge>>

}