package com.batuhan.triviagame.ui.mainactivity

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.batuhan.triviagame.db.UserRepository
import com.batuhan.triviagame.model.User
import com.batuhan.triviagame.ui.loginactivity.LogInFragment
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class SettingsFragmentViewModel(val repository: UserRepository) : ViewModel() {
    var nameChangeLiveData = MutableLiveData<Boolean>()
    val db = FirebaseFirestore.getInstance()

    fun changeName(newName: String) {
        val currentUserEMail = LogInFragment.user.eMail
        viewModelScope.launch {
            changeNameLocal(currentUserEMail, newName)
        }
        changeNameStaticUser(newName)
        changeNameFirebase(currentUserEMail, newName)
    }

    private suspend fun changeNameLocal(currentUserEMail: String, newName: String) {
        val currentUser = repository.getUserByEmail(currentUserEMail)
        val updatedUser = User(
            newName,
            currentUser.eMail,
            currentUser.answeredQuestion,
            currentUser.trueAnswerNumber
        )
        repository.update(updatedUser)
    }

    private fun changeNameFirebase(currentUserEMail: String, newName: String) {
        db.collection("User").document(currentUserEMail.toString()).update("name", newName)
            .addOnCompleteListener {
                nameChangeLiveData.value = true
            }
    }

    private fun changeNameStaticUser(newName: String) {
        LogInFragment.user.name = newName
    }
}