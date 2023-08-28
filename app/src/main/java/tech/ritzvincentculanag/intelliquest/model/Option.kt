package tech.ritzvincentculanag.intelliquest.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "Options")
data class Option(
    @PrimaryKey(autoGenerate = true)
    var optionId: Int = 0,
    var originChallengeId: Int,

    var option: String
) : Parcelable
