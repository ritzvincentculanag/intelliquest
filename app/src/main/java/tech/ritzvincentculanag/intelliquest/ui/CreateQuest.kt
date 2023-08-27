package tech.ritzvincentculanag.intelliquest.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import tech.ritzvincentculanag.intelliquest.R
import tech.ritzvincentculanag.intelliquest.databinding.FragmentCreateQuestBinding
import tech.ritzvincentculanag.intelliquest.model.QuestType
import tech.ritzvincentculanag.intelliquest.util.Snacks
import tech.ritzvincentculanag.intelliquest.util.Validators
import tech.ritzvincentculanag.intelliquest.util.Validators.Companion.fieldIsEmpty
import tech.ritzvincentculanag.intelliquest.viewmodel.CreateQuestViewModel
import tech.ritzvincentculanag.intelliquest.viewmodel.factory.CreateQuestViewModelFactory

class CreateQuest : Fragment() {

    private val args by navArgs<CreateQuestArgs>()

    private lateinit var binding: FragmentCreateQuestBinding
    private lateinit var factory: CreateQuestViewModelFactory
    private lateinit var viewModel: CreateQuestViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setupFragment()
        setupTopAppBar()
        setupQuestType()
        setupOnClickListeners()
        setupObservers()
        setupOnFocusChangeListeners()

        return binding.root
    }

    private fun setupFragment() {
        factory = CreateQuestViewModelFactory(requireActivity().application)
        viewModel = ViewModelProvider(this, factory)[CreateQuestViewModel::class.java]
        binding = FragmentCreateQuestBinding.inflate(layoutInflater)
        binding.viewModel = viewModel

        if (args.questState.isCreating) {
            binding.actionCreateQuestion.visibility = View.INVISIBLE
            binding.actionDeleteQuest.visibility = View.INVISIBLE
        }
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
        binding.actionCreate.setOnClickListener {
            val titleIsValid = !fieldIsEmpty(binding.containerTitle)
            val descriptionIsValid = !fieldIsEmpty(binding.containerDescription)
            val questTypeIsValid = !fieldIsEmpty(binding.containerQuestType)
            val durationIsValid = durationIsValid()

            if (
                !titleIsValid &&
                !descriptionIsValid &&
                !questTypeIsValid &&
                !durationIsValid
            ) {
                Snacks.shortSnack(binding.root)
                return@setOnClickListener
            }

            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Create quest")
                .setMessage("Are you sure you want to create the quest?")
                .setPositiveButton(getString(R.string.dialog_positive_button_title)) { _, _ ->
                    createQuest()
                }
                .setNegativeButton(getString(R.string.dialog_negative_button_title)) { _, _ ->

                }
                .show()
        }
    }

    private fun setupObservers() {
        viewModel.inputTimed.observe(viewLifecycleOwner) { isTimed ->
            if (!isTimed) {
                binding.inputDuration.text?.clear()
            }
            binding.inputDuration.isEnabled = isTimed ?: false
        }
    }

    private fun setupOnFocusChangeListeners() {
        binding.inputTitle.setOnFocusChangeListener { _, isFocused ->
            if (!isFocused) {
                fieldIsEmpty(binding.containerTitle)
            }
        }
        binding.inputDescription.setOnFocusChangeListener { _, isFocused ->
            if (!isFocused) {
                fieldIsEmpty(binding.containerDescription)
            }
        }
        binding.inputQuestType.setOnFocusChangeListener { _, isFocused ->
            if (!isFocused) {
                fieldIsEmpty(binding.containerQuestType)
            }
        }
        binding.inputDuration.setOnFocusChangeListener { _, isFocused ->
            if (isFocused) {
                return@setOnFocusChangeListener
            }

            if (fieldIsEmpty(binding.containerDuration)) {
                return@setOnFocusChangeListener
            }

            durationIsValid()
        }
    }

    private fun createQuest() {
        CoroutineScope(Dispatchers.IO).launch {
            viewModel.createQuest().collect {
                viewModel.newQuest = it ?: -1
            }
        }
    }

    private fun durationIsValid(): Boolean {
        val duration = binding.inputDuration.text?.toString()?.trim()?.toInt() ?: 0
        val isValid = duration < 10 || duration > 60

        if (isValid) {
            Validators.setError(
                field = binding.containerDuration,
                message = "Minimum is 10 and maximum is 60"
            )
        } else {
            Validators.clearError(binding.containerDuration)
        }

        return isValid
    }
}