package com.batuhan.triviagame.ui.mainactivity

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.batuhan.triviagame.db.UserRepository
import com.batuhan.triviagame.model.Buttons
import com.batuhan.triviagame.model.Questions
import com.batuhan.triviagame.model.User
import com.batuhan.triviagame.ui.loginactivity.LogInFragment
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class PlayViewModel(private val repository: UserRepository) : ViewModel() {
    var trueAnswerNumber = 0
    var answeredQuestionNumber = 0
    var soruIndex = 0
    private var allQuestions = mutableListOf<Questions>()

    private var question = MutableLiveData<Questions>()
    private var db = FirebaseFirestore.getInstance()

    private var _answerButtons = MutableLiveData<MutableList<Buttons>>(mutableListOf())
    val answerButtons: LiveData<MutableList<Buttons>>
        get() = _answerButtons

    fun getQuestions(): LiveData<Questions> {
        return question
    }

    fun getDatabase() {
        db.collection("Play").addSnapshotListener { snapshot, e ->
            if (e == null) {
                if (snapshot != null && !snapshot.isEmpty) {
                    val allDocuments = snapshot.documents as ArrayList<DocumentSnapshot>

                    for (index in 0..9) {
                        val text = allDocuments[index].get("Question") as String
                        val answerA = allDocuments[index].get("AnswerA") as String
                        val answerB = allDocuments[index].get("AnswerB") as String
                        val answerC = allDocuments[index].get("AnswerC") as String
                        val answerD = allDocuments[index].get("AnswerD") as String
                        val trueAnswer = allDocuments[index].get("TrueAnswer") as String
                        val uuid = allDocuments[index].get("Uid") as Long
                        val answerList = listOf(answerA, answerB, answerC, answerD)
                        allQuestions.add(
                            Questions(text, answerList, trueAnswer, uuid.toInt())
                        )
                    }
                    nextQuestion()
                }
            }
        }
    }

    // buraya bak
    fun updateUserTestValues() {
        viewModelScope.launch {
            val currentUser = repository.getUserByEmail(LogInFragment.user.eMail)//get user

            val user = User(
                currentUser.name,
                currentUser.eMail,
                currentUser.answeredQuestion + answeredQuestionNumber,
                currentUser.trueAnswerNumber + trueAnswerNumber
            )
            repository.update(user)// update user with new values
            db.collection("User").document(currentUser.eMail).set(user)//update user in firebase
        }
        //update user values in static variables
        LogInFragment.user.trueAnswerNumber += trueAnswerNumber
        LogInFragment.user.answeredQuestion += answeredQuestionNumber
    }

    fun resetButtons() {
        Log.d("allah", "reset")
        _answerButtons.value?.clear()
        for (i in 0..3) {
            _answerButtons.value?.add(Buttons.UNSELECTED)
        }
        notifyAnswerList()
    }

    fun selectAnswer(index: Int) {
        _answerButtons.value?.replaceAll {
            if (it == Buttons.SELECTED) Buttons.UNSELECTED
            else it
        }
        _answerButtons.value?.set(index, Buttons.SELECTED)
        notifyAnswerList()
    }

    fun selectedAnswerIndex() =
        answerButtons.value?.indexOf(Buttons.SELECTED).takeIf { it != -1 }

    fun trueAnswerIndex() =
        getQuestions().value?.answers?.indexOf(getQuestions().value?.trueAnswer)
            .takeIf { it != -1 }

    fun nextQuestion() {
        question.value = allQuestions[soruIndex]
    }

    fun isLastQuestion() = soruIndex == allQuestions.size - 1

    fun notifyAnswerList() {
        _answerButtons.value = _answerButtons.value
    }
}