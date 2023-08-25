package tech.ritzvincentculanag.intelliquest.viewmodel

import android.app.Activity
import android.app.Application
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import tech.ritzvincentculanag.intelliquest.db.AppDatabase
import tech.ritzvincentculanag.intelliquest.model.User
import tech.ritzvincentculanag.intelliquest.repository.UserRepository
import tech.ritzvincentculanag.intelliquest.util.SessionManager
import tech.ritzvincentculanag.intelliquest.util.Snacks

class UserViewModel(private val application: Application) : ViewModel() {

    private val sessionManager = SessionManager(application.applicationContext)
    private val repository: UserRepository = UserRepository(AppDatabase.getDatabase(application).userDao())
    private val users: Flow<List<User>> = repository.getUsers()

    var inputUsername = MutableLiveData<String>()
    var inputPassword = MutableLiveData<String>()

    var inputRegFirstName = MutableLiveData<String>()
    var inputRegLastName = MutableLiveData<String>()
    var inputRegMiddleName = MutableLiveData<String>()
    var inputRegUsername = MutableLiveData<String>()
    var inputRegPassword = MutableLiveData<String>()

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

    fun getUser(userId: Int): Flow<User?> {
        val userFlow = flow {
            val user = repository.getUser(userId)
            emit(user)
        }

        return userFlow.flowOn(Dispatchers.IO)
    }

    fun registerUser() {
        val user = User(
            firstName = inputRegFirstName.value!!,
            lastName = inputRegLastName.value!!,
            middleName = inputRegMiddleName.value!!,
            username = inputRegUsername.value!!,
            password = inputRegPassword.value!!
        )

        insert(user)
    }

    fun logout(activity: Activity) {
        MaterialAlertDialogBuilder(activity)
            .setTitle("Logout")
            .setMessage("Are you sure you want to logout?")
            .setPositiveButton("Yes") { _, _ ->
                sessionManager.clearSession()
            }
            .setNegativeButton("No") { _, _ ->

            }
            .show()
    }

    fun login(view: View): Boolean {
        val username = inputUsername.value!!
        val password = inputPassword.value!!
        var isLoggedIn = false

        runBlocking {
            getUser(username, password).collect { user ->
                if (user == null) {
                    Snacks.shortSnack(view, "User not found")
                    return@collect
                }

                isLoggedIn = true
                sessionManager.saveSession(user)
                Snacks.shortSnack(view, "Welcome user!")
            }
        }

        return isLoggedIn
    }

}