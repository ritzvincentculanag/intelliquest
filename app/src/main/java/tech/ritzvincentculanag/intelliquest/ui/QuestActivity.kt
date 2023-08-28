package tech.ritzvincentculanag.intelliquest.ui

import android.os.Bundle
import android.os.CountDownTimer
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.navArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import tech.ritzvincentculanag.intelliquest.R
import tech.ritzvincentculanag.intelliquest.databinding.ActivityQuestBinding
import tech.ritzvincentculanag.intelliquest.db.AppDatabase
import tech.ritzvincentculanag.intelliquest.model.Challenge
import tech.ritzvincentculanag.intelliquest.repository.QuestRepository

class QuestActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuestBinding
    private lateinit var timer: CountDownTimer

    private val args by navArgs<QuestActivityArgs>()
    private val repository = QuestRepository(AppDatabase.getDatabase(this).questDao())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupLayout()
    }

    private fun setupLayout() {
        CoroutineScope(Dispatchers.Default).launch {
            repository.getQuestChallenges().collect { items ->
                val questChallenge = items.find { it.quest.questId == args.quest.questId }
                val challenges = questChallenge?.challenges!!

                checkIfChallengeIsEmpty(challenges)
            }
        }

        startTimer()
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

    private fun startTimer() {
        val seconds = args.quest.timeDuration
        val factor = 100 / seconds
        val countdownDuration = (args.quest.timeDuration.toLong() * 1000)

        timer = object: CountDownTimer(countdownDuration, 1000) {
            override fun onTick(timeLeftInMillis: Long) {
                binding.textView13.text = (timeLeftInMillis / 1000).toString()

                val secondsRemaining = timeLeftInMillis / 1000
                val progressPercentage = (seconds - secondsRemaining) * factor

                binding.progressBar.progress = progressPercentage.toInt()
            }

            override fun onFinish() {
            }
        }
        timer.start()
    }
}