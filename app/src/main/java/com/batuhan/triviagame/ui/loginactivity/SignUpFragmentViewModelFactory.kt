package com.batuhan.triviagame.ui.loginactivity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.batuhan.triviagame.db.UserRepository
import java.lang.IllegalArgumentException

class SignUpFragmentViewModelFactory(private val repository: UserRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(SignUpFragmentViewModel::class.java)){
            return SignUpFragmentViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown Viewmodel class")
    }
}