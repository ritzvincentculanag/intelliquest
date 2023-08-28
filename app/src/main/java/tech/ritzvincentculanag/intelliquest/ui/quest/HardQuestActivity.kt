package tech.ritzvincentculanag.intelliquest.ui.quest

import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.navArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import tech.ritzvincentculanag.intelliquest.R
import tech.ritzvincentculanag.intelliquest.databinding.ActivityHardQuestBinding
import tech.ritzvincentculanag.intelliquest.db.AppDatabase
import tech.ritzvincentculanag.intelliquest.model.Challenge
import tech.ritzvincentculanag.intelliquest.model.QuestType
import tech.ritzvincentculanag.intelliquest.model.SCORE_EASY
import tech.ritzvincentculanag.intelliquest.model.SCORE_HARD
import tech.ritzvincentculanag.intelliquest.model.SCORE_MEDIUM
import tech.ritzvincentculanag.intelliquest.repository.QuestRepository
import tech.ritzvincentculanag.intelliquest.util.Scorer
import tech.ritzvincentculanag.intelliquest.util.Snacks

class HardQuestActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityHardQuestBinding
    private lateinit var timer: CountDownTimer

    private val args by navArgs<HardQuestActivityArgs>()
    private val repository = QuestRepository(AppDatabase.getDatabase(this).questDao())
    private val challenges = mutableListOf<Challenge>()

    private var position = 0
    private var score = 0
    private var scoreIncrement = 0

    private var seconds = 0
    private var duration = 0L
    private var factor = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupActivity()
        setContentView(binding.root)

        setupTimer()
        setupLayout()
        setupQuest()
        setupOnClickListeners()
    }

    override fun onClick(p0: View?) {
        if (position >= challenges.size) {
            Scorer.updateScore(this, score)
            finish()
            return
        }

        val answerIsValid = binding.inputAnswerHard.text.toString().isNotEmpty()
        val answer = binding.inputAnswerHard.text.toString()
        val challenge = challenges[position]

        if (!answerIsValid) {
            Snacks.shortSnack(
                view = binding.root,
                message = "Please provide an answer"
            )
            return
        }

        if (answer.contentEquals(challenge.answer)) {
            score += scoreIncrement
        }

        position += 1
        timer.cancel()
        resetTimer()
        nextQuest()
    }

    private fun setupActivity() {
        binding = ActivityHardQuestBinding.inflate(layoutInflater)
    }

    private fun setupTimer() {
        resetTimer()
        timer = object: CountDownTimer(duration, 1000) {
            override fun onTick(timeLeftInMillis: Long) {
                duration = timeLeftInMillis
                binding.timerHard.text = (timeLeftInMillis / 1000).toString()

                val secondsRemaining = timeLeftInMillis / 1000
                val progressPercentage = (seconds - secondsRemaining) * factor

                binding.progressBarHard.progress = progressPercentage.toInt()
            }

            override fun onFinish() {
                position += 1
                nextQuest()
            }
        }
    }

    private fun resetTimer() {
        seconds = args.quest.timeDuration
        duration = (seconds * 1000).toLong()
        factor = 100 / seconds
        binding.timerHard.text = seconds.toString()
    }

    private fun setupLayout() {
        scoreIncrement = when (args.quest.questType) {
            QuestType.EASY -> SCORE_EASY
            QuestType.MEDIUM -> SCORE_MEDIUM
            QuestType.HARD -> SCORE_HARD
        }

        if (!args.quest.isTimed) {
            binding.timerHard.visibility = View.INVISIBLE
            binding.progressBarHard.visibility = View.INVISIBLE
        }

        CoroutineScope(Dispatchers.Default).launch {
            repository.getQuestChallenges().collect { items ->
                val questChallenge = items.find { it.quest.questId == args.quest.questId }
                val challenges = questChallenge?.challenges!!

                checkIfChallengeIsEmpty(challenges)
            }
        }
    }

    private fun setupOnClickListeners() {
        binding.actionSubmit.setOnClickListener(this)
    }

    private fun setupQuest() {
        CoroutineScope(Dispatchers.Default).launch {
            repository.getQuestChallenges().collect {
                challenges.addAll(it.find {
                    it.quest.questId == args.quest.questId
                }?.challenges!!)

                runOnUiThread {
                    challenges.shuffle()
                    nextQuest()
                }
            }
        }

        timer.start()
    }

    private fun nextQuest() {
        if (position >= challenges.size) {
            Scorer.updateScore(this, score)
            finish()
            return
        }

        timer.start()
        val quest = challenges[position]

        binding.questionHard.text = quest.question
    }

    private fun checkIfChallengeIsEmpty(challenges: List<Challenge>) {
        if (challenges.isNotEmpty()) {
            return
        }

        MaterialAlertDialogBuilder(this)
            .setTitle("Quest error")
            .setMessage("Quest is empty! Consider adding items to it.")
            .setNeutralButton("Go back") { _, _ ->
                findNavController(R.id.navigationDashboard).navigateUp()
            }
    }

}