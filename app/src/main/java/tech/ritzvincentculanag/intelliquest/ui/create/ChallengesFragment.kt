package tech.ritzvincentculanag.intelliquest.ui.create

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import tech.ritzvincentculanag.intelliquest.databinding.FragmentChallengesBinding
import tech.ritzvincentculanag.intelliquest.db.AppDatabase
import tech.ritzvincentculanag.intelliquest.model.QuestType
import tech.ritzvincentculanag.intelliquest.model.adapter.ChallengeAdapter
import tech.ritzvincentculanag.intelliquest.repository.QuestRepository

class ChallengesFragment : Fragment() {

    private val args by navArgs<ChallengesFragmentArgs>()

    private lateinit var binding: FragmentChallengesBinding
    private lateinit var repository: QuestRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setupFragment()
        setupRecyclerView()
        setupOnClickListeners()

        return binding.root
    }

    private fun setupFragment() {
        repository = QuestRepository(AppDatabase.getDatabase(requireContext()).questDao())
        binding = FragmentChallengesBinding.inflate(layoutInflater)
    }

    private fun setupRecyclerView() {
        val adapter = ChallengeAdapter()

        binding.challengeList.layoutManager = LinearLayoutManager(requireContext())
        binding.challengeList.adapter = adapter

        CoroutineScope(Dispatchers.Default).launch {
            repository.getQuestChallenges().collect {
                val questChallenges = it.find { item ->
                    item.quest.questId == args.quest.questId
                }
                activity?.runOnUiThread {
                    Log.d("USER_CHALLENGE", questChallenges?.challenges!!.toString())
                    adapter.setChallenges(questChallenges.challenges)
                }
            }
        }
    }

    private fun setupOnClickListeners() {
        binding.actionCreateChallenge.setOnClickListener {
            val createFragment: BottomSheetDialogFragment = when (args.quest.questType) {
                QuestType.EASY -> CreateEasyChallenge(args.quest)
                QuestType.MEDIUM -> CreateMediumChallenge()
                QuestType.HARD -> CreateHardChallenge()
            }

            createFragment.show(activity?.supportFragmentManager!!, "CREATE_QUEST")
        }
    }

}