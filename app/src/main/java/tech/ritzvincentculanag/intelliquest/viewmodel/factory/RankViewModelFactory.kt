package tech.ritzvincentculanag.intelliquest.viewmodel.factory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import tech.ritzvincentculanag.intelliquest.viewmodel.RankViewModel

class RankViewModelFactory(private val application: Application) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RankViewModel::class.java)) {
            return RankViewModel(application) as T
        } else {
            throw IllegalArgumentException("ViewModel not found")
        }
    }

}