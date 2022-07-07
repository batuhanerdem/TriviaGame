package com.batuhan.triviagame.ui.loginactivity

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
import com.google.firebase.auth.FirebaseAuth

class SignUpFragment : Fragment() {
    private lateinit var viewModel: SignUpFragmentViewModel
    private lateinit var binding: FragmentSignUpBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_sign_up, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSignUpBinding.bind(view)
        viewModel = ViewModelProvider(this).get(SignUpFragmentViewModel::class.java)
        auth = FirebaseAuth.getInstance()

        binding.apply {
            buttonKayitOlFragment.setOnClickListener {
                viewModel.createAccount(
                    auth,
                    editTextEmail.text.toString(),
                    editTextPassword.text.toString()
                )
                viewModel.getIsSignUp().observe(viewLifecycleOwner, Observer {
                    if (viewModel.getIsSignUp().value!!) {
                        Toast.makeText(context, "Basariyla kayit olundu", Toast.LENGTH_SHORT).show()

                        val action = SignUpFragmentDirections.actionSignUpFragmentToLogInFragment()
                        Navigation.findNavController(view).navigate(action)

                    } else if (!viewModel.getIsSignUp().value!!) {
                        Toast.makeText(
                            requireContext(),
                            viewModel.getException(),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                })
            }
        }
    }
}