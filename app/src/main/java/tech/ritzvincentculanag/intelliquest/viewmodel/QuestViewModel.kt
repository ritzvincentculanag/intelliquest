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
import tech.ritzvincentculanag.intelliquest.repository.ChallengeRepository
import tech.ritzvincentculanag.intelliquest.repository.QuestRepository
import tech.ritzvincentculanag.intelliquest.repository.UserRepository
import tech.ritzvincentculanag.intelliquest.util.SessionManager

class QuestViewModel(private val application: Application) : ViewModel() {

    private val repository: QuestRepository = QuestRepository(AppDatabase.getDatabase(application).questDao())
    private val challengeRepository: ChallengeRepository = ChallengeRepository(AppDatabase.getDatabase(application).challengeDao())
    private val userRepository: UserRepository = UserRepository(AppDatabase.getDatabase(application).userDao())
    private val quests: Flow<List<Quest>> = repository.getQuests()
    private val userQuests: Flow<List<UserQuests>> = userRepository.getUserQuests()
    private val challenges = challengeRepository.getChallengeOptions()

    fun getQuests(): Flow<List<Quest>> {
        val questsFlow = flow {
            quests.collect {
                emit(it)
            }
        }

        return questsFlow.flowOn(Dispatchers.Main)
    }

    fun getUserQuests(): Flow<List<Quest>> {
        val userQuestsFlow = flow {
            val session = SessionManager(application)
            val userId = session.getInt("USER_ID")

            userQuests.collect { items ->
                items.forEach {
                    if (it.user.userId == userId) {
                        emit(it.quests)
                    }
                }
            }
        }

        return userQuestsFlow.flowOn(Dispatchers.Default)
    }

    fun getQuestChallenges() = repository.getQuestChallenges()

}