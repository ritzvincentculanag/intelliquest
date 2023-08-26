package tech.ritzvincentculanag.intelliquest.ui

import  android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import tech.ritzvincentculanag.intelliquest.LoginActivity
import tech.ritzvincentculanag.intelliquest.R
import tech.ritzvincentculanag.intelliquest.databinding.ActivityRegisterUserBinding
import tech.ritzvincentculanag.intelliquest.factory.RegisterUserViewModelFactory
import tech.ritzvincentculanag.intelliquest.util.Snacks
import tech.ritzvincentculanag.intelliquest.util.Validators.Companion.validateField
import tech.ritzvincentculanag.intelliquest.viewmodel.RegisterUserViewModel

private const val PATTERN_NAME = "^[A-Z][a-z]{2,29}( [A-Z][a-z]{2,29})?( [A-Z])?$"
private const val PATTERN_PASSWORD = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*]).{8,}$"
private const val PATTERN_USERNAME = "^[a-z][a-z_]{7,}$"

class RegisterUser : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterUserBinding
    private lateinit var viewModel: RegisterUserViewModel
    private lateinit var factory: RegisterUserViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        factory = RegisterUserViewModelFactory(application)
        viewModel = ViewModelProvider(this, factory)[RegisterUserViewModel::class.java]
        binding = ActivityRegisterUserBinding.inflate(layoutInflater)
        binding.viewModel = viewModel

        setOnClickListeners()
        setOnFocusChangeListeners()
        setContentView(binding.root)
    }

    private fun setOnClickListeners() {
        binding.actionRegister.setOnClickListener {
            val firstNameIsValid = validateField(binding.containerFirstName, PATTERN_NAME)
            val lastNameIsValid = validateField(binding.containerLastName, PATTERN_NAME)
            val usernameIsValid = validateField(
                field = binding.containerRegUsername,
                message = getString(R.string.validation_username_message),
                pattern = PATTERN_USERNAME
            )
            val passwordIsValid = validateField(
                field = binding.containerRegPassword,
                message = getString(R.string.validation_password_message),
                pattern = PATTERN_PASSWORD
            )
            if (
                !firstNameIsValid ||
                !lastNameIsValid ||
                !usernameIsValid ||
                !passwordIsValid
            ) {
                showInvalidMessage()
                return@setOnClickListener
            }

            if (binding.inputMiddleName.text.toString().isNotEmpty()) {
                val middleNameIsValid = validateField(binding.containerMiddleName, PATTERN_NAME)
                if (!middleNameIsValid) {
                    showInvalidMessage()
                    return@setOnClickListener
                }
            }

            MaterialAlertDialogBuilder(this)
                .setTitle(getString(R.string.dialog_register_title))
                .setMessage(getString(R.string.dialog_register_message))
                .setPositiveButton(getString(R.string.dialog_positive_button_title)) { _, _ ->
                    viewModel.insertUser()
                    startActivity(Intent(this, LoginActivity::class.java))
                }
                .setNegativeButton(getString(R.string.dialog_negative_button_title)) { _, _ ->

                }
                .show()
        }
    }

    private fun setOnFocusChangeListeners() {
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
                    message = getString(R.string.validation_username_message)
                )
            }
        }
        binding.inputRegPassword.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                validateField(
                    field = binding.containerRegPassword,
                    pattern = PATTERN_PASSWORD,
                    message = getString(R.string.validation_password_message)
                )
            }
        }
    }

    private fun showInvalidMessage() {
        Snacks.shortSnack(
            view = binding.root,
            message = getString(R.string.validation_default_message_register)
        )
    }

}