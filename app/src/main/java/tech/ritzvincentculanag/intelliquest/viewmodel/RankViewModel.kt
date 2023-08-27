package tech.ritzvincentculanag.intelliquest.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow
import tech.ritzvincentculanag.intelliquest.db.AppDatabase
import tech.ritzvincentculanag.intelliquest.model.User

class RankViewModel(private val application: Application) : ViewModel() {

    private val userRepository = AppDatabase.getDatabase(application).userDao()
    private val users = userRepository.getUsers()

    fun getUsers(): Flow<List<User>> {
        return users
    }

}