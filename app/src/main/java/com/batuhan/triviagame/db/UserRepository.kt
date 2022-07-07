package com.batuhan.triviagame.db

import com.batuhan.triviagame.model.User

class UserRepository(private val dao: UserDAO) {

    suspend fun insert(user: User) {
        dao.insertUser(user)
    }
    suspend fun getAllUsers():List<User>{
        return dao.getAllUsers()
    }

    suspend fun update(user: User) {
        dao.updateUser(user)
    }

    suspend fun delete(user: User) {
        dao.deleteUser(user)
    }

    suspend fun clearDatabase() {
        dao.clearDB()
    }
}