package tech.ritzvincentculanag.intelliquest.repository

import kotlinx.coroutines.flow.Flow
import tech.ritzvincentculanag.intelliquest.dao.QuestDao
import tech.ritzvincentculanag.intelliquest.model.Quest

class QuestRepository(private val questDao: QuestDao) {

    private val quests: Flow<List<Quest>> = questDao.getQuests()

    suspend fun insert(quest: Quest) {
        questDao.insert(quest)
    }

    suspend fun update(quest: Quest) {
        questDao.update(quest)
    }

    suspend fun delete(quest: Quest) {
        questDao.delete(quest)
    }

    fun getQuests(): Flow<List<Quest>> = quests

}