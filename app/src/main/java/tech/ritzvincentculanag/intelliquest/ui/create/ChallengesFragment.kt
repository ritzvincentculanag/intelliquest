package tech.ritzvincentculanag.intelliquest.ui.create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import tech.ritzvincentculanag.intelliquest.databinding.FragmentChallengesBinding
import tech.ritzvincentculanag.intelliquest.db.AppDatabase
import tech.ritzvincentculanag.intelliquest.model.Challenge
import tech.ritzvincentculanag.intelliquest.model.QuestType
import tech.ritzvincentculanag.intelliquest.model.adapter.ChallengeAdapter
import tech.ritzvincentculanag.intelliquest.repository.QuestRepository

class ChallengesFragment : Fragment(), ChallengeAdapter.ChallengeAdapterEvent{

    private val args by navArgs<ChallengesFragmentArgs>()
    private val challenges = mutableListOf<Challenge>()

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

    override fun onItemClick(position: Int) {
        val challenge = challenges[position]
        val dialog = getDialog(challenge)

        dialog.show(activity?.supportFragmentManager!!, "UPDATE_CHALLENGE")
    }

    private fun setupFragment() {
        repository = QuestRepository(AppDatabase.getDatabase(requireContext()).questDao())
        binding = FragmentChallengesBinding.inflate(layoutInflater)
    }

    private fun setupRecyclerView() {
        val adapter = ChallengeAdapter(this)

        binding.challengeList.layoutManager = LinearLayoutManager(requireContext())
        binding.challengeList.adapter = adapter

        CoroutineScope(Dispatchers.Default).launch {
            repository.getQuestChallenges().collect {
                val questChallenges = it.find { item ->
                    item.quest.questId == args.quest.questId
                }
                activity?.runOnUiThread {
                    if (questChallenges?.challenges?.isNotEmpty()!!) {
                        binding.noChallengeCover.visibility = View.INVISIBLE
                        binding.noChallengeLabel.visibility = View.INVISIBLE
                    } else {
                        binding.noChallengeCover.visibility = View.VISIBLE
                        binding.noChallengeLabel.visibility = View.VISIBLE
                    }

                    challenges.clear()
                    challenges.addAll(questChallenges.challenges)
                    adapter.setChallenges(challenges)
                }
            }
        }
    }

    private fun setupOnClickListeners() {
        binding.actionCreateChallenge.setOnClickListener {
            val createFragment: BottomSheetDialogFragment = getDialog()
            val type = args.quest.questType
            createFragment.show(activity?.supportFragmentManager!!, "CREATE_QUEST")
        }
        binding.materialToolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun getDialog(challenge: Challenge? = null): BottomSheetDialogFragment {
        return when (args.quest.questType) {
            QuestType.HARD -> CreateHardChallenge(args.quest, challenge)
            QuestType.MEDIUM -> CreateMediumChallenge(args.quest, challenge)
            QuestType.EASY -> CreateEasyChallenge(args.quest, challenge)
        }
    }

}