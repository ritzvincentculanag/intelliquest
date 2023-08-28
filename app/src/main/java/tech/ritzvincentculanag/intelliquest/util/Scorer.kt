package tech.ritzvincentculanag.intelliquest.util

import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import tech.ritzvincentculanag.intelliquest.db.AppDatabase

class Scorer {
    companion object {
        fun updateScore(context: Context, score: Int) {
            val repository = AppDatabase.getDatabase(context).userDao()
            val session = SessionManager(context)
            val userId = session.getInt("USER_ID")
            val user = repository.getUser(userId)

            CoroutineScope(Dispatchers.IO).launch {
                user.collect {
                    it.score = score
                    repository.update(it)
                }
            }
        }
    }
}