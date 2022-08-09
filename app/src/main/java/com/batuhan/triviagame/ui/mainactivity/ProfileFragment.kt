package com.batuhan.triviagame.ui.mainactivity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.batuhan.triviagame.databinding.FragmentProfileBinding
import com.batuhan.triviagame.db.UserDatabase
import com.batuhan.triviagame.db.UserRepository
import com.batuhan.triviagame.model.User
import com.batuhan.triviagame.ui.adapter.Adapter
import com.batuhan.triviagame.ui.loginactivity.LogInFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {
    private lateinit var viewModel: ProfileFragmentViewModel
    private lateinit var binding: FragmentProfileBinding
    private lateinit var adapter: Adapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dao = UserDatabase.getInstance(requireContext()).userDAO
        val repository = UserRepository(dao)
        val factory = ProfilfeFragmentViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(ProfileFragmentViewModel::class.java)


        viewModel.apply{
            highScoreList.observe(viewLifecycleOwner, Observer {
                adapter.notifyDataSetChanged()
            })
            getHighscoreTable()
            highScoreList.value?.let {
                setRV(it)
            }
        }

        binding.apply {
            LogInFragment.user.apply {
                tvName.text = name
                tvAnsweredQuestion.text = answeredQuestion.toString()
                tvTrueAnsweredQuestion.text = trueAnswerNumber.toString()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        binding.apply {
            LogInFragment.user.apply {
                tvName.text = name
                tvAnsweredQuestion.text = answeredQuestion.toString()
                tvTrueAnsweredQuestion.text = trueAnswerNumber.toString()
            }
        }
    }

    private fun setRV(highScoreList: MutableList<User>) {
        adapter = Adapter(highScoreList)
        binding.highScoreRecycler.adapter = adapter
        binding.highScoreRecycler.layoutManager = LinearLayoutManager(requireContext())
    }

}