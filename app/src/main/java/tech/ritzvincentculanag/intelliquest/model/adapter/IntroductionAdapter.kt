package tech.ritzvincentculanag.intelliquest.model.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import tech.ritzvincentculanag.intelliquest.R
import tech.ritzvincentculanag.intelliquest.model.Introduction

class IntroductionAdapter :  RecyclerView.Adapter<IntroductionAdapter.IntroductionViewHolder>() {

    private val items = mutableListOf<Introduction>()

    init {
        items.add(Introduction(
            title = "Learn while enjoying",
            description = "Discover and learn new things along the way by" +
                    "playing interactive and fun games and quizzes.",
            cover = R.drawable.viewpager_learn
        ))
        items.add(Introduction(
            title = "Go on a quest",
            description = "Embark on a journey of self learning and go on" +
                    "quests, or even better make you own quests!",
            cover = R.drawable.viewpager_quest
        ))
        items.add(
            Introduction(
            title = "Made with Love",
            description = "An open-source project made with love with the" +
                    "goal of learning and contributing to the community.",
            cover = R.drawable.viewpager_love
            )
        )
    }

    inner class IntroductionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val pagerTitle: TextView = view.findViewById(R.id.pagerTitle)
        val pagerDescription: TextView = view.findViewById(R.id.pagerDescription)
        val pagerCover: ImageView = view.findViewById(R.id.pagerCover)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IntroductionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.viewpager_intro, parent, false)

        return IntroductionViewHolder(view)
    }

    override fun onBindViewHolder(holder: IntroductionViewHolder, position: Int) {
        val item = items[position]
        holder.pagerTitle.text = item.title
        holder.pagerDescription.text = item.description
        holder.pagerCover.setImageResource(item.cover)
    }

    override fun getItemCount(): Int = items.size

}