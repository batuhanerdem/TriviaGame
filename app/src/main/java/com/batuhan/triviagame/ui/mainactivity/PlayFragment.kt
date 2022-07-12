package com.batuhan.triviagame.ui.mainactivity

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.batuhan.triviagame.R
import com.batuhan.triviagame.databinding.FragmentPlayBinding
import com.batuhan.triviagame.databinding.FragmentProfileBinding
import com.batuhan.triviagame.db.UserDAO
import com.batuhan.triviagame.db.UserDatabase
import com.batuhan.triviagame.db.UserRepository

class PlayFragment : Fragment() {
    private lateinit var binding: FragmentPlayBinding
    private lateinit var viewModel: PlayViewModel
    private lateinit var factory: PlayViewModelFactory
    private var selectedButton: Button? = null
    private var trueAnswer = "X"
    private var questionUid = 1
    private lateinit var buttonList: List<Button>
    private lateinit var dao: UserDAO
    private lateinit var repository: UserRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlayBinding.inflate(inflater, container, false);

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dao = UserDatabase.getInstance(requireContext()).userDAO
        repository = UserRepository(dao)
        factory = PlayViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(PlayViewModel::class.java)
        val trueAnswerNumber = MutableLiveData<Int>()
        var answeredQuestionNumber = 0
        buttonList = listOf(
            binding.btnAnswer1,
            binding.btnAnswer2,
            binding.btnAnswer3,
            binding.btnAnswer4
        )

        viewModel.getDatabase(requireContext(), questionUid)//sets database
        trueAnswerNumber.value = 0
        trueAnswerNumber.observe(viewLifecycleOwner, Observer {
            binding.tvPoint.text = it.toString()
        })
        setQuestions()// guestions from database

        //buttons are gonna change their colors according to if they are the true answer or not
        for (button in buttonList) {
            button.changeBackGround()
        }

        binding.apply {
            btnAnswerIt.setOnClickListener {
                selectedButton?.let {
                    disableButtons()
                    btnNextQuestion.text = "SONRAKI SORU"
                    answeredQuestionNumber++

                    if (it.text.toString() == trueAnswer) {
                        it.trueAnswer()
                        trueAnswerNumber.value = trueAnswerNumber.value!!.plus(1)
                    } else {
                        it.wrongAnswer()
                        showTrueAnswer()
                    }
                } ?: run {
                    Toast.makeText(requireContext(), "Lutfen bir sik seciniz", Toast.LENGTH_SHORT)
                }
            }

            btnNextQuestion.setOnClickListener {
                resetButtons()
                enableButtons()
                selectedButton = null
                viewModel.getDatabase(requireContext(), ++questionUid)
                if (viewModel.hasQuestions) {//this means we still have questions
                    setQuestions()
                } else {
                    viewModel.updateUserQuestions(trueAnswerNumber.value!!, answeredQuestionNumber)
                    Intent(requireContext(), MainActivity::class.java).apply {
                        startActivity(this)
                    }
                }
            }
        }
    }

    private fun setQuestions() {
        viewModel.getQuestions().observe(viewLifecycleOwner, Observer {
            val answerList = it.answers
            var i = 0

            binding.apply {
                tvQuestion.text = it.text
                trueAnswer = it.trueAnswer
                btnNextQuestion.text = "PAS GEC"
                for (button in buttonList) {
                    button.text = answerList[i++]
                }
            }
        })
    }

    private fun showTrueAnswer() {
        val trueAnswerDraw = resources.getDrawable(R.drawable.true_answer_button_background)
        for (button in buttonList) {
            if (button.text.toString() == trueAnswer) {
                button.background = trueAnswerDraw
            }
        }
    }

    private fun resetButtons() {
        val notClicked = resources.getDrawable(R.drawable.button_background)
        for (button in buttonList) {
            button.background = notClicked
        }
    }

    private fun enableButtons() {
        val buttonListExtended = buttonList.toMutableList()
        buttonListExtended.add(binding.btnAnswerIt)
        for (button in buttonListExtended) {
            button.isEnabled = true
        }
    }

    private fun disableButtons() {
        val buttonListExtended = buttonList.toMutableList()
        buttonListExtended.add(binding.btnAnswerIt)
        for (button in buttonListExtended) {
            button.isEnabled = false
        }
    }

    private fun Button.changeBackGround() {
        this.setOnClickListener {
            resetButtons()
            val clicked = resources.getDrawable(R.drawable.clicked_button_background)
            this.background = clicked
            selectedButton = this
        }
    }

    private fun Button.trueAnswer() {
        resetButtons()
        val trueAnswerDraw = resources.getDrawable(R.drawable.true_answer_button_background)
        this.background = trueAnswerDraw
    }

    private fun Button.wrongAnswer() {
        resetButtons()
        val wrongAnswerDraw = resources.getDrawable(R.drawable.wrong_answer_button_background)
        this.background = wrongAnswerDraw
    }
}