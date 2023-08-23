package tech.ritzvincentculanag.intelliquest.util

import com.google.android.material.textfield.TextInputLayout

class Validators {
    companion object {
        fun clearErrors(field: TextInputLayout) {
            field.error = ""
            field.isErrorEnabled = false
        }

        fun setError(field: TextInputLayout, message: String = "Field is required") {
            field.error = message
            field.isErrorEnabled = true
        }
    }
}