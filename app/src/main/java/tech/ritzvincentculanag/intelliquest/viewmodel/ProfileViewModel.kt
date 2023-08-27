package tech.ritzvincentculanag.intelliquest.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import tech.ritzvincentculanag.intelliquest.db.AppDatabase
import tech.ritzvincentculanag.intelliquest.model.User
import tech.ritzvincentculanag.intelliquest.repository.UserRepository

class ProfileViewModel(private val application: Application) : ViewModel() {

    private val repository = UserRepository(AppDatabase.getDatabase(application).userDao())
    private val users: Flow<List<User>> = repository.getUsers()

    fun insert(user: User) {
        viewModelScope.launch {
            repository.insert(user)
        }
    }

    fun update(user: User) {
        viewModelScope.launch {
            repository.update(user)
        }
    }

    fun delete(user: User) {
        viewModelScope.launch {
            repository.delete(user)
        }
    }

    fun getUser(userId: Int): Flow<User?> {
        val userFlow = flow {
            val user = repository.getUser(userId)
            emit(user)
        }

        return userFlow.flowOn(Dispatchers.IO)
    }

}