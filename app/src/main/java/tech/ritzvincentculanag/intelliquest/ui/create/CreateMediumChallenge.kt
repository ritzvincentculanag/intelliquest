package tech.ritzvincentculanag.intelliquest.ui.create

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import tech.ritzvincentculanag.intelliquest.databinding.FragmentCreateMediumChallengeBinding
import tech.ritzvincentculanag.intelliquest.db.AppDatabase
import tech.ritzvincentculanag.intelliquest.model.Challenge
import tech.ritzvincentculanag.intelliquest.model.Option
import tech.ritzvincentculanag.intelliquest.model.Quest
import tech.ritzvincentculanag.intelliquest.repository.ChallengeRepository
import tech.ritzvincentculanag.intelliquest.repository.OptionRepository
import tech.ritzvincentculanag.intelliquest.util.Snacks

class CreateMediumChallenge(
    private val quest: Quest,
    private val challenge: Challenge? = null
) : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentCreateMediumChallengeBinding
    private lateinit var repository: ChallengeRepository
    private lateinit var optionRepository: OptionRepository

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
        binding = FragmentCreateMediumChallengeBinding.inflate(layoutInflater)
        repository = ChallengeRepository(AppDatabase.getDatabase(requireContext()).challengeDao())
        optionRepository = OptionRepository(AppDatabase.getDatabase(requireContext()).optionDao())
    }

    private fun setupLayout() {
        if (challenge == null) {
            binding.actionDeleteMedium.visibility = View.INVISIBLE
            return
        }

        CoroutineScope(Dispatchers.Default).launch {
            repository.getChallengeOptions().collect {
                val challengeOptions = it.find { it.challenge.challengeId == challenge.challengeId }
                activity?.runOnUiThread {
                    val options = challengeOptions?.options!!
                    Log.d("OPTIONS_TAG", options.toString())
                    binding.inputAnswerMedium.setText(challenge.answer)
                    binding.inputChoice2.setText(options[0].option)
                    binding.inputChoice3.setText(options[1].option)
                    binding.inputChoice4.setText(options[2].option)
                }
            }
        }

        binding.inputMediumQuestion.setText(challenge.question)
        binding.actionSaveMedium.text = "Update"

    }

    private fun setupOnClickListeners() {
        binding.actionSaveMedium.setOnClickListener {
            if (challenge != null) {
                updateChallenge()
            } else {
                createChallenge()
            }
        }
    }

    private fun updateChallenge() {
        if (!challengeIsValid()) {
            Snacks.shortSnack(
                view = binding.root,
                message = "Some fields have errors"
            )
            return
        }

        val challengeId = challenge?.challengeId!!
        val challenge = Challenge(
            challengeId = challengeId,
            originQuestId = quest.questId,
            question = binding.inputMediumQuestion.text.toString(),
            answer = binding.inputAnswerMedium.text.toString()
        )

        CoroutineScope(Dispatchers.IO).launch {
            repository.update(challenge)
        }

        createOptions(challengeId.toLong())
        dismiss()
    }

    private fun createChallenge() {
        if (!challengeIsValid()) {
            Snacks.shortSnack(
                view = binding.root,
                message = "Some fields have errors"
            )
            return
        }

        var challengeId = 0
        val challenge = Challenge(
            challengeId = challengeId,
            originQuestId = quest.questId,
            question = binding.inputMediumQuestion.text.toString(),
            answer = binding.inputAnswerMedium.text.toString()
        )

        CoroutineScope(Dispatchers.IO).launch {
            repository.insert(challenge).collect {
                activity?.runOnUiThread {
                    challengeId = it.toInt()
                    createOptions(challengeId.toLong())
                }
            }
        }

        dismiss()
    }

    private fun createOptions(challengeId: Long) {
        val optionsList = mutableListOf(
            Option(
                optionId = 0,
                originChallengeId = challengeId.toInt(),
                option = binding.inputChoice2.text.toString()
            ),
            Option(
                optionId = 0,
                originChallengeId = challengeId.toInt(),
                option = binding.inputChoice3.text.toString()
            ),
            Option(
                optionId = 0,
                originChallengeId = challengeId.toInt(),
                option = binding.inputChoice4.text.toString()
            )
        )

        if (challenge != null) {
            CoroutineScope(Dispatchers.IO).launch {
                optionsList.forEach { option: Option ->
                    optionRepository.update(option)
                }
            }
        } else {
            CoroutineScope(Dispatchers.IO).launch {
                optionsList.forEach { option: Option ->
                    optionRepository.insert(option)
                }
            }
        }
    }

    private fun challengeIsValid(): Boolean {
        val optionAnswer = binding.inputAnswerMedium.text?.isNotEmpty()!!
        val option2IsValid = binding.inputChoice2.text?.isNotEmpty()!!
        val option3IsValid = binding.inputChoice3.text?.isNotEmpty()!!
        val option4IsValid = binding.inputChoice4.text?.isNotEmpty()!!
        val challengeIsValid = optionAnswer ||
                option2IsValid ||
                option3IsValid ||
                option4IsValid

        if (!challengeIsValid) {
            Snacks.longSnack(
                view = binding.root,
                message = "Some fields have error"
            )
        }

        return challengeIsValid
    }
}