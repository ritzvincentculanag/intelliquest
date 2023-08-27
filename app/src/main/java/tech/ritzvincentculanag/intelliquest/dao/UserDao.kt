package tech.ritzvincentculanag.intelliquest.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import tech.ritzvincentculanag.intelliquest.model.User
import tech.ritzvincentculanag.intelliquest.model.relationship.UserQuests

@Dao
interface UserDao {

    @Insert
    suspend fun insert(user: User)

    @Update
    suspend fun update(user: User)

    @Delete
    suspend fun delete(user: User)

    @Query("SELECT * FROM Users WHERE username = :username AND PASSWORD = :password")
    fun getUser(username: String, password: String): Flow<User>

    @Query("SELECT * FROM Users WHERE userId = :userId")
    fun getUser(userId: Int): Flow<User>

    @Query("SELECT * FROM Users")
    fun getUsers(): Flow<List<User>>

    @Transaction
    @Query("SELECT * FROM Users")
    fun getUserQuests(): Flow<List<UserQuests>>

}