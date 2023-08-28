package tech.ritzvincentculanag.intelliquest.util

import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import tech.ritzvincentculanag.intelliquest.db.AppDatabase

class Scorer {
    companion object {
        fun updateScore(context: Context, score: Int) {
            val repository = AppDatabase.getDatabase(context).userDao()
            val session = SessionManager(context)
            val userId = session.getInt("USER_ID")
            val user = repository.getUser(userId)
            val userFlow = flow {
                user.collect {
                    emit(it)
                }
            }

            CoroutineScope(Dispatchers.IO).launch {
                userFlow.collect {
                    it.score += score
                    repository.update(it)
                }
            }
        }
    }
}