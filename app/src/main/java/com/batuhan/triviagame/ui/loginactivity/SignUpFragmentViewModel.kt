package com.batuhan.triviagame.ui.loginactivity


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.batuhan.triviagame.db.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SignUpFragmentViewModel(private val repository: UserRepository) : ViewModel() {
    private var isSignUp = MutableLiveData<Boolean>()
    private var exception = MutableLiveData<String>()
    private var auth = FirebaseAuth.getInstance()
    private var db = FirebaseFirestore.getInstance()

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
                    isSignUp.value = true
                    saveUserToFirestore(e_mail, name)
                }
            }.addOnFailureListener { e ->
                isSignUp.value = false
                exception.value = e.localizedMessage as String
            }
        } else {
            exception.value = "Lutfen bir deger giriniz"
        }
    }

    private fun saveUserToFirestore(e_mail: String, name: String) {
        val userMap = hashMapOf(
            "name" to name,
            "email" to e_mail,
            "answeredQuestion" to 0,
            "trueAnswerNumber" to 0
        )
        db.collection("User").document(e_mail).set(userMap)
    }
}