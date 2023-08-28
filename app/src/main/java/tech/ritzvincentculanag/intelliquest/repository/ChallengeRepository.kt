package tech.ritzvincentculanag.intelliquest.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import tech.ritzvincentculanag.intelliquest.dao.ChallengeDao
import tech.ritzvincentculanag.intelliquest.model.Challenge
import tech.ritzvincentculanag.intelliquest.model.relationship.ChallengeOptions

class ChallengeRepository(private val challengeDao: ChallengeDao) {

    private val challengeOptions = challengeDao.getChallengeOptions()

    fun insert(challenge: Challenge): Flow<Long> {
        val challengeFlow = flow {
            val challengeId = challengeDao.insert(challenge)
            emit(challengeId)
        }

        return challengeFlow.flowOn(Dispatchers.IO)
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