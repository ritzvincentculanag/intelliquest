package tech.ritzvincentculanag.intelliquest.repository

import kotlinx.coroutines.flow.Flow
import tech.ritzvincentculanag.intelliquest.dao.UserDao
import tech.ritzvincentculanag.intelliquest.model.User
import tech.ritzvincentculanag.intelliquest.model.relationship.UserQuests

class UserRepository(private val userDao: UserDao) {

    private val users: Flow<List<User>> = userDao.getUsers()
    private val userQuests: Flow<List<UserQuests>> = userDao.getUserQuests()

    suspend fun insert(user: User) {
        userDao.insert(user)
    }

    suspend fun update(user: User) {
        userDao.update(user)
    }

    suspend fun delete(user: User) {
        userDao.delete(user)
    }

    fun getUser(username: String, password: String): Flow<User> {
        return userDao.getUser(username, password)
    }

    fun getUser(userId: Int): Flow<User> {
        return userDao.getUser(userId)
    }

    fun getUsers(): Flow<List<User>> = users

    fun getUserQuests(): Flow<List<UserQuests>> = userQuests

}