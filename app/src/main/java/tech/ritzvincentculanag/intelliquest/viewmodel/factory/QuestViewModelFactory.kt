package tech.ritzvincentculanag.intelliquest.viewmodel.factory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import tech.ritzvincentculanag.intelliquest.viewmodel.QuestViewModel

class QuestViewModelFactory(private val application: Application): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(QuestViewModel::class.java)) {
            QuestViewModel(application) as T
        } else {
            throw IllegalArgumentException("ViewModel not found")
        }
    }

}