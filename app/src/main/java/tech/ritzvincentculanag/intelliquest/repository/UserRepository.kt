package tech.ritzvincentculanag.intelliquest.repository

import kotlinx.coroutines.flow.Flow
import tech.ritzvincentculanag.intelliquest.dao.UserDao
import tech.ritzvincentculanag.intelliquest.model.User

class UserRepository(private val userDao: UserDao) {

    private val users: Flow<List<User>> = userDao.getUsers()

    suspend fun insert(user: User) {
        userDao.insert(user)
    }

    suspend fun update(user: User) {
        userDao.update(user)
    }

    suspend fun delete(user: User) {
        userDao.delete(user)
    }

    suspend fun getUser(username: String, password: String): User {
        return userDao.getUser(username, password)
    }

    suspend fun getUser(userId: Int): User {
        return userDao.getUser(userId)
    }

    fun getUsers(): Flow<List<User>> = users

}