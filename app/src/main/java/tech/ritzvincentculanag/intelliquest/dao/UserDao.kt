package tech.ritzvincentculanag.intelliquest.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import tech.ritzvincentculanag.intelliquest.model.User

@Dao
interface UserDao {

    @Insert
    suspend fun insert(user: User)

    @Update
    suspend fun update(user: User)

    @Delete
    suspend fun delete(user: User)

    @Query("SELECT * FROM Users WHERE username = :username AND PASSWORD = :password")
    suspend fun getUser(username: String, password: String): User

    @Query("SELECT * FROM Users WHERE userId = :userId")
    suspend fun getUser(userId: Int): User

    @Query("SELECT * FROM Users")
    fun getUsers(): Flow<List<User>>

}