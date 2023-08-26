package tech.ritzvincentculanag.intelliquest.viewmodel.factory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import tech.ritzvincentculanag.intelliquest.viewmodel.UserViewModel

class UserViewModelFactory(private val application: Application): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            UserViewModel(application) as T
        } else {
            throw IllegalArgumentException("ViewModel not found")
        }
    }

}