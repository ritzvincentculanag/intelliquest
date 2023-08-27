package tech.ritzvincentculanag.intelliquest.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import tech.ritzvincentculanag.intelliquest.databinding.FragmentCampBinding
import tech.ritzvincentculanag.intelliquest.model.QuestState
import tech.ritzvincentculanag.intelliquest.model.adapter.QuestCampAdapter
import tech.ritzvincentculanag.intelliquest.viewmodel.QuestViewModel
import tech.ritzvincentculanag.intelliquest.viewmodel.factory.QuestViewModelFactory


class CampFragment : Fragment() {

    private lateinit var binding: FragmentCampBinding
    private lateinit var viewModel: QuestViewModel
    private lateinit var factory: QuestViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setupFragment()
        setOnClickListeners()
        setupRecyclerView()

        return binding.root
    }

    private fun setupFragment() {
        factory = QuestViewModelFactory(requireActivity().application)
        viewModel = ViewModelProvider(this, factory)[QuestViewModel::class.java]
        binding = FragmentCampBinding.inflate(layoutInflater)
    }

    private fun setupRecyclerView() {
        val adapter = QuestCampAdapter()
        binding.questList.adapter = adapter
        binding.questList.layoutManager = LinearLayoutManager(requireContext())

        CoroutineScope(Dispatchers.Default).launch {
            viewModel.getUserQuests().collect {
                activity?.runOnUiThread {
                    adapter.setQuests(it)
                }
            }
        }
    }

    private fun setOnClickListeners() {
        binding.campCreateQuest.setOnClickListener {
            val goToCreateQuest = CampFragmentDirections.actionCampFragmentToCreateQuest(QuestState())
            findNavController().navigate(goToCreateQuest)
        }
    }

}