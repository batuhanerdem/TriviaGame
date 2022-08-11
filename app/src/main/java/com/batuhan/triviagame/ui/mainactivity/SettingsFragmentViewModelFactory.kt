package com.batuhan.triviagame.ui.mainactivity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.batuhan.triviagame.db.UserRepository
import java.lang.IllegalArgumentException

class SettingsFragmentViewModelFactory (private val repository: UserRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(SettingsFragmentViewModel::class.java)){
            return SettingsFragmentViewModel(repository) as T
        }
        throw IllegalArgumentException("Unkown Viewmodel class")
    }
}