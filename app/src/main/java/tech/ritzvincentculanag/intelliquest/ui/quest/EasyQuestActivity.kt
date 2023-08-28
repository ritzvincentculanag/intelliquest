package tech.ritzvincentculanag.intelliquest.ui.quest

import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.navArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import tech.ritzvincentculanag.intelliquest.R
import tech.ritzvincentculanag.intelliquest.databinding.ActivityEasyQuestBinding
import tech.ritzvincentculanag.intelliquest.db.AppDatabase
import tech.ritzvincentculanag.intelliquest.model.Challenge
import tech.ritzvincentculanag.intelliquest.model.QuestType
import tech.ritzvincentculanag.intelliquest.model.SCORE_EASY
import tech.ritzvincentculanag.intelliquest.model.SCORE_HARD
import tech.ritzvincentculanag.intelliquest.model.SCORE_MEDIUM
import tech.ritzvincentculanag.intelliquest.repository.QuestRepository
import tech.ritzvincentculanag.intelliquest.util.Scorer

class EasyQuestActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityEasyQuestBinding
    private lateinit var timer: CountDownTimer

    private val args by navArgs<EasyQuestActivityArgs>()
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
        binding = ActivityEasyQuestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        resetTimer()
        setupTimer()
        setupLayout()
        setupQuest()
        registerOnBackPressed()
    }

    override fun onClick(p0: View?) {
        if (position >= challenges.size) {
            Scorer.updateScore(this, score)
            finish()
            return
        }

        val challenge = challenges[position]
        val buttonSelected = findViewById<Button>(p0?.id!!)
        if (buttonSelected.text == challenge.answer) {
            Toast.makeText(this, "Correct", Toast.LENGTH_LONG).show()
            score += scoreIncrement
        } else {
            Toast.makeText(this, "Wrong!", Toast.LENGTH_LONG).show()
        }

        position += 1
        timer.cancel()
        resetTimer()
        nextQuest()
    }

    private fun setupLayout() {
        scoreIncrement = when (args.quest.questType) {
            QuestType.EASY -> SCORE_EASY
            QuestType.MEDIUM -> SCORE_MEDIUM
            QuestType.HARD -> SCORE_HARD
        }

        if (!args.quest.isTimed) {
            binding.timerEasy.visibility = View.INVISIBLE
            binding.progressBarEasy.visibility = View.INVISIBLE
        }

        CoroutineScope(Dispatchers.Default).launch {
            repository.getQuestChallenges().collect { items ->
                val questChallenge = items.find { it.quest.questId == args.quest.questId }
                val challenges = questChallenge?.challenges!!

                checkIfChallengeIsEmpty(challenges)
            }
        }
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

    private fun setupTimer() {
        timer = object: CountDownTimer(duration, 1000) {
            override fun onTick(timeLeftInMillis: Long) {
                duration = timeLeftInMillis
                binding.timerEasy.text = (timeLeftInMillis / 1000).toString()

                val secondsRemaining = timeLeftInMillis / 1000
                val progressPercentage = (seconds - secondsRemaining) * factor

                binding.progressBarEasy.progress = progressPercentage.toInt()
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
        binding.timerEasy.text = seconds.toString()
    }

    private fun nextQuest() {
        if (position >= challenges.size) {
            Scorer.updateScore(this, score)
            finish()
            return
        }

        timer.start()
        val quest = challenges[position]

        binding.questionEasy.text = quest.question
        binding.actionTrue.setOnClickListener(this)
        binding.actionFalse.setOnClickListener(this)
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

    private fun registerOnBackPressed() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                timer.cancel()

                MaterialAlertDialogBuilder(this@EasyQuestActivity)
                    .setTitle("Exit quest")
                    .setMessage("All progress will be lost and score will not be recorded.")
                    .setPositiveButton("Go back") { _, _ ->
                        finish()
                    }
                    .setNegativeButton("Continue") { _, _ ->
                        timer.start()
                    }
                    .show()
            }
        })
    }
}