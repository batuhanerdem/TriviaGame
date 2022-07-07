package com.batuhan.triviagame.ui.loginactivity


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class SignUpFragmentViewModel : ViewModel() {
    private var isSignUp = MutableLiveData<Boolean>()
    private var exception = String()

    fun getIsSignUp(): MutableLiveData<Boolean> {
        return isSignUp
    }

    fun getException(): String {
        return exception
    }

    fun createAccount(
        auth: FirebaseAuth,
        e_mail: String,
        password: String
    ) {
        auth.createUserWithEmailAndPassword(e_mail, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                isSignUp.value = true
            }
        }.addOnFailureListener { e ->
            isSignUp.value = false
            exception = e.localizedMessage as String
        }
    }
}