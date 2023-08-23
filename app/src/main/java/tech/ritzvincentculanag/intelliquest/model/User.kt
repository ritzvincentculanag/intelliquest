package tech.ritzvincentculanag.intelliquest.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Users")
data class User(
    @PrimaryKey
    var userId: Int,

    var firstName: String,
    var lastName: String,
    var middleName: String,
    var username: String,
    var password: String,
    var score: Int
)
