package tech.ritzvincentculanag.intelliquest.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import tech.ritzvincentculanag.intelliquest.db.AppDatabase
import tech.ritzvincentculanag.intelliquest.model.Quest
import tech.ritzvincentculanag.intelliquest.model.relationship.UserQuests
import tech.ritzvincentculanag.intelliquest.repository.UserRepository

class QuestViewModel(private val application: Application) : ViewModel() {

    private val userRepository: UserRepository = UserRepository(AppDatabase.getDatabase(application).userDao())
    private val userQuests: Flow<List<UserQuests>> = userRepository.getUserQuests()

    fun getUserQuests(userId: Int): Flow<List<Quest>> {
        val userQuestsFlow = flow<List<Quest>> {
            userQuests.collect {
                emit(it[userId].quests)
            }
        }

        return userQuestsFlow.flowOn(Dispatchers.Default)
    }

}