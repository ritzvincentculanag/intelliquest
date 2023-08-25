package tech.ritzvincentculanag.intelliquest.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import tech.ritzvincentculanag.intelliquest.databinding.FragmentProfileBinding
import tech.ritzvincentculanag.intelliquest.factory.UserViewModelFactory
import tech.ritzvincentculanag.intelliquest.util.SessionManager
import tech.ritzvincentculanag.intelliquest.viewmodel.UserViewModel

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var viewModel: UserViewModel
    private lateinit var factory: UserViewModelFactory
    private lateinit var sessionManager: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        factory = UserViewModelFactory(requireActivity().application)
        viewModel = ViewModelProvider(this, factory)[UserViewModel::class.java]
        sessionManager = SessionManager(requireContext())
        binding = FragmentProfileBinding.inflate(layoutInflater)

        loadUserData()

        return binding.root
    }

    private fun loadUserData() {
        CoroutineScope(Dispatchers.IO).launch {
            val userId = sessionManager.getInt("USER_ID")
            viewModel.getUser(userId).collect {
                binding.user = it
            }
        }
    }

}