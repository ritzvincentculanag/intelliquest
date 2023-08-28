package tech.ritzvincentculanag.intelliquest.repository

import kotlinx.coroutines.flow.Flow
import tech.ritzvincentculanag.intelliquest.dao.ChallengeDao
import tech.ritzvincentculanag.intelliquest.model.Challenge
import tech.ritzvincentculanag.intelliquest.model.relationship.ChallengeOptions

class ChallengeRepository(private val challengeDao: ChallengeDao) {

    private val challengeOptions = challengeDao.getChallengeOptions()

    suspend fun insert(challenge: Challenge) {
        challengeDao.insert(challenge)
    }

    suspend fun update(challenge: Challenge) {
        challengeDao.update(challenge)
    }

    suspend fun delete(challenge: Challenge) {
        challengeDao.delete(challenge)
    }

    fun getChallengeOptions(): Flow<List<ChallengeOptions>> {
        return challengeOptions
    }

}