package tech.ritzvincentculanag.intelliquest

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import tech.ritzvincentculanag.intelliquest.entity.Quest
import tech.ritzvincentculanag.intelliquest.entity.QuestType
import tech.ritzvincentculanag.intelliquest.viewmodel.QuestViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var questViewModel: QuestViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        questViewModel = QuestViewModel(application)
        questViewModel.insert(Quest(
            qid = 0,
            name = "An easy quest",
            description = "An easy description",
            type = QuestType.EASY
        ))
        questViewModel.getQuests().observe(this) { quests ->
            quests.forEach { quest ->
                Log.d("QUEST_TAG", quest.toString())
            }
        }
        questViewModel.insert(Quest(
            qid = 0,
            name = "A medium quest",
            description = "An medium description",
            type = QuestType.EASY
        ))
    }
}