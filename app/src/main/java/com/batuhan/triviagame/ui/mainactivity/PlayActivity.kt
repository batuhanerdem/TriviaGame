package com.batuhan.triviagame.ui.mainactivity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.batuhan.triviagame.R
import com.batuhan.triviagame.databinding.ActivityPlayBinding
import com.batuhan.triviagame.db.UserDAO
import com.batuhan.triviagame.db.UserDatabase
import com.batuhan.triviagame.db.UserRepository
import com.batuhan.triviagame.model.Buttons

class PlayActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlayBinding
    private lateinit var viewModel: PlayViewModel
    private lateinit var factory: PlayViewModelFactory
    private lateinit var buttonList: List<Button>
    private lateinit var dao: UserDAO
    private lateinit var repository: UserRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dao = UserDatabase.getInstance(this).userDAO
        repository = UserRepository(dao)
        factory = PlayViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(PlayViewModel::class.java)
        buttonList = listOf(
            binding.btnAnswer1,
            binding.btnAnswer2,
            binding.btnAnswer3,
            binding.btnAnswer4
        )
        disableAllButtons()
        setQuestions()
        setAnswers()
        viewModel.getDatabase()
        setupOnClickListeners()

    }

    private fun setQuestions() {
        viewModel.getQuestions().observe(this, Observer {
            viewModel.resetButtons()
            //enableButtons()
            enableAllButtons()
            binding.apply {
                tvQuestion.text = it.text
                for ((index, button) in buttonList.withIndex()) {
                    button.text = it.answers[index]
                }
            }
        })
    }

    private fun setAnswers() {
        viewModel.answerButtons.observe(this, Observer {
            it.forEachIndexed { index, button ->
                buttonList[index].setTypeTo(button)
            }
        })
    }


    private fun setupOnClickListeners() {
        buttonList.forEachIndexed { index, button ->
            button.setOnClickListener {
                viewModel.selectAnswer(index)
            }
        }
        binding.apply {
            tvPoint.text = "0"
            btnAnswerIt.setOnClickListener {
                val selectedIndex = viewModel.selectedAnswerIndex()
                val trueAnswerIndex = viewModel.trueAnswerIndex()
                trueAnswerIndex ?: return@setOnClickListener
                selectedIndex ?: run {
                    Toast.makeText(this@PlayActivity, "lutfen bir sik seciniz", Toast.LENGTH_SHORT)
                        .show()
                    return@setOnClickListener
                }
                if (viewModel.isLastQuestion()) {
                    btnNextQuestion.text = "TESTI BITIR"
                } else {
                    btnNextQuestion.text = "SONRAKI SORU"
                }

                viewModel.answeredQuestionNumber += 1
                disableButtons()
                //set the backgrounds
                viewModel.answerButtons.value?.set(trueAnswerIndex, Buttons.CORRECT)

                if (selectedIndex != trueAnswerIndex) {
                    viewModel.answerButtons.value?.set(selectedIndex, Buttons.WRONG)
                } else {
                    viewModel.trueAnswerNumber += 1
                    tvPoint.text = viewModel.trueAnswerNumber.toString()
                }
                viewModel.notifyAnswerList()
            }

            btnNextQuestion.setOnClickListener {
                if (viewModel.isLastQuestion()) {
                    viewModel.updateUserTestValues()
                    backToProfile()
                } else {
                    viewModel.indexOfQuestion += 1
                    viewModel.resetButtons()
                    viewModel.nextQuestion()
                    btnNextQuestion.text = "PAS GEC"
                }

            }
        }
    }


    private fun Button.setTypeTo(buttons: Buttons) {
        this.background = when (buttons) {
            Buttons.SELECTED -> resources.getDrawable(R.drawable.clicked_button_background)
            Buttons.UNSELECTED -> resources.getDrawable(R.drawable.button_background)
            Buttons.CORRECT -> resources.getDrawable(R.drawable.true_answer_button_background)
            Buttons.WRONG -> resources.getDrawable(R.drawable.wrong_answer_button_background)
        }
    }


    private fun disableButtons() {
        buttonList.forEach {
            it.isEnabled = false
        }
        binding.btnAnswerIt.isEnabled = false
    }

    private fun enableButtons() {
        buttonList.forEach {
            it.isEnabled = true
        }
        binding.btnAnswerIt.isEnabled = true
    }

    private fun backToProfile() {
        val myIntent = Intent(this@PlayActivity, MainActivity::class.java)
        startActivity(myIntent)
        this@PlayActivity.finish()
    }

    override fun onBackPressed() {
        backToProfile()
    }

    private fun disableAllButtons() {
        disableButtons()
        binding.btnNextQuestion.isEnabled = false
    }

    private fun enableAllButtons() {
        enableButtons()
        binding.btnNextQuestion.isEnabled = true
    }
}