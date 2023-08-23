package tech.ritzvincentculanag.intelliquest

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputLayout
import tech.ritzvincentculanag.intelliquest.databinding.ActivityLoginBinding
import tech.ritzvincentculanag.intelliquest.factory.UserViewModelFactory
import tech.ritzvincentculanag.intelliquest.util.Snacks
import tech.ritzvincentculanag.intelliquest.util.Validators
import tech.ritzvincentculanag.intelliquest.viewmodel.UserViewModel

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: UserViewModel
    private lateinit var factory: UserViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        factory = UserViewModelFactory(application)
        viewModel = ViewModelProvider(this, factory)[UserViewModel::class.java]
        binding = ActivityLoginBinding.inflate(layoutInflater)
        binding.viewModel = viewModel
        setContentView(binding.root)
        setValidations()
    }

    private fun setValidations() {
        val containerUsername = binding.containerUsername
        val containerPassword = binding.containerPassword

        binding.actionLogin.setOnClickListener {
            val usernameIsValid = validateField(containerUsername)
            val passwordIsValid = validateField(containerPassword)

            if (!usernameIsValid && !passwordIsValid) {
                Snacks.shortSnack(binding.root, "Fields are required")
                return@setOnClickListener
            }
        }
        binding.inputUsername.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                validateField(containerUsername)
            }
        }
        binding.inputPassword.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                validateField(containerPassword)
            }
        }
    }

    private fun validateField(field: TextInputLayout): Boolean {
        val input = binding.inputUsername.text.toString()

        return if (input.isEmpty()) {
            Validators.setError(field)
            false
        } else {
            Validators.clearErrors(field)
            true
        }
    }

}