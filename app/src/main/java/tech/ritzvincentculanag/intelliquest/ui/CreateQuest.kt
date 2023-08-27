package tech.ritzvincentculanag.intelliquest.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import tech.ritzvincentculanag.intelliquest.databinding.FragmentCreateQuestBinding

class CreateQuest : Fragment() {

    private lateinit var binding: FragmentCreateQuestBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setupFragment()
        setupTopAppBar()

        return binding.root
    }

    private fun setupFragment() {
        binding = FragmentCreateQuestBinding.inflate(layoutInflater)
    }

    private fun setupTopAppBar() {
        binding.materialToolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

}