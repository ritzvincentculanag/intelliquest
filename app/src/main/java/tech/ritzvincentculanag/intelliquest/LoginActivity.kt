package tech.ritzvincentculanag.intelliquest

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import tech.ritzvincentculanag.intelliquest.databinding.ActivityLoginBinding
import tech.ritzvincentculanag.intelliquest.factory.UserViewModelFactory
import tech.ritzvincentculanag.intelliquest.viewmodel.LoginViewModel
import tech.ritzvincentculanag.intelliquest.viewmodel.UserViewModel

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        binding.viewModel = viewModel

        val userViewModelFactory = UserViewModelFactory(application)
        val userViewModel = ViewModelProvider(this, userViewModelFactory)[UserViewModel::class.java]

        CoroutineScope(Dispatchers.IO).launch {
            userViewModel.getUser("ritchiev", "J&Jwuth10").collect {
                Log.d("COROTAG", it.toString())
            }
        }
    }

}