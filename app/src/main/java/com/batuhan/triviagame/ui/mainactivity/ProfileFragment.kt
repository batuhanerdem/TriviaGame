package com.batuhan.triviagame.ui.mainactivity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.batuhan.triviagame.R
import com.batuhan.triviagame.databinding.FragmentProfileBinding
import com.batuhan.triviagame.db.UserDatabase
import com.batuhan.triviagame.db.UserRepository
import com.batuhan.triviagame.ui.loginactivity.LogInFragment

class ProfileFragment : Fragment() {
    private lateinit var viewModel: ProfileFragmentViewModel
    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dao = UserDatabase.getInstance(requireContext()).userDAO
        val repository = UserRepository(dao)
        val factory = ProfilfeFragmentViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(ProfileFragmentViewModel::class.java)

        binding.textView2.text = LogInFragment.username
    }
}