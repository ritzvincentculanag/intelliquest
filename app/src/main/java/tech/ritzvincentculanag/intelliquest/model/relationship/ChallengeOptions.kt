package tech.ritzvincentculanag.intelliquest.model.relationship

import androidx.room.Embedded
import androidx.room.Relation
import tech.ritzvincentculanag.intelliquest.model.Challenge
import tech.ritzvincentculanag.intelliquest.model.Option


data class ChallengeOptions(
    @Embedded
    var challenge: Challenge,

    @Relation(parentColumn = "challengeId", entityColumn = "originChallengeId")
    var options: List<Option>
)
