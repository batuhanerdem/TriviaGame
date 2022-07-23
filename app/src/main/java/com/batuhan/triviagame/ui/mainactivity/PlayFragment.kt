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
import com.batuhan.triviagame.db.UserDAO
import com.batuhan.triviagame.db.UserDatabase
import com.batuhan.triviagame.db.UserRepository
import com.batuhan.triviagame.model.Buttons

class PlayFragment : Fragment() {
    private lateinit var binding: FragmentPlayBinding
    private lateinit var viewModel: PlayViewModel
    private lateinit var factory: PlayViewModelFactory
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
        buttonList = listOf(
            binding.btnAnswer1,
            binding.btnAnswer2,
            binding.btnAnswer3,
            binding.btnAnswer4
        )
        setQuestions()// guestions from database
        viewModel.getDatabase(requireContext(), questionUid)//sets database

        binding.apply {
            btnAnswerIt.setOnClickListener {
                selectedButton?.let {
                    disableButtons()
                    btnNextQuestion.text = "SONRAKI SORU"
                    answeredQuestionNumber++//viewmodel


                    if (it.text.toString() == trueAnswer) {
                        it.trueAnswer()
                    } else {
                        it.wrongAnswer()
                        showTrueAnswer()
                    }
                } ?: run {
                    Toast.makeText(requireContext(), "Lutfen bir sik seciniz", Toast.LENGTH_SHORT)
                }
            }






            btnNextQuestion.setOnClickListener {
                selectedButton = null
                viewModel.getDatabase(requireContext(), ++questionUid)
                if (viewModel.hasQuestions) {//this means we still have questions
                    setQuestions()
                } else {
                    //viewModel.updateUserTestValues(trueAnswerNumber.value!!, answeredQuestionNumber)
                    Intent(requireContext(), MainActivity::class.java).apply {
                        startActivity(this)
                    }
                }
            }
        }
    }

    private fun setQuestions() {
        viewModel.getQuestions().observe(viewLifecycleOwner, Observer {
            viewModel.resetButtons()
            binding.apply {
                tvQuestion.text = it.text
                for ((index, button) in buttonList.withIndex()) {
                    button.text = it.answers[index]
                }
            }
        })
    }

    private fun setAnswers() {
        viewModel.answerButtons.observe(viewLifecycleOwner, Observer {
            binding.apply {
                it.forEachIndexed { index, buttons ->
                    buttonList[index].setTypeTo(buttons)
                }
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
            btnAnswerIt.setOnClickListener {
                val selectedIndex = viewModel.indexOfSelectedButton()
                val trueAnswerIndex = viewModel.trueAnswerIndex()
                trueAnswerIndex ?: return@setOnClickListener
                selectedIndex ?: return@setOnClickListener

                disableButtons()
                //set the backgrounds
                viewModel.answerButtons.value?.set(trueAnswerIndex, Buttons.CORRECT)

                if(selectedIndex!=)
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
}

