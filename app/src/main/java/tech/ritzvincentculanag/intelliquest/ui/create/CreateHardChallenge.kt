package tech.ritzvincentculanag.intelliquest.ui.create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import tech.ritzvincentculanag.intelliquest.databinding.FragmentCreateHardChallengeBinding
import tech.ritzvincentculanag.intelliquest.db.AppDatabase
import tech.ritzvincentculanag.intelliquest.model.Challenge
import tech.ritzvincentculanag.intelliquest.model.Quest
import tech.ritzvincentculanag.intelliquest.repository.ChallengeRepository
import tech.ritzvincentculanag.intelliquest.util.Snacks

class CreateHardChallenge(
    private val quest: Quest,
    private val challenge: Challenge?
) : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentCreateHardChallengeBinding
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
        binding = FragmentCreateHardChallengeBinding.inflate(layoutInflater)
        repository = ChallengeRepository(AppDatabase.getDatabase(requireContext()).challengeDao())
    }

    private fun setupOnClickListeners() {
        binding.actionSaveHard.setOnClickListener {
            createChallenge()
        }
    }

    private fun createChallenge() {
        val questionIsValid = binding.inputHardQuestion.text.toString().isNotEmpty()
        val answerIsValid = binding.inputHardAnswer.text.toString().isNotEmpty()

        if (!questionIsValid && !answerIsValid) {
            Snacks.longSnack(
                view = binding.root,
                message = "Some fields have invalid values"
            )
        }

        var challengeId = 0
        val challenge = Challenge(
            challengeId = challengeId,
            originQuestId = quest.questId,
            question = binding.inputHardQuestion.text.toString(),
            answer = binding.inputHardAnswer.text.toString()
        )

        CoroutineScope(Dispatchers.IO).launch {
            repository.insert(challenge).collect {
                activity?.runOnUiThread {
                    challengeId = it.toInt()
                }
            }
        }

        dismiss()
    }

}