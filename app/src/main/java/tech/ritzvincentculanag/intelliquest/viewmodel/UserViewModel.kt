package tech.ritzvincentculanag.intelliquest.viewmodel

import android.app.Application
import android.view.View
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
import tech.ritzvincentculanag.intelliquest.util.SessionManager
import tech.ritzvincentculanag.intelliquest.util.Snacks

class UserViewModel(private val application: Application) : ViewModel() {

    var inputUsername = MutableLiveData<String>()
    var inputPassword = MutableLiveData<String>()

    private val repository: UserRepository = UserRepository(AppDatabase.getDatabase(application).userDao())
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

    fun getUsers(): Flow<List<User>> = users

    fun getUser(username: String, password: String): Flow<User?> {
        val userFlow = flow {
            val user = repository.getUser(username, password)
            emit(user)
        }

        return userFlow.flowOn(Dispatchers.IO)
    }

    suspend fun login(view: View) {
        val username = inputUsername.value!!
        val password = inputPassword.value!!
        val sessionManager = SessionManager(application.applicationContext)

        getUser(username, password).collect { user ->
            if (user == null) {
                Snacks.shortSnack(view, "User not found")
                return@collect
            }

            sessionManager.saveSession(user)
            Snacks.shortSnack(view, "Welcome user!")
        }
    }

}