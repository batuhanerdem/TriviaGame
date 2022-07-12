package com.batuhan.triviagame.ui.mainactivity

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.batuhan.triviagame.db.UserRepository
import com.batuhan.triviagame.model.User
import com.batuhan.triviagame.ui.loginactivity.LogInFragment
import kotlinx.coroutines.launch

class ProfileFragmentViewModel(private val repository: UserRepository) : ViewModel() {
    var currentUser = MutableLiveData<User>()

    fun getCurrentUser() {
        viewModelScope.launch {
            currentUser.value = repository.getUserByEmail(LogInFragment.user.eMail)
        }
    }
}