package tech.ritzvincentculanag.intelliquest.model.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import tech.ritzvincentculanag.intelliquest.R
import tech.ritzvincentculanag.intelliquest.model.Quest

class QuestAdapter(
    private val listener: QuestInterface
) : RecyclerView.Adapter<QuestAdapter.QuestViewHolder>() {

    private var quests: List<Quest> = mutableListOf()

    inner class QuestViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        private val questStart: TextView = view.findViewById(R.id.questStart)

        val questTitle: TextView = view.findViewById(R.id.questTitle)
        val questDescription: TextView = view.findViewById(R.id.questDescription)
        val questScore: TextView = view.findViewById(R.id.questScore)
        val questTimer: TextView = view.findViewById(R.id.questTimer)
        val questTimerIcon: ImageView = view.findViewById(R.id.questTimerIcon)

        init {
            questStart.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onStartClick(position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_quest, parent, false)
        return QuestViewHolder(view)
    }

    override fun onBindViewHolder(holder: QuestViewHolder, position: Int) {
        val quest = quests[position]

        if (!quest.isTimed) {
            holder.questTimerIcon.visibility = View.INVISIBLE
            holder.questTimer.visibility = View.INVISIBLE
        }

        holder.questTitle.text = quest.name
        holder.questDescription.text = quest.description
        holder.questTimer.text = quest.timeDuration.toString()
    }

    override fun getItemCount(): Int = quests.size

    fun setQuests(quests: List<Quest>) {
        this.quests = quests
        notifyDataSetChanged()
    }

    interface QuestInterface {
        fun onStartClick(position: Int)
    }

}