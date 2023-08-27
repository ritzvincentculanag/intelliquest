package tech.ritzvincentculanag.intelliquest.viewmodel.factory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import tech.ritzvincentculanag.intelliquest.viewmodel.CreateQuestViewModel

class CreateQuestViewModelFactory(private val application: Application) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CreateQuestViewModel::class.java)) {
            return CreateQuestViewModel(application) as T
        } else {
            throw IllegalArgumentException("ViewModel not found")
        }
    }
}