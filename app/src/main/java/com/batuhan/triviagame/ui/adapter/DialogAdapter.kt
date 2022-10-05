package com.batuhan.triviagame.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.batuhan.triviagame.databinding.UserlistRecyclerItemBinding
import com.batuhan.triviagame.model.User
import com.batuhan.triviagame.ui.loginactivity.LogInFragment

class DialogAdapter(private val userList: List<User>, private val goActivity: () -> Unit) :
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
            goActivity()
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }
}