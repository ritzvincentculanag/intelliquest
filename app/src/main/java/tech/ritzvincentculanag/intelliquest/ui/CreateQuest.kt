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
import tech.ritzvincentculanag.intelliquest.util.SessionManager
import tech.ritzvincentculanag.intelliquest.util.Snacks
import tech.ritzvincentculanag.intelliquest.util.Validators
import tech.ritzvincentculanag.intelliquest.util.Validators.Companion.clearError
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

        if (args.questState?.isCreating == true) {
            viewModel.currentQuest = 0
            viewModel.inputTimed.value = false
            viewModel.inputPublic.value = false

            binding.actionDeleteQuest.visibility = View.INVISIBLE
            binding.actionCreateChallenges.visibility = View.INVISIBLE
        } else {
            binding.actionCreate.text = "Update"
            binding.materialToolbar.title = "Update quest"
        }

        if (args.quest != null) {
            val quest = args.quest!!

            viewModel.currentQuest = quest.questId.toLong()
            viewModel.inputTitle.value = quest.name
            viewModel.inputDescription.value = quest.description
            viewModel.inputQuestType.value = quest.questType.name
            viewModel.inputDuration.value = quest.timeDuration.toString()
            viewModel.inputTimed.value = quest.isTimed
            viewModel.inputPublic.value = quest.isPublic

            binding.inputQuestType.isEnabled = false
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
        binding.actionCreateChallenges.setOnClickListener {
            val session = SessionManager(requireContext())
            val quest = viewModel.getQuest(session.getInt("USER_ID"))
            val directions = CreateQuestDirections.actionCreateQuestToChallengesFragment(quest)
            findNavController().navigate(directions)
        }
        binding.actionCreate.setOnClickListener {
            val titleIsValid = !fieldIsEmpty(binding.containerTitle)
            val descriptionIsValid = !fieldIsEmpty(binding.containerDescription)
            val questTypeIsValid = !fieldIsEmpty(binding.containerQuestType)

            if (
                !titleIsValid &&
                !descriptionIsValid &&
                !questTypeIsValid
            ) {
                Snacks.shortSnack(binding.root)
                return@setOnClickListener
            }

            if (!durationIsValid()) {
                Snacks.shortSnack(binding.root)
                return@setOnClickListener
            }

            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Warning")
                .setMessage("Are you sure you want to proceed?")
                .setPositiveButton(getString(R.string.dialog_positive_button_title)) { _, _ ->
                    if (args.questState?.isCreating!!) {
                        createQuest()
                        showActions()
                    } else {
                        updateQuest()
                    }
                }
                .setNegativeButton(getString(R.string.dialog_negative_button_title)) { _, _ ->

                }
                .show()
        }
        binding.actionDeleteQuest.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Delete quest")
                .setMessage("Are you sure you want to proceed?")
                .setPositiveButton(getString(R.string.dialog_positive_button_title)) { _, _ ->
                    viewModel.deleteQuest()
                    Snacks.longSnack(
                        view = binding.root,
                        message = "Quest deleted successfully"
                    )
                    findNavController().navigateUp()
                    onDestroy()
                }
                .setNegativeButton(getString(R.string.dialog_negative_button_title)) { _, _ ->

                }
                .show()
        }
    }

    private fun setupObservers() {
        viewModel.inputTimed.observe(viewLifecycleOwner) { isTimed ->
            if (isTimed) {
                clearError(binding.containerDuration)
            } else {
                viewModel.inputDuration.value = "15"
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
            if (fieldIsEmpty(binding.containerDuration)) {
                return@setOnFocusChangeListener
            }

            if (!isFocused) {
                durationIsValid()
            }
        }
    }

    private fun createQuest() {
        CoroutineScope(Dispatchers.IO).launch {
            viewModel.createQuest().collect {
                viewModel.currentQuest = it ?: -1
            }
        }
    }

    private fun updateQuest() {
        CoroutineScope(Dispatchers.IO).launch {
            viewModel.updateQuest().collect {
                viewModel.currentQuest = (it ?: -1).toLong()
            }
        }
    }

    private fun showActions() {
        binding.actionCreate.isEnabled = false
        binding.actionDeleteQuest.visibility = View.VISIBLE
        binding.actionCreateChallenges.visibility = View.VISIBLE
    }

    private fun durationIsValid(): Boolean {
        if (!binding.inputDuration.isEnabled) {
            return false
        }

        val duration = viewModel.inputDuration.value?.toInt() ?: 0
        val isValid = duration in 10..60

        if (!isValid) {
            Validators.setError(
                field = binding.containerDuration,
                message = "Minimum is 10 and maximum is 60"
            )
        } else {
            clearError(binding.containerDuration)
        }

        return isValid
    }
}