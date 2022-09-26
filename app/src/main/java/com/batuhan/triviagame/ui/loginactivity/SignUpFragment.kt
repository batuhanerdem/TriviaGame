package com.batuhan.triviagame.ui.loginactivity

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.batuhan.triviagame.R
import com.batuhan.triviagame.databinding.FragmentSignUpBinding
import com.batuhan.triviagame.db.UserDAO
import com.batuhan.triviagame.db.UserDatabase
import com.batuhan.triviagame.db.UserRepository
import com.batuhan.triviagame.ui.mainactivity.MainActivity
import com.google.firebase.auth.FirebaseAuth

class SignUpFragment : Fragment() {
    private lateinit var viewModel: SignUpFragmentViewModel
    private lateinit var binding: FragmentSignUpBinding
    private lateinit var factory: SignUpFragmentViewModelFactory
    private lateinit var dao: UserDAO
    private lateinit var repository: UserRepository
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dao = UserDatabase.getInstance(requireContext()).userDAO
        val repository = UserRepository(dao)
        factory = SignUpFragmentViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(SignUpFragmentViewModel::class.java)

        binding.apply {
            buttonSignUp.setOnClickListener {
                val email = editTextEmail.text.toString()
                val password = editTextPassword.text.toString()
                val name = editTextName.text.toString()
                viewModel.createAccount(email, password, name)
                viewModel.getException().observe(viewLifecycleOwner, Observer {
                    Toast.makeText(context, it, Toast.LENGTH_LONG).show()
                })
                viewModel.getIsSignUp().observe(viewLifecycleOwner, Observer {
                    if (it) {
                        Toast.makeText(context, "Basariyla kayit olundu", Toast.LENGTH_SHORT).show()
                        LogInFragment.user = viewModel.newUser
                        Intent(requireContext(),MainActivity::class.java).apply {
                            startActivity(this)
                        }
                    }
                })
            }
        }
    }
}