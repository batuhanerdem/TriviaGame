package com.batuhan.triviagame.ui.mainactivity

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.batuhan.triviagame.db.UserRepository
import com.batuhan.triviagame.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfileFragmentViewModel(private val repository: UserRepository) : ViewModel() {

    var users: MutableLiveData<List<User>> = MutableLiveData()

    fun getAllUsers() {
        viewModelScope.launch {
            var result :List<User>? = null
            withContext(Dispatchers.IO){
                result = repository.getAllUsers()
            }
            users.value = result
        }
    }
}