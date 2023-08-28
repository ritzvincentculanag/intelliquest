package tech.ritzvincentculanag.intelliquest.ui.create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import tech.ritzvincentculanag.intelliquest.databinding.FragmentChallengesBinding
import tech.ritzvincentculanag.intelliquest.model.QuestType

class ChallengesFragment : Fragment() {

    private val args by navArgs<ChallengesFragmentArgs>()

    private lateinit var binding: FragmentChallengesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setupFragment()
        setupOnClickListeners()

        return binding.root
    }

    private fun setupFragment() {
        binding = FragmentChallengesBinding.inflate(layoutInflater)
    }

    private fun setupOnClickListeners() {
        binding.actionCreateChallenge.setOnClickListener {
            val questType = args.quest.questType
            val createFragment: BottomSheetDialogFragment = when (questType) {
                QuestType.EASY -> CreateEasyChallenge()
                QuestType.MEDIUM -> CreateMediumChallenge()
                QuestType.HARD -> CreateHardChallenge()
                else -> {
                    throw IllegalArgumentException("Invalid quest type")
                }
            }

            createFragment.show(activity?.supportFragmentManager!!, "CREATE_QUEST")
        }
    }

}