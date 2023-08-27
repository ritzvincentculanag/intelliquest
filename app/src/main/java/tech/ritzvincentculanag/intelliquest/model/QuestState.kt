package tech.ritzvincentculanag.intelliquest.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class QuestState(
    val isCreating: Boolean = true
) : Parcelable
