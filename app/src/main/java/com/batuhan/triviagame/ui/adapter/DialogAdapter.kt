package com.batuhan.triviagame.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.batuhan.triviagame.databinding.UserlistRecyclerItemBinding
import com.batuhan.triviagame.model.User
import com.batuhan.triviagame.ui.loginactivity.LogInFragment
import com.batuhan.triviagame.ui.mainactivity.MainActivity

class DialogAdapter(private val userList: List<User>) :
    RecyclerView.Adapter<DialogAdapter.VHMainList>() {
    class VHMainList(val binding: UserlistRecyclerItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHMainList {
        val binding =
            UserlistRecyclerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VHMainList(binding)
    }

    override fun onBindViewHolder(holder: VHMainList, position: Int) {
        holder.binding.userEmail.text = userList[position].eMail
        holder.binding.userEmail.setOnClickListener {
            LogInFragment.user = userList[position]
            val myIntent = Intent(it.context, MainActivity::class.java)
            it.context.startActivity(myIntent)
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }
}