package tech.ritzvincentculanag.intelliquest.util

import android.view.View
import com.google.android.material.snackbar.Snackbar

class Snacks {
    companion object {
        fun shortSnack(view: View, message: String = "Error") {
            Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show()
        }

        fun longSnack(view: View, message: String = "Error") {
            Snackbar.make(view, message, Snackbar.LENGTH_LONG).show()
        }
    }
}