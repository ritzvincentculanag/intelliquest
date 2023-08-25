package tech.ritzvincentculanag.intelliquest.model.relationship

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Relation
import kotlinx.parcelize.Parcelize
import tech.ritzvincentculanag.intelliquest.model.Quest
import tech.ritzvincentculanag.intelliquest.model.User

@Parcelize
data class UserQuests (
    @Embedded
    var user: User,

    @Relation(parentColumn = "userId", entityColumn = "originUserId")
    var quests: List<Quest>
) : Parcelable