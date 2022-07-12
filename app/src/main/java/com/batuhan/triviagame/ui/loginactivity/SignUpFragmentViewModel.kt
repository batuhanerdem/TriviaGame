package com.batuhan.triviagame.ui.loginactivity


import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlin.coroutines.coroutineContext

class SignUpFragmentViewModel : ViewModel() {
    private var isSignUp = MutableLiveData<Boolean>()
    private var exception = MutableLiveData<String>()
    private var auth = FirebaseAuth.getInstance()

    fun getIsSignUp(): MutableLiveData<Boolean> {
        return isSignUp
    }

    fun getException(): MutableLiveData<String> {
        return exception
    }

    fun createAccount(
        e_mail: String,
        password: String
    ) {
        if (e_mail.isNotEmpty() && password.isNotEmpty()){
            auth.createUserWithEmailAndPassword(e_mail, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    isSignUp.value = true
                }
            }.addOnFailureListener { e ->
                isSignUp.value = false
                exception.value = e.localizedMessage as String
            }
        }else{
            exception.value = "Lutfen bir deger giriniz"
        }

    }
}