package tech.ritzvincentculanag.intelliquest.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import tech.ritzvincentculanag.intelliquest.R
import tech.ritzvincentculanag.intelliquest.databinding.FragmentCampBinding


class CampFragment : Fragment() {

    private lateinit var binding: FragmentCampBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setupFragment()
        setOnClickListeners()

        return binding.root
    }

    private fun setupFragment() {
        binding = FragmentCampBinding.inflate(layoutInflater)
    }

    private fun setOnClickListeners() {
        binding.campCreateQuest.setOnClickListener {
            findNavController().navigate(R.id.action_campFragment_to_createQuest)
        }
    }

}