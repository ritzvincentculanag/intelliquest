package tech.ritzvincentculanag.intelliquest.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import tech.ritzvincentculanag.intelliquest.db.AppDatabase
import tech.ritzvincentculanag.intelliquest.model.Quest
import tech.ritzvincentculanag.intelliquest.model.QuestType
import tech.ritzvincentculanag.intelliquest.util.SessionManager

class CreateQuestViewModel(private val application: Application) : ViewModel() {

    private val repository = AppDatabase.getDatabase(application).questDao()

    var inputTitle = MutableLiveData<String>()
    var inputDescription = MutableLiveData<String>()
    var inputQuestType = MutableLiveData<String>()
    var inputDuration = MutableLiveData<String>()
    var inputTimed = MutableLiveData<Boolean>()
    var inputPublic = MutableLiveData<Boolean>()

    fun createQuest() {
        viewModelScope.launch {
            val session = SessionManager(application)
            val userId = session.getInt("USER_ID")

            repository.insert(Quest(
                questId = 0,
                originUserId = userId,
                timeDuration = inputDuration.value?.toInt() ?: 15,
                name = inputTitle.value ?: "",
                description = inputDescription.value ?: "",
                questType = QuestType.valueOf(inputQuestType.value ?: "EASY"),
                isTimed = inputTimed.value ?: false,
                isPublic = inputPublic.value ?: true
            ))
        }
    }

}