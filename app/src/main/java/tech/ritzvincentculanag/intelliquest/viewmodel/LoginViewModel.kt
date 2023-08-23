package tech.ritzvincentculanag.intelliquest.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {

    var inputUsername = MutableLiveData<String>()
    var inputPassword = MutableLiveData<String>()

    fun toastData() {
        val username = inputUsername.value
        val password = inputPassword.value

        inputPassword.value = inputUsername.value.toString()
        inputUsername.value = "IT'S FREAKING WORKING!"
    }
}