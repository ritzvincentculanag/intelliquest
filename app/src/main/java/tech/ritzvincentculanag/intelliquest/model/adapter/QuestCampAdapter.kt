package tech.ritzvincentculanag.intelliquest.model.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import tech.ritzvincentculanag.intelliquest.R
import tech.ritzvincentculanag.intelliquest.model.Quest

class QuestCampAdapter : RecyclerView.Adapter<QuestCampAdapter.QuestCampViewHolder>() {

    private var quests: List<Quest> = mutableListOf()

    inner class QuestCampViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val campTitle: TextView = view.findViewById(R.id.campTitle)
        val campDescription: TextView = view.findViewById(R.id.campDescription)
        val campEdit: Button = view.findViewById(R.id.campEdit)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestCampViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_quest_camp, parent, false)
        return QuestCampViewHolder(view)
    }

    override fun onBindViewHolder(holder: QuestCampViewHolder, position: Int) {
        val quest = quests[position]

        holder.campTitle.text = quest.name
        holder.campDescription.text = quest.description
    }

    override fun getItemCount(): Int = quests.size

    fun setQuests(quests: List<Quest>) {
        this.quests = quests
        notifyDataSetChanged()
    }

}