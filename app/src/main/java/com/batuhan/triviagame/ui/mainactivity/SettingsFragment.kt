package com.batuhan.triviagame.ui.mainactivity

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.batuhan.triviagame.databinding.FragmentSettingsBinding
import com.batuhan.triviagame.db.UserDatabase
import com.batuhan.triviagame.db.UserRepository

class SettingsFragment : Fragment() {
    lateinit var viewModel: SettingsFragmentViewModel
    lateinit var binding: FragmentSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dao = UserDatabase.getInstance(requireContext()).userDAO
        val repository = UserRepository(dao)
        val factory = SettingsFragmentViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(SettingsFragmentViewModel::class.java)
        setOnClickListeners()

    }

    private fun setObserver() {
        viewModel.nameChangeLiveData.observe(viewLifecycleOwner, Observer {
            if (it) {
                Toast.makeText(
                    requireContext(),
                    "Isminiz basariyla degistirildi",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }

    private fun setOnClickListeners() {
        binding.apply {
            btnChangeName.setOnClickListener {
                tvNameChange.visibility = VISIBLE
                btnNameChangeTest.visibility = VISIBLE
            }
            btnNameChangeTest.setOnClickListener {
                val newName = tvNameChange.text.toString()
                changeName(newName)
                tvNameChange.visibility = INVISIBLE
                btnNameChangeTest.visibility = INVISIBLE
            }
        }
    }

    private fun changeName(newName: String) {
        viewModel.changeName(newName)
        setObserver()
    }
}