package com.batuhan.triviagame.ui.loginactivity

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.batuhan.triviagame.db.UserRepository
import com.batuhan.triviagame.model.User
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginFragmentViewModel(private val repository: UserRepository) : ViewModel() {
    var isSignIn = MutableLiveData<Boolean>()
    private var auth = FirebaseAuth.getInstance()

    fun getIsSignIn(): MutableLiveData<Boolean> {
        return isSignIn
    }

    fun signIn(
        e_mail: String,
        password: String
    ) {
        if (e_mail.isNotEmpty() && password.isNotEmpty()) {
            auth.signInWithEmailAndPassword(e_mail, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    isSignIn.value = true
                    viewModelScope.launch {
                       insertIfNotExist(e_mail)
                    }
                }
            }.addOnFailureListener { e ->
                isSignIn.value = false
            }
        } else {
            isSignIn.value = false
        }
    }

    fun insertUser(user: User) {
        viewModelScope.launch {
            repository.insert(user)
        }
    }

    fun deleteUser(user: User) {
        viewModelScope.launch {
            repository.delete(user)
        }
    }

    fun clearDatabase() {
        viewModelScope.launch {
            repository.clearDatabase()
        }
    }

    suspend fun insertIfNotExist(e_mail :String){
        val allUsers = repository.getAllUsers()
        var insertGate = true

        for (user in allUsers) {
            if (e_mail == user.name) {
                println("ben kayitliyim yarram")
                insertGate = false
            }
        }
        if (insertGate) {
            println("insertgate =$insertGate")
            insertUser(User(name = e_mail))
        }
    }

    /*fun getAllUsers() {
        viewModelScope.launch {
            var result: List<User>?
            withContext(Dispatchers.IO) {
                result = repository.getAllUsers()
                users.value = result
            }
        }
    }*/
}