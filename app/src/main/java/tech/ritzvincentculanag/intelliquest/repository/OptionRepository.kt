package tech.ritzvincentculanag.intelliquest.repository

import tech.ritzvincentculanag.intelliquest.dao.OptionDao
import tech.ritzvincentculanag.intelliquest.model.Option

class OptionRepository(private val optionDao: OptionDao) {

    suspend fun insert(option: Option) {
        optionDao.insert(option)
    }

    suspend fun update(option: Option) {
        optionDao.update(option)
    }

}