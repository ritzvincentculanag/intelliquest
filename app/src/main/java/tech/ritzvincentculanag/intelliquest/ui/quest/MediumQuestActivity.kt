package tech.ritzvincentculanag.intelliquest.ui.quest

import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.navArgs
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import tech.ritzvincentculanag.intelliquest.databinding.ActivityMediumQuestBinding
import tech.ritzvincentculanag.intelliquest.db.AppDatabase
import tech.ritzvincentculanag.intelliquest.model.Challenge
import tech.ritzvincentculanag.intelliquest.model.QuestType
import tech.ritzvincentculanag.intelliquest.model.SCORE_EASY
import tech.ritzvincentculanag.intelliquest.model.SCORE_HARD
import tech.ritzvincentculanag.intelliquest.model.SCORE_MEDIUM
import tech.ritzvincentculanag.intelliquest.repository.ChallengeRepository
import tech.ritzvincentculanag.intelliquest.repository.QuestRepository
import tech.ritzvincentculanag.intelliquest.util.Scorer

class MediumQuestActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMediumQuestBinding
    private lateinit var timer: CountDownTimer

    private val args by navArgs<MediumQuestActivityArgs>()
    private val repository = QuestRepository(AppDatabase.getDatabase(this).questDao())
    private val challengeRepository = ChallengeRepository(AppDatabase.getDatabase(this).challengeDao())
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
        setupLayout()
        resetTimer()
        setupTimer()
        setupQuest()

        setContentView(binding.root)
    }

    private fun setupActivity() {
        binding = ActivityMediumQuestBinding.inflate(layoutInflater)
    }

    private fun setupLayout() {
        scoreIncrement = when (args.quest.questType) {
            QuestType.EASY -> SCORE_EASY
            QuestType.MEDIUM -> SCORE_MEDIUM
            QuestType.HARD -> SCORE_HARD
        }

        if (!args.quest.isTimed) {
            binding.timerMedium.visibility = View.INVISIBLE
            binding.progressBarMedium.visibility = View.INVISIBLE
        }
    }

    private fun resetTimer() {
        seconds = args.quest.timeDuration
        duration = (seconds * 1000).toLong()
        factor = 100 / seconds
        binding.timerMedium.text = seconds.toString()
    }

    private fun setupTimer() {
        timer = object: CountDownTimer(duration, 1000) {
            override fun onTick(timeLeftInMillis: Long) {
                duration = timeLeftInMillis
                binding.timerMedium.text = (timeLeftInMillis / 1000).toString()

                val secondsRemaining = timeLeftInMillis / 1000
                val progressPercentage = (seconds - secondsRemaining) * factor

                binding.progressBarMedium.progress = progressPercentage.toInt()
            }

            override fun onFinish() {
                position += 1
                nextQuest()
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

    private fun nextQuest() {
        if (position >= challenges.size) {
            Scorer.updateScore(this, score)
            finish()
            return
        }

        timer.start()
        val challenge = challenges[position]

        CoroutineScope(Dispatchers.Default).launch {
            challengeRepository.getChallengeOptions().collect {
                val current = it.find { it.challenge.challengeId == challenge.challengeId }
                runOnUiThread {
                    val options = mutableListOf<String>()
                    options.add(challenge.answer)
                    current?.options?.forEach {
                        options.add(it.option)
                    }

                    options.shuffle()

                    binding.questionMedium.text = challenge.question
                    binding.actionOption1.text = options[0]
                    binding.actionOption2.text = options[1]
                    binding.actionOption3.text = options[2]
                    binding.actionOption4.text = options[3]
                }
            }
        }
    }
}