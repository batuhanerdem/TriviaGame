package com.batuhan.triviagame.ui.loginactivity


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.batuhan.triviagame.db.UserRepository
import com.batuhan.triviagame.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class SignUpFragmentViewModel(private val repository: UserRepository) : ViewModel() {
    private var isSignUp = MutableLiveData<Boolean>()
    private var exception = MutableLiveData<String>()
    private var auth = FirebaseAuth.getInstance()
    private var db = FirebaseFirestore.getInstance()
    lateinit var newUser: User

    fun getIsSignUp(): MutableLiveData<Boolean> {
        return isSignUp
    }

    fun getException(): MutableLiveData<String> {
        return exception
    }

    fun createAccount(
        e_mail: String,
        password: String,
        name: String
    ) {
        if (e_mail.isNotEmpty() && password.isNotEmpty() && name.isNotEmpty()) {
            auth.createUserWithEmailAndPassword(e_mail, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    newUser = saveUserToFirestore(e_mail, name)
                    isSignUp.value = true
                    saveToLocal()
                }
            }.addOnFailureListener { e ->
                isSignUp.value = false
                exception.value = e.localizedMessage as String
            }
        } else {
            exception.value = "Lutfen bir deger giriniz"
        }
    }

    private fun saveUserToFirestore(
        e_mail: String,
        name: String
    ): User {
        val userMap = hashMapOf(
            "name" to name,
            "email" to e_mail,
            "answeredQuestion" to 0,
            "trueAnswerNumber" to 0
        )

        val newUser = User(name, e_mail)
        db.collection("User").document(e_mail).set(userMap)
        return newUser
    }

    private fun saveToLocal() {
        viewModelScope.launch {
            insertOrUpdate(newUser)
        }
    }

    private suspend fun insertOrUpdate(
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
            repository.insert(currentUser)
        } else {
            repository.update(currentUser)
        }
    }
}