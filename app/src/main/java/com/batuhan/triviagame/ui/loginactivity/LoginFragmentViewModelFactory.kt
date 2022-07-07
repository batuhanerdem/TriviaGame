package com.batuhan.triviagame.ui.loginactivity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.batuhan.triviagame.db.UserRepository
import java.lang.IllegalArgumentException

class LoginFragmentViewModelFactory(private val repository: UserRepository):ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(LoginFragmentViewModel::class.java)){
            return LoginFragmentViewModel(repository) as T
        }
        throw IllegalArgumentException("Unkown Viewmodel class")
    }
}