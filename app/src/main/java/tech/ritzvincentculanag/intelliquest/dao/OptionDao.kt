package tech.ritzvincentculanag.intelliquest.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Update
import tech.ritzvincentculanag.intelliquest.model.Option

@Dao
interface OptionDao {

    @Insert
    suspend fun insert(option: Option)

    @Update
    suspend fun update(option: Option)

}