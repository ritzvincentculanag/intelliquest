package tech.ritzvincentculanag.intelliquest.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "Users")
data class User(
    @PrimaryKey(autoGenerate = true)
    var userId: Int = 0,
    var score: Int = 0,

    var firstName: String,
    var lastName: String,
    var middleName: String,
    var username: String,
    var password: String
) : Parcelable
