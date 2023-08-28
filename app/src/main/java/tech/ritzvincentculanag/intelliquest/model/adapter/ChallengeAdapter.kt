package tech.ritzvincentculanag.intelliquest.model.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import tech.ritzvincentculanag.intelliquest.R
import tech.ritzvincentculanag.intelliquest.model.Challenge

class ChallengeAdapter(
    private val cardEvent: ChallengeAdapterEvent
) : RecyclerView.Adapter<ChallengeAdapter.ChallengeViewHolder>() {

    private var challenges: List<Challenge> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChallengeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_challenge, parent, false)

        return ChallengeViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChallengeViewHolder, position: Int) {
        holder.challengeQuestion.text = challenges[position].question
    }

    override fun getItemCount(): Int = challenges.size

    fun setChallenges(challenges: List<Challenge>) {
        this.challenges = challenges
        notifyDataSetChanged()
    }

    inner class ChallengeViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {

        private val card: MaterialCardView = view.findViewById(R.id.challegeCard)
        val challengeQuestion: TextView = view.findViewById(R.id.challengeQuestion)

        init {
            card.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                cardEvent.onItemClick(adapterPosition)
            }
        }
    }

    interface ChallengeAdapterEvent {
        fun onItemClick(position: Int)
    }

}