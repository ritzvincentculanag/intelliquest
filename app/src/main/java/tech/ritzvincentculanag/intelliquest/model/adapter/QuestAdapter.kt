package tech.ritzvincentculanag.intelliquest.model.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import tech.ritzvincentculanag.intelliquest.R
import tech.ritzvincentculanag.intelliquest.model.Quest

class QuestAdapter : RecyclerView.Adapter<QuestAdapter.QuestViewHolder>() {

    private var quests: List<Quest> = mutableListOf()

    inner class QuestViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val questTitle: TextView = view.findViewById(R.id.questTitle)
        val questDescription: TextView = view.findViewById(R.id.questDescription)
        val questScore: TextView = view.findViewById(R.id.questScore)
        val questStart: TextView = view.findViewById(R.id.questStart)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_quest, parent, false)
        return QuestViewHolder(view)
    }

    override fun onBindViewHolder(holder: QuestViewHolder, position: Int) {
        val quest = quests[position]

        holder.questTitle.text = quest.name
        holder.questDescription.text = quest.description
    }

    override fun getItemCount(): Int = quests.size

    fun setQuests(quests: List<Quest>) {
        this.quests = quests
        notifyDataSetChanged()
    }

}