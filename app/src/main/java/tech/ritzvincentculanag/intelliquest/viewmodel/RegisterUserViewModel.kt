package tech.ritzvincentculanag.intelliquest.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import tech.ritzvincentculanag.intelliquest.db.AppDatabase
import tech.ritzvincentculanag.intelliquest.model.User
import tech.ritzvincentculanag.intelliquest.repository.UserRepository

class RegisterUserViewModel(application: Application) : ViewModel() {

    private var userRepository: UserRepository =
        UserRepository(AppDatabase.getDatabase(application).userDao())

    var inputFirstName = MutableLiveData<String>()
    var inputLastName = MutableLiveData<String>()
    var inputMiddleName = MutableLiveData<String>()
    var inputUsername = MutableLiveData<String>()
    var inputPassword = MutableLiveData<String>()

    fun insertUser() {
        viewModelScope.launch {
            userRepository.insert(
                User(
                    userId = 0,
                    score = 0,
                    firstName = inputFirstName.value!!,
                    lastName = inputLastName.value!!,
                    middleName = inputMiddleName.value!!,
                    username = inputUsername.value!!,
                    password = inputPassword.value!!
                )
            )
        }
    }

}