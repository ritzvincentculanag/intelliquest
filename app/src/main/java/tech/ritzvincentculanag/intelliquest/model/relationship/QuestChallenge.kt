package tech.ritzvincentculanag.intelliquest.model.relationship

import androidx.room.Embedded
import androidx.room.Relation
import tech.ritzvincentculanag.intelliquest.model.Challenge
import tech.ritzvincentculanag.intelliquest.model.Quest

data class QuestChallenge(
    @Embedded
    var quest: Quest,

    @Relation(parentColumn = "questId", entityColumn = "originQuestId")
    var challenges: List<Challenge>
)