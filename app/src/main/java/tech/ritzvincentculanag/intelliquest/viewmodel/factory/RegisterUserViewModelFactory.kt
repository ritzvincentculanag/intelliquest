package tech.ritzvincentculanag.intelliquest.viewmodel.factory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import tech.ritzvincentculanag.intelliquest.viewmodel.RegisterUserViewModel

class RegisterUserViewModelFactory(private val application: Application): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(RegisterUserViewModel::class.java)) {
            RegisterUserViewModel(application) as T
        } else {
            throw IllegalArgumentException("ViewModel not found")
        }
    }

}