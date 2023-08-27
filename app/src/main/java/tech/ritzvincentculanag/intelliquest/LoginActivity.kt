package tech.ritzvincentculanag.intelliquest

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import tech.ritzvincentculanag.intelliquest.databinding.ActivityLoginBinding
import tech.ritzvincentculanag.intelliquest.ui.Dashboard
import tech.ritzvincentculanag.intelliquest.ui.RegisterUser
import tech.ritzvincentculanag.intelliquest.util.SessionManager
import tech.ritzvincentculanag.intelliquest.util.Snacks
import tech.ritzvincentculanag.intelliquest.util.Validators.Companion.fieldIsEmpty
import tech.ritzvincentculanag.intelliquest.viewmodel.LoginViewModel
import tech.ritzvincentculanag.intelliquest.viewmodel.factory.LoginViewModelFactory

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel
    private lateinit var factory: LoginViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        prepareLayout()
        checkUserStatus()
        setOnClickListeners()
        setOnFocusChangeListeners()
    }

    private fun checkUserStatus() {
        val sessionManager = SessionManager(applicationContext)
        val userIsLoggedIn = sessionManager.userIsActive()

        if (userIsLoggedIn) {
            Snacks.shortSnack(binding.root, message = "User is already logged in!")
        }
    }

    private fun prepareLayout() {
        factory = LoginViewModelFactory(application)
        viewModel = ViewModelProvider(this, factory)[LoginViewModel::class.java]
        binding = ActivityLoginBinding.inflate(layoutInflater)
        binding.viewModel = viewModel
        setContentView(binding.root)
    }

    private fun setOnClickListeners() {
        binding.actionLogin.setOnClickListener {
            if (
                fieldIsEmpty(binding.containerUsername) &&
                fieldIsEmpty(binding.containerPassword)
            ) {
                Snacks.shortSnack(binding.root)
                return@setOnClickListener
            }

            CoroutineScope(Dispatchers.Default).launch {
                viewModel.login(binding.root).collect { isLoggedIn ->
                    if (isLoggedIn) {
                        goToDashboard()
                    }
                }
            }
        }
        binding.actionSignup.setOnClickListener {
            startActivity(Intent(this, RegisterUser::class.java))
        }
    }

    private fun setOnFocusChangeListeners() {
        binding.inputUsername.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                fieldIsEmpty(binding.containerUsername)
            }
        }
        binding.inputPassword.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                fieldIsEmpty(binding.containerPassword)
            }
        }
    }

    private fun goToDashboard() {
        startActivity(Intent(this, Dashboard::class.java))
    }

}