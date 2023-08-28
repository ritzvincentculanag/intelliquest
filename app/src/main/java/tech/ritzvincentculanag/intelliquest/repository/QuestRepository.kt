package tech.ritzvincentculanag.intelliquest.repository

import kotlinx.coroutines.flow.Flow
import tech.ritzvincentculanag.intelliquest.dao.QuestDao
import tech.ritzvincentculanag.intelliquest.model.Quest
import tech.ritzvincentculanag.intelliquest.model.relationship.QuestChallenge

class QuestRepository(private val questDao: QuestDao) {

    private val quests = questDao.getQuests()
    private val questChallenges =questDao.getQuestChallenges()

    suspend fun insert(quest: Quest): Long {
        return questDao.insert(quest)
    }

    suspend fun update(quest: Quest): Int {
        return questDao.update(quest)
    }

    suspend fun delete(quest: Quest) {
        questDao.delete(quest)
    }

    fun getQuests(): Flow<List<Quest>> = quests

    fun getQuestChallenges(): Flow<List<QuestChallenge>> = questChallenges

}