package tech.ritzvincentculanag.intelliquest.util

import com.google.android.material.textfield.TextInputLayout

class Validators {
    companion object {
        fun clearError(field: TextInputLayout) {
            field.error = ""
            field.isErrorEnabled = false
        }

        fun setError(field: TextInputLayout, message: String = "Field is required") {
            field.error = message
            field.isErrorEnabled = true
        }

        fun validateField(
            field: TextInputLayout,
            pattern: String,
            message: String = "Field is invalid"
        ): Boolean {
            val input = field.editText?.text.toString().trim()
            val regex = Regex(pattern)
            val isValid = regex.matches(input)

            if (isValid) {
                clearError(field)
            } else {
                setError(field, message)
            }

            return isValid
        }
    }
}