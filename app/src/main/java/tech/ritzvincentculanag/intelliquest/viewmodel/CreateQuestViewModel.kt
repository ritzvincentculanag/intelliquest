package tech.ritzvincentculanag.intelliquest.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import tech.ritzvincentculanag.intelliquest.db.AppDatabase
import tech.ritzvincentculanag.intelliquest.model.Quest
import tech.ritzvincentculanag.intelliquest.model.QuestType
import tech.ritzvincentculanag.intelliquest.util.SessionManager

class CreateQuestViewModel(private val application: Application) : ViewModel() {

    private val repository = AppDatabase.getDatabase(application).questDao()

    var newQuest: Long = -1

    var inputTitle = MutableLiveData<String>()
    var inputDescription = MutableLiveData<String>()
    var inputQuestType = MutableLiveData<String>()
    var inputDuration = MutableLiveData<String>()
    var inputTimed = MutableLiveData<Boolean>()
    var inputPublic = MutableLiveData<Boolean>()

    fun createQuest(): Flow<Long?> {
        val questFlow = flow {
            val session = SessionManager(application)
            val userId = session.getInt("USER_ID")
            val questId = repository.insert(Quest(
                questId = 0,
                originUserId = userId,
                timeDuration = inputDuration.value?.toInt() ?: 15,
                name = inputTitle.value ?: "",
                description = inputDescription.value ?: "",
                questType = QuestType.valueOf(inputQuestType.value ?: "EASY"),
                isTimed = inputTimed.value ?: false,
                isPublic = inputPublic.value ?: true
            ))

            emit(questId)
        }

        return questFlow.flowOn(Dispatchers.IO)
    }
}