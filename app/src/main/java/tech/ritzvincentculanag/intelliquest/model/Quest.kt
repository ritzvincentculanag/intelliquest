package tech.ritzvincentculanag.intelliquest.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "Quests")
data class Quest(
    @PrimaryKey(autoGenerate = true)
    var questId: Int,
    var originUserId: Int,

    var name: String,
    var description: String,
    var questType: QuestType,
    var isFinished: Boolean = false,
    var isPublic: Boolean = true
) : Parcelable
