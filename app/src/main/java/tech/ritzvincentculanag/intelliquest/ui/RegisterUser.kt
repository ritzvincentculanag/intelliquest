package tech.ritzvincentculanag.intelliquest.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout
import tech.ritzvincentculanag.intelliquest.LoginActivity
import tech.ritzvincentculanag.intelliquest.databinding.ActivityRegisterUserBinding
import tech.ritzvincentculanag.intelliquest.factory.UserViewModelFactory
import tech.ritzvincentculanag.intelliquest.util.Snacks
import tech.ritzvincentculanag.intelliquest.util.Validators
import tech.ritzvincentculanag.intelliquest.viewmodel.UserViewModel

private const val PATTERN_NAME = "^[A-Z][a-z]{2,29}( [A-Z][a-z]{2,29})?( [A-Z])?$"
private const val PATTERN_PASSWORD = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*]).{8,}$"
private const val PATTERN_USERNAME = "^[a-z][a-z_]{7,}$"

class RegisterUser : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterUserBinding
    private lateinit var viewModel: UserViewModel
    private lateinit var factory: UserViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        factory = UserViewModelFactory(application)
        viewModel = ViewModelProvider(this, factory)[UserViewModel::class.java]
        binding = ActivityRegisterUserBinding.inflate(layoutInflater)
        binding.viewModel = viewModel

        setListeners()
        setContentView(binding.root)
    }

    private fun setListeners() {
        binding.actionRegister.setOnClickListener {
            val firstNameIsValid = validateField(binding.containerFirstName, PATTERN_NAME)
            val lastNameIsValid = validateField(binding.containerLastName, PATTERN_NAME)
            val middleNameIsValid = validateField(binding.containerMiddleName, PATTERN_NAME)
            val usernameIsValid = validateField(binding.containerRegUsername, PATTERN_USERNAME)
            val passwordIsValid = validateField(binding.containerRegPassword, PATTERN_PASSWORD)

            if (
                !firstNameIsValid ||
                !lastNameIsValid ||
                !middleNameIsValid ||
                !usernameIsValid ||
                !passwordIsValid
            ) {
                Snacks.shortSnack(
                    view = binding.root,
                    message = "Some fields have invalid value"
                )
                return@setOnClickListener
            }

            MaterialAlertDialogBuilder(this)
                .setTitle("Register")
                .setMessage("Proceed to registration?")
                .setPositiveButton("Yes") { _, _ ->
                    viewModel.registerUser()
                    startActivity(Intent(this, LoginActivity::class.java))
                }
                .setNegativeButton("Cancel") { _, _ ->

                }
                .show()
        }
        binding.inputFirstName.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                validateField(
                    field = binding.containerFirstName,
                    pattern = PATTERN_NAME
                )
            }
        }
        binding.inputLastName.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                validateField(
                    field = binding.containerLastName,
                    pattern = PATTERN_NAME
                )
            }
        }
        binding.inputMiddleName.setOnFocusChangeListener { _, focused ->
            if (focused || binding.inputMiddleName.text.toString().isEmpty()) {
                return@setOnFocusChangeListener
            }

            validateField(
                field = binding.containerLastName,
                pattern = PATTERN_NAME
            )
        }
        binding.inputRegUsername.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                validateField(
                    field = binding.containerRegUsername,
                    pattern = PATTERN_USERNAME,
                    message = "Username must be all lowercase, " +
                            "must be at least 8 characters long, " +
                            "and is separated by underscore"
                )
            }
        }
        binding.inputRegPassword.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                validateField(
                    field = binding.containerRegPassword,
                    pattern = PATTERN_PASSWORD,
                    message = "Password must be " +
                            "at least 8 characters long, " +
                            "has at least 1 special character, " +
                            "an uppercase letter, " +
                            "and a lowercase letter."
                )
            }
        }
    }

    private fun validateField(
        field: TextInputLayout,
        pattern: String,
        message: String = "Invalid field"
    ): Boolean {
        val input = field.editText?.text.toString().trim()
        val regex = Regex(pattern)

        return if (regex.matches(input)) {
            Validators.clearErrors(field)
            true
        } else {
            Validators.setError(
                field = field,
                message = message
            )
            false
        }
    }

}