package com.batuhan.triviagame.ui.loginactivity

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.batuhan.triviagame.db.UserRepository
import com.batuhan.triviagame.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class LoginFragmentViewModel(private val repository: UserRepository) : ViewModel() {
    var isSignIn = MutableLiveData<Boolean>()
    private var auth = FirebaseAuth.getInstance()
    private var db = Firebase.firestore

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
                        val currentUser = repository.getUserByEmail(e_mail)
                        insertIfNotExist(e_mail)
                        saveToFirestore(currentUser)
                    }
                }
            }.addOnFailureListener {
                isSignIn.value = false
            }
        } else {
            isSignIn.value = false
        }
    }

    private fun insertUser(user: User) {
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

    suspend fun insertIfNotExist(e_mail: String) {
        val allUsers = repository.getAllUsers()
        var insertGate = true

        for (user in allUsers) {
            if (e_mail == user.name) {
                insertGate = false
            }
        }
        if (insertGate) {
            insertUser(User(name = e_mail, eMail = e_mail))
        }
    }

    private fun saveToFirestore(user: User) {
        user.apply {
            val userMap = hashMapOf(
                "Name" to name,
                "Mail" to eMail,
                "Answered Question" to answeredQuestion,
                "True Answered Question" to trueAnswerNumber
            )
            db.collection("User").document(eMail).set(userMap)
        }
    }
}