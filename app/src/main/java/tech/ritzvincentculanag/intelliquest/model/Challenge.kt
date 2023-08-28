package tech.ritzvincentculanag.intelliquest.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "Challenges")
data class Challenge(
    @PrimaryKey(autoGenerate = true)
    var challengeId: Int = 0,
    var originQuestId: Int,

    var question: String,
    var answer: String
) : Parcelable
