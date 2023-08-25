package tech.ritzvincentculanag.intelliquest.model.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import tech.ritzvincentculanag.intelliquest.R
import tech.ritzvincentculanag.intelliquest.model.User

class RankAdapter : RecyclerView.Adapter<RankAdapter.RankViewHolder>() {

    private var users: List<User> = mutableListOf()

    inner class RankViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val rankPosition: TextView = view.findViewById(R.id.rankPosition)
        val rankUsername: TextView = view.findViewById(R.id.rankUsername)
        val rankScore: TextView = view.findViewById(R.id.rankScore)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RankViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_ranks, parent, false)

        return RankViewHolder(view)
    }

    override fun onBindViewHolder(holder: RankViewHolder, position: Int) {
        val user = users[position]
        val rank = (position + 1).toString()

        holder.rankPosition.text = rank
        holder.rankScore.text = user.score.toString()
        holder.rankUsername.text = user.username
    }

    override fun getItemCount(): Int = users.size

    fun setUsers(users: List<User>) {
        this.users = users
        notifyDataSetChanged()
    }

}