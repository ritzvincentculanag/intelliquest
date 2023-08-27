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
import tech.ritzvincentculanag.intelliquest.databinding.FragmentRankBinding
import tech.ritzvincentculanag.intelliquest.model.adapter.RankAdapter
import tech.ritzvincentculanag.intelliquest.viewmodel.RankViewModel
import tech.ritzvincentculanag.intelliquest.viewmodel.factory.RankViewModelFactory

class RankFragment : Fragment() {

    private lateinit var binding: FragmentRankBinding
    private lateinit var viewModel: RankViewModel
    private lateinit var factory: RankViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        setupFragment()
        setupRecyclerView()

        return binding.root
    }

    private fun setupFragment() {
        factory = RankViewModelFactory(requireActivity().application)
        viewModel = ViewModelProvider(this, factory)[RankViewModel::class.java]
        binding = FragmentRankBinding.inflate(layoutInflater)
    }

    private fun setupRecyclerView() {
        val users = viewModel.getUsers()
        val adapter = RankAdapter()
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        CoroutineScope(Dispatchers.Main).launch {
            users.collect { users ->
                adapter.setUsers(users)
            }
        }
    }

}