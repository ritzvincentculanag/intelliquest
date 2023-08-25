package tech.ritzvincentculanag.intelliquest.repository

import tech.ritzvincentculanag.intelliquest.dao.QuestDao
import tech.ritzvincentculanag.intelliquest.model.Quest

class QuestRepository(private val questDao: QuestDao) {

    suspend fun insert(quest: Quest) {
        questDao.insert(quest)
    }

    suspend fun update(quest: Quest) {
        questDao.update(quest)
    }

    suspend fun delete(quest: Quest) {
        questDao.delete(quest)
    }

}