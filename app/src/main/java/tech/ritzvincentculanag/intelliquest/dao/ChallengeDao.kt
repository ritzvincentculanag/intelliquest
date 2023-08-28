package tech.ritzvincentculanag.intelliquest.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import tech.ritzvincentculanag.intelliquest.model.Challenge
import tech.ritzvincentculanag.intelliquest.model.relationship.ChallengeOptions

@Dao
interface ChallengeDao {

    @Update
    suspend fun update(challenge: Challenge)

    @Delete
    suspend fun delete(challenge: Challenge)

    @Insert
    fun insert(challenge: Challenge): Long

    @Query("SELECT * From Challenges")
    fun getChallengeOptions(): Flow<List<ChallengeOptions>>

}