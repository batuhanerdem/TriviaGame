package com.batuhan.triviagame.ui.mainactivity

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.batuhan.triviagame.db.UserRepository
import com.batuhan.triviagame.model.User
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class ProfileFragmentViewModel(private val repository: UserRepository) : ViewModel() {
    val db = FirebaseFirestore.getInstance()
    var highScoreList = MutableLiveData<MutableList<User>>()


    fun getHighscoreTable() {
        highScoreList.value = mutableListOf()
        db.collection("User").orderBy("trueAnswerNumber", Query.Direction.DESCENDING)
            .addSnapshotListener { value, error ->
                value?.let {
                    highScoreList.value!!.clear()
                    for (i in 0 until value.documents.size) {
                        if (it.documents[i]["trueAnswerNumber"].toString().toInt() > 0) {
                            //println(it.documents[i]["name"].toString())
                            val newUser = User(
                                it.documents[i]["name"].toString(),
                                it.documents[i]["email"].toString(),
                                it.documents[i]["answeredQuestion"].toString().toInt(),
                                it.documents[i]["trueAnswerNumber"].toString().toInt(),
                            )
                            highScoreList.value!!.add(newUser)
                        }
                    }
                    highScoreList.value = highScoreList.value
                }
            }
    }

    fun getUser() {
        //db.collection("User").document().

    }
}