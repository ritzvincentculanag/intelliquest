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

class CreateEasyChallenge(
    private val quest: Quest,
    private val challenge: Challenge? = null
) : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentCreateEasyChallengeBinding
    private lateinit var repository: ChallengeRepository


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setupFragment()
        setupLayout()
        setupOnClickListeners()

        return binding.root
    }

    private fun setupFragment() {
        repository = ChallengeRepository(AppDatabase.getDatabase(requireContext()).challengeDao())
        binding = FragmentCreateEasyChallengeBinding.inflate(layoutInflater)
    }

    private fun setupLayout() {
        if (challenge == null) {
            binding.actionDeleteEasyChallenge.visibility = View.INVISIBLE
            return
        }

        binding.inputEasyQuestion.setText(challenge.question)
        binding.actionSaveEasyQuestion.text = "Update"
        if (challenge.answer == "True") {
            binding.optionTrue.isChecked = true
        } else {
            binding.optionFalse.isChecked = true
        }
    }

    private fun setupOnClickListeners() {
        binding.actionSaveEasyQuestion.setOnClickListener {
            if (challenge != null) {
                updateChallenge()
            } else {
                createChallenge()
            }
        }
        binding.actionDeleteEasyChallenge.setOnClickListener {
            val challenge = getChallenge()
            challenge.challengeId = this.challenge?.challengeId!!

            CoroutineScope(Dispatchers.IO).launch {
                repository.delete(challenge)
            }

            dismiss()
        }
    }

    private fun createChallenge() {
        if (challengeIsValid()) {
            CoroutineScope(Dispatchers.IO).launch {
                repository.insert(getChallenge())
            }

            dismiss()
        }
    }

    private fun updateChallenge() {
        val challenge = getChallenge()
        challenge.challengeId = this.challenge?.challengeId!!

        if (challengeIsValid()) {
            CoroutineScope(Dispatchers.IO).launch {
                repository.update(challenge)
            }

            dismiss()
        }
    }

    private fun getChallenge(): Challenge {
        val originQuestId = quest.questId
        val question = binding.inputEasyQuestion.text.toString()
        val answer = if (binding.optionTrue.isChecked) "True" else "False"

        return Challenge(
            originQuestId = originQuestId,
            question = question,
            answer = answer
        )
    }

    private fun challengeIsValid(): Boolean {
        val optionIsValid = binding.radioGroup.checkedRadioButtonId != -1
        val questionIsValid = binding.inputEasyQuestion.text?.toString()?.isNotEmpty() ?: false
        val challengeIsValid = optionIsValid && questionIsValid

        if (!challengeIsValid) {
            Snacks.longSnack(
                view = binding.root,
                message = "Some fields have error"
            )
        }

        return challengeIsValid
    }
}