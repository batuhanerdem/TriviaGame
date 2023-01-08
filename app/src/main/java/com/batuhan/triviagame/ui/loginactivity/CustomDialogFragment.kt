package com.batuhan.triviagame.ui.loginactivity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.batuhan.triviagame.databinding.FragmentCustomDialogBinding
import com.batuhan.triviagame.model.User
import com.batuhan.triviagame.ui.adapter.DialogAdapter
import com.batuhan.triviagame.ui.mainactivity.MainActivity

class CustomDialogFragment(private val userList: List<User>) : DialogFragment() {
    private lateinit var binding: FragmentCustomDialogBinding
    private lateinit var adapter: DialogAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCustomDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRV(userList)
    }

    override fun onResume() {
        super.onResume()
        dialog?.window?.let {
            val params = it.attributes
            params.width = MATCH_PARENT
            params.height = WRAP_CONTENT
            params.dimAmount = 0.7f
            it.attributes = params
        }
    }

    private fun setUpRV(userList: List<User>) {
        adapter = DialogAdapter(userList) {
            val myIntent = Intent(requireContext(), MainActivity::class.java)
            startActivity(myIntent)
            activity?.finish()
        }
        binding.accountRecycler.adapter = adapter
        binding.accountRecycler.layoutManager = LinearLayoutManager(requireContext())
    }
}