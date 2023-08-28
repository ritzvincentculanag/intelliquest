package tech.ritzvincentculanag.intelliquest.model.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import tech.ritzvincentculanag.intelliquest.R
import tech.ritzvincentculanag.intelliquest.model.Challenge

class ChallengeAdapter : RecyclerView.Adapter<ChallengeAdapter.ChallengeViewHolder>()  {

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

    inner class ChallengeViewHolder(view: View) :  RecyclerView.ViewHolder(view) {
        val challengeQuestion: TextView = view.findViewById(R.id.challengeQuestion)
    }

}