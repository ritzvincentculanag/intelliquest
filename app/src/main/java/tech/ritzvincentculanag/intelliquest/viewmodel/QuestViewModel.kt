package tech.ritzvincentculanag.intelliquest.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import tech.ritzvincentculanag.intelliquest.db.AppDatabase
import tech.ritzvincentculanag.intelliquest.model.Quest
import tech.ritzvincentculanag.intelliquest.repository.QuestRepository

class QuestViewModel(application: Application) : ViewModel() {

    private val repository: QuestRepository = QuestRepository(AppDatabase.getDatabase(application).questDao())
    private val userQuests: Flow<List<Quest>> = repository.getQuests()

    fun getQuests(): Flow<List<Quest>> {
        val userQuestsFlow = flow {
            userQuests.collect {
                emit(it)
            }
        }

        return userQuestsFlow.flowOn(Dispatchers.Main)
    }

}