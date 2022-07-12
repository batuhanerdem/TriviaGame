package com.batuhan.triviagame.ui.mainactivity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.batuhan.triviagame.db.UserRepository
import java.lang.IllegalArgumentException

class PlayViewModelFactory(private val repository: UserRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(PlayViewModel::class.java)){
            return PlayViewModel(repository) as T
        }
        throw IllegalArgumentException("Unkown Viewmodel class")
    }
}