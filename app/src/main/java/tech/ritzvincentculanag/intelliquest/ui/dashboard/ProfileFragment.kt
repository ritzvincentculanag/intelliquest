package tech.ritzvincentculanag.intelliquest.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import tech.ritzvincentculanag.intelliquest.LoginActivity
import tech.ritzvincentculanag.intelliquest.databinding.FragmentProfileBinding
import tech.ritzvincentculanag.intelliquest.util.SessionManager
import tech.ritzvincentculanag.intelliquest.util.Snacks
import tech.ritzvincentculanag.intelliquest.viewmodel.ProfileViewModel
import tech.ritzvincentculanag.intelliquest.viewmodel.factory.ProfileViewModelFactory

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var viewModel: ProfileViewModel
    private lateinit var factory: ProfileViewModelFactory
    private lateinit var sessionManager: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        setupFragment()
        loadUserData()
        setOnClickListeners()

        return binding.root
    }

    private fun setupFragment() {
        factory = ProfileViewModelFactory(requireActivity().application)
        viewModel = ViewModelProvider(this, factory)[ProfileViewModel::class.java]
        sessionManager = SessionManager(requireContext())
        binding = FragmentProfileBinding.inflate(layoutInflater)
    }

    private fun loadUserData() {
        CoroutineScope(Dispatchers.IO).launch {
            val userId = sessionManager.getInt("USER_ID")
            viewModel.getUser(userId).collect {
                binding.user = it
            }
        }
    }

    private fun setOnClickListeners() {
        binding.actionLogout.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Yes") { _, _ ->
                    sessionManager.clearSession()
                    startActivity(Intent(requireContext(), LoginActivity::class.java))
                    onDestroy()
                }
                .setNegativeButton("No") { _, _ ->
                    Snacks.shortSnack(
                        view = binding.root,
                        message = "Logout cancelled"
                    )
                }
                .show()
        }
        binding.actionDeleteProfile.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Delete profile")
                .setMessage("Are you sure you want to delete your profile? " +
                        "All data associated with it will also be lost.")
                .setPositiveButton("Yes") { _, _ ->
                    CoroutineScope(Dispatchers.IO).launch {
                        val userId = sessionManager.getInt("USER_ID")
                        viewModel.getUser(userId).collect {
                            viewModel.delete(it)
                            startActivity(Intent(requireContext(), LoginActivity::class.java))
                            onDestroy()
                        }
                    }
                }
                .setNegativeButton("No") { _, _ ->
                    Snacks.shortSnack(
                        view = binding.root,
                        message = "Delete cancelled"
                    )
                }
                .show()
        }
    }

}