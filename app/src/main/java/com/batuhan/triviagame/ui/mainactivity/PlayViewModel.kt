package com.batuhan.triviagame.ui.mainactivity

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.batuhan.triviagame.db.UserRepository
import com.batuhan.triviagame.model.Questions
import com.batuhan.triviagame.model.User
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class PlayViewModel() : ViewModel() {
    private var allDocuments = arrayListOf<DocumentSnapshot>()
    private var question = MutableLiveData<Questions>()
    var hasQuestions = true

    //private var questionList = arrayListOf<Questions>()

    fun getQuestions(): MutableLiveData<Questions> {
        return question
    }

    fun getAllDocuments(): ArrayList<DocumentSnapshot> {
        return allDocuments
    }

    fun getDatabase(db: FirebaseFirestore, context: Context, uid: Int) {

        db.collection("Play").addSnapshotListener { snapshot, exception ->

            if (exception != null) {
                Toast.makeText(context, exception.localizedMessage, Toast.LENGTH_LONG).show()
            } else {
                if (snapshot != null) {
                    if (!snapshot.isEmpty) {
                        val allDocuments = snapshot.documents as ArrayList<DocumentSnapshot>
                        if (uid >= allDocuments.size) {
                            hasQuestions = false
                        }
                        for (document in allDocuments) {
                            if (document.get("Uid") == uid.toLong()) {
                                val text = document.get("Question") as String
                                val answerA = document.get("AnswerA") as String
                                val answerB = document.get("AnswerB") as String
                                val answerC = document.get("AnswerC") as String
                                val answerD = document.get("AnswerD") as String
                                val trueAnswer = document.get("TrueAnswer") as String
                                val uuid = document.get("Uid") as Long
                                val answerList = listOf(answerA, answerB, answerC, answerD)
                                question.value =
                                    Questions(text, answerList, trueAnswer, uuid.toInt())
                            }
                        }
                    }
                }
            }
        }
    }
}