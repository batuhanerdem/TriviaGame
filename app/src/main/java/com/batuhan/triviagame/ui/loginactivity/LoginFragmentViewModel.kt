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
                    val userRef = db.collection("User").document(e_mail)
                    userRef.get().addOnSuccessListener { document ->
                        if (document != null) {
                            val name = document.get("name") as String
                            val e_mail = document.get("email") as String
                            val answeredQuestion = document.get("answeredQuestion") as Long
                            val trueAnswerNumber = document.get("trueAnswerNumber") as Long
                            val user = User(
                                name,
                                e_mail,
                                answeredQuestion.toInt(),
                                trueAnswerNumber.toInt()
                            )
                            viewModelScope.launch {
                                insertIfNotExist(user)
                            }
                            LogInFragment.user = User(
                                name,
                                e_mail,
                                answeredQuestion.toInt(),
                                trueAnswerNumber.toInt()
                            )
                        }
                        isSignIn.value = true
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

    private fun updateUser(user: User) {
        viewModelScope.launch {
            repository.update(user)
        }
    }

//    fun deleteUser(user: User) {
//        viewModelScope.launch {
//            repository.delete(user)
//        }
//    }

//    fun clearDatabase() {
//        viewModelScope.launch {
//            repository.clearDatabase()
//        }
//    }

    suspend fun insertIfNotExist(
        currentUser: User
    ) {
        val allUsers = repository.getAllUsers()
        var insertGate = true

        for (user in allUsers) {
            if (user.eMail == currentUser.eMail) {
                insertGate = false
            }
        }
        if (insertGate) {
            insertUser(currentUser)
        } else {
            updateUser(currentUser)
        }
    }
}