package tech.ritzvincentculanag.intelliquest.ui.create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import tech.ritzvincentculanag.intelliquest.databinding.FragmentCreateEasyChallengeBinding
import tech.ritzvincentculanag.intelliquest.db.AppDatabase
import tech.ritzvincentculanag.intelliquest.model.Challenge
import tech.ritzvincentculanag.intelliquest.model.Quest
import tech.ritzvincentculanag.intelliquest.repository.ChallengeRepository
import tech.ritzvincentculanag.intelliquest.util.Snacks

class CreateEasyChallenge(private val quest: Quest) : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentCreateEasyChallengeBinding
    private lateinit var repository: ChallengeRepository


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setupFragment()
        setupOnClickListeners()

        return binding.root
    }

    private fun setupFragment() {
        repository = ChallengeRepository(AppDatabase.getDatabase(requireContext()).challengeDao())
        binding = FragmentCreateEasyChallengeBinding.inflate(layoutInflater)
    }

    private fun setupOnClickListeners() {
        binding.actionSaveEasyQuestion.setOnClickListener {
            createChallenge()
        }
    }

    private fun createChallenge() {
        val optionIsValid = binding.radioGroup.checkedRadioButtonId != -1
        val questionIsValid = binding.inputEasyQuestion.text?.toString()?.isNotEmpty() ?: false

        if (!optionIsValid || !questionIsValid) {
            Snacks.longSnack(
                view = binding.root,
                message = "Some fields have error"
            )
            return
        }

        val originQuestId = quest.questId
        val question = binding.inputEasyQuestion.text.toString()
        val answer = if (binding.optionTrue.isChecked) "True" else "False"
        val challenge = Challenge(
            originQuestId = originQuestId,
            question = question,
            answer = answer
        )

        CoroutineScope(Dispatchers.IO).launch {
            repository.insert(challenge)
        }

        Snacks.longSnack(
            view = binding.root,
            message = "Success"
        )

        dismiss()
    }
}