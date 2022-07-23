package com.batuhan.triviagame.ui.mainactivity

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.batuhan.triviagame.R
import com.batuhan.triviagame.db.UserRepository
import com.batuhan.triviagame.model.Buttons
import com.batuhan.triviagame.model.Questions
import com.batuhan.triviagame.model.User
import com.batuhan.triviagame.ui.loginactivity.LogInFragment
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import java.util.function.UnaryOperator

class PlayViewModel(private val repository: UserRepository) : ViewModel() {
    private var trueAnswerNumber = 0
    private var answeredQuestionNumber = 0

    private var question = MutableLiveData<Questions>()
    var hasQuestions = true
    private var db = FirebaseFirestore.getInstance()

    private var _answerButtons = MutableLiveData<MutableList<Buttons>>()
    val answerButtons: LiveData<MutableList<Buttons>>
        get() = _answerButtons

    fun getQuestions(): MutableLiveData<Questions> {
        return question
    }

    fun getDatabase(context: Context, uid: Int) {
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

    // buraya bak
    fun updateUserTestValues(
        trueAnswerNumber: Int,
        answeredQuestionNumber: Int
    ) {
        viewModelScope.launch {
            val currentUser = repository.getUserByEmail(LogInFragment.user.eMail)//get user
            currentUser.apply {
                val user = User(
                    name,
                    eMail,
                    this.answeredQuestion.plus(answeredQuestionNumber),
                    this.trueAnswerNumber.plus(trueAnswerNumber)
                )
                repository.update(user)// update user with new values
                db.collection("User").document(eMail).set(user)//update user in firebase
            }
        }
        //update user values in static variables
        LogInFragment.user.trueAnswerNumber += trueAnswerNumber
        LogInFragment.user.answeredQuestion += answeredQuestionNumber
    }

    fun resetButtons() {
        _answerButtons.value?.clear() ?: run {
            _answerButtons.value = mutableListOf()
        }
        for (i in 0..3) {
            _answerButtons.value!!.add(Buttons.UNSELECTED)
        }
    }

    fun selectAnswer(index: Int) {
        _answerButtons.value?.replaceAll {
            if (it == Buttons.SELECTED) Buttons.UNSELECTED
            it
        }
    }

    fun indexOfSelectedButton() = answerButtons.value?.indexOf(Buttons.SELECTED).takeIf { it != -1 }

    fun trueAnswerIndex() =
        getQuestions().value?.answers?.indexOf(getQuestions().value?.trueAnswer).takeIf { it != -1 }
}