package tech.ritzvincentculanag.intelliquest.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import tech.ritzvincentculanag.intelliquest.databinding.FragmentQuestBinding
import tech.ritzvincentculanag.intelliquest.model.Quest
import tech.ritzvincentculanag.intelliquest.model.QuestType
import tech.ritzvincentculanag.intelliquest.model.adapter.QuestAdapter
import tech.ritzvincentculanag.intelliquest.util.Snacks
import tech.ritzvincentculanag.intelliquest.viewmodel.QuestViewModel
import tech.ritzvincentculanag.intelliquest.viewmodel.factory.QuestViewModelFactory

class QuestFragment : Fragment(), QuestAdapter.QuestInterface {

    private lateinit var binding: FragmentQuestBinding
    private lateinit var viewModel: QuestViewModel
    private lateinit var factory: QuestViewModelFactory

    private val quests = mutableListOf<Quest>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        setupFragment()
        setupRecyclerView()

        return binding.root
    }

    override fun onStartClick(position: Int) {
        val quest = quests[position]
        val direction = when (quest.questType) {
            QuestType.EASY -> QuestFragmentDirections.actionQuestFragmentToQuestActivity(quest)
            QuestType.MEDIUM -> QuestFragmentDirections.actionQuestFragmentToHardQuestActivity(quest)
            QuestType.HARD -> QuestFragmentDirections.actionQuestFragmentToHardQuestActivity(quest)
        }

        CoroutineScope(Dispatchers.Default).launch {
            viewModel.getQuestChallenges().collect {
                val items = it.find { it.quest.questId == quest.questId }
                activity?.runOnUiThread {
                    if (items?.challenges?.isNotEmpty()!!) {
                        findNavController().navigate(direction)
                        return@runOnUiThread
                    }

                    MaterialAlertDialogBuilder(requireContext())
                        .setTitle("Warning")
                        .setMessage("Can't proceed to quest because quest is empty")
                        .setNeutralButton("Sad naman ng beshy ko") { _, _ ->
                            Snacks.shortSnack(
                                view = binding.root,
                                message = "Aborting quest"
                            )
                        }
                        .show()
                }
            }
        }
    }

    private fun setupFragment() {
        factory = QuestViewModelFactory(requireActivity().application)
        viewModel = ViewModelProvider(this, factory)[QuestViewModel::class.java]
        binding = FragmentQuestBinding.inflate(layoutInflater)
    }

    private fun setupRecyclerView() {
        val adapter = QuestAdapter(this)

        binding.questList.adapter = adapter
        binding.questList.layoutManager = LinearLayoutManager(requireContext())

        CoroutineScope(Dispatchers.Default).launch {
            viewModel.getQuests().collect {
                activity?.runOnUiThread {
                    if (it.isEmpty()) {
                        return@runOnUiThread
                    }

                    quests.clear()
                    quests.addAll(it)
                    adapter.setQuests(quests)
                }
            }
        }
    }

}