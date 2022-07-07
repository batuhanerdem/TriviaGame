package com.batuhan.triviagame.ui.mainactivity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.batuhan.triviagame.db.UserRepository
import com.batuhan.triviagame.ui.loginactivity.LoginFragmentViewModel
import java.lang.IllegalArgumentException

class ProfilfeFragmentViewModelFactory(private val repository: UserRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(ProfileFragmentViewModel::class.java)){
            return ProfileFragmentViewModel(repository) as T
        }
        throw IllegalArgumentException("Unkown Viewmodel class")
    }
}