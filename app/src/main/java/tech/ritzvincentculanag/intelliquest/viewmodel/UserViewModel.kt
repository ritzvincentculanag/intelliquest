package tech.ritzvincentculanag.intelliquest.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
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

class UserViewModel(application: Application) : ViewModel() {

    var inputUsername = MutableLiveData<String>()
    var inputPassword = MutableLiveData<String>()

    private val repository: UserRepository
    private val users: Flow<List<User>>

    init {
        repository = UserRepository(AppDatabase.getDatabase(application).userDao())
        users = repository.getUsers()
    }

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

    fun getUsers(): Flow<List<User>> = users

    fun getUser(username: String, password: String): Flow<User> {
        val userFlow = flow {
            val user = repository.getUser(username, password)
            emit(user)
        }

        return userFlow.flowOn(Dispatchers.IO)
    }

}