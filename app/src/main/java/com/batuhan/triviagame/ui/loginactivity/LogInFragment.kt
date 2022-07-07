package com.batuhan.triviagame.ui.loginactivity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.batuhan.triviagame.R
import com.batuhan.triviagame.databinding.FragmentLogInBinding
import com.batuhan.triviagame.db.UserDatabase
import com.batuhan.triviagame.db.UserRepository
import com.batuhan.triviagame.model.User
import com.batuhan.triviagame.ui.mainactivity.MainActivity
import com.google.firebase.auth.FirebaseAuth

//FF004C69
class LogInFragment : Fragment() {
    private lateinit var viewModel: LoginFragmentViewModel
    private lateinit var binding: FragmentLogInBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_log_in, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dao = UserDatabase.getInstance(requireContext()).userDAO
        val repository = UserRepository(dao)
        val factory = LoginFragmentViewModelFactory(repository)
        var insertGate = true
        viewModel = ViewModelProvider(this, factory).get(LoginFragmentViewModel::class.java)
        binding = FragmentLogInBinding.bind(view)
        auth = FirebaseAuth.getInstance()


        binding.apply {

            textViewKayitOl.setOnClickListener() {
                val action = LogInFragmentDirections.actionLogInFragmentToSignUpFragment()
                it.findNavController().navigate(action)
            }
            buttonGirisYap.setOnClickListener {
                val username = editTextEmail.text.toString()
                val password = editTextPassword.text.toString()

                if (username == "delete") {
                    viewModel.clearDatabase()
                    Toast.makeText(context, "Database temizlendi", Toast.LENGTH_LONG)
                }
                viewModel.signIn(username, password)
                //for testing
                /*Intent(requireContext(), MainActivity::class.java).apply {
                    startActivity(this)
                }*/
            }
            viewModel.getIsSignIn().observe(viewLifecycleOwner, Observer {
                if (it) {
                    Intent(requireContext(), MainActivity::class.java).apply {
                        putExtra("username", editTextEmail.text.toString())
                        startActivity(this)
                    }
                } else {
                    Toast.makeText(requireContext(), "Basarisiz giris", Toast.LENGTH_LONG)
                        .show()
                }
            })
        }
    }
}