package com.batuhan.triviagame.ui.mainactivity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.net.toUri
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.fragment.findNavController
import com.batuhan.triviagame.R
import com.batuhan.triviagame.databinding.FragmentPlayBinding
import com.batuhan.triviagame.db.UserDAO
import com.batuhan.triviagame.db.UserDatabase
import com.batuhan.triviagame.db.UserRepository
import com.batuhan.triviagame.model.Buttons
import java.io.File

class PlayFragment : Fragment() {
    private lateinit var binding: FragmentPlayBinding
    private lateinit var viewModel: PlayViewModel
    private lateinit var factory: PlayViewModelFactory
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
        setAnswers()
        viewModel.getDatabase()//sets database
        setupOnClickListeners()
    }

    private fun setQuestions() {
        viewModel.getQuestions().observe(viewLifecycleOwner, Observer {
            viewModel.resetButtons()
            Log.d("allah", viewModel.answerButtons.value.toString())
            enableButtons()
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
            btnAnswerIt.setOnClickListener {
                val selectedIndex = viewModel.selectedAnswerIndex()
                val trueAnswerIndex = viewModel.trueAnswerIndex()
                trueAnswerIndex ?: return@setOnClickListener
                selectedIndex ?: run {
                    Toast.makeText(requireContext(), "sik sec", Toast.LENGTH_LONG)
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
                }
                viewModel.notifyAnswerList()
            }

            btnNextQuestion.setOnClickListener {
                if (viewModel.isLastQuestion()) {
                    Log.d("mytag", viewModel.answeredQuestionNumber.toString())
                    viewModel.updateUserTestValues()
//                    requireActivity().supportFragmentManager.beginTransaction()
//                        .remove(this@PlayFragment)
//                        .commit()
                    val request =
                        NavDeepLinkRequest.Builder.fromAction("PlayFragmentDirections.ActionPlayFragmentToProfileFragment")
                            .build()
                    findNavController().navigate(request)
                } else {
                    viewModel.numberOfQuestion += 1
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
}

