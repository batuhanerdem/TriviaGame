package com.batuhan.triviagame.db

import android.provider.ContactsContract
import androidx.room.*
import com.batuhan.triviagame.model.User

@Dao
interface UserDAO {

    @Insert
    suspend fun insertUser(user: User)

    @Update
    suspend fun updateUser(user: User)

    @Delete
    suspend fun deleteUser(user: User)

    @Query("DELETE FROM User")
    suspend fun clearDB()

    @Query("SELECT * FROM user")
    suspend fun getAllUsers(): List<User>

    @Query("SELECT * FROM User WHERE userEmail =:eMail")
    suspend fun getUserByEmail(eMail: String): User
}