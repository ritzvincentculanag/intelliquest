package tech.ritzvincentculanag.intelliquest.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import tech.ritzvincentculanag.intelliquest.databinding.FragmentCreateQuestBinding
import tech.ritzvincentculanag.intelliquest.model.QuestType

class CreateQuest : Fragment() {

    private lateinit var binding: FragmentCreateQuestBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setupFragment()
        setupTopAppBar()
        setupQuestType()
        setupOnClickListeners()

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

    private fun setupQuestType() {
        val questTypes = QuestType.values().toList().map(QuestType::name)
        val questTypeAdapter = ArrayAdapter(
            requireContext(),
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
            questTypes
        )
        binding.inputQuestType.setText(questTypes[0])
        binding.inputQuestType.setAdapter(questTypeAdapter)
    }

    private fun setupOnClickListeners() {
        binding.optionTime.setOnCheckedChangeListener { _, isChecked ->
            if (!isChecked) {
                binding.inputDuration.text?.clear()
            }
            binding.inputDuration.isEnabled = isChecked
        }
    }

}