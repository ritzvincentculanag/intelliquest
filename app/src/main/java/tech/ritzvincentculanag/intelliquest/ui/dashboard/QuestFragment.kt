package tech.ritzvincentculanag.intelliquest.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import tech.ritzvincentculanag.intelliquest.databinding.FragmentQuestBinding
import tech.ritzvincentculanag.intelliquest.factory.QuestViewModelFactory
import tech.ritzvincentculanag.intelliquest.model.adapter.QuestAdapter
import tech.ritzvincentculanag.intelliquest.util.SessionManager
import tech.ritzvincentculanag.intelliquest.viewmodel.QuestViewModel

class QuestFragment : Fragment() {

    private lateinit var binding: FragmentQuestBinding
    private lateinit var viewModel: QuestViewModel
    private lateinit var factory: QuestViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        setupFragment()
        setupRecyclerView()

        return binding.root
    }

    private fun setupFragment() {
        factory = QuestViewModelFactory(requireActivity().application)
        viewModel = ViewModelProvider(this, factory)[QuestViewModel::class.java]
        binding = FragmentQuestBinding.inflate(layoutInflater)
    }

    private fun setupRecyclerView() {
        val adapter = QuestAdapter()
        val session = SessionManager(requireContext())
        val userId = session.getInt("USER_ID")

        CoroutineScope(Dispatchers.Default).launch {
            viewModel.getUserQuests(userId).collect {
                adapter.setQuests(it)
            }
        }
        binding.questList.adapter = adapter
        binding.questList.layoutManager = LinearLayoutManager(requireContext())
    }

}