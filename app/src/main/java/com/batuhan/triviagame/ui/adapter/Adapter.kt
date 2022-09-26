package com.batuhan.triviagame.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.batuhan.triviagame.databinding.HighscoreRecyclerItemBinding
import com.batuhan.triviagame.model.User

class Adapter(private val highScoreList: List<User>) : RecyclerView.Adapter<Adapter.VHMainList>() {

    class VHMainList(val binding: HighscoreRecyclerItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHMainList {
        val binding =
            HighscoreRecyclerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VHMainList(binding)
    }

    override fun onBindViewHolder(holder: VHMainList, position: Int) {
        holder.binding.tvName.text = highScoreList[position].name
        holder.binding.tvScore.text = highScoreList[position].trueAnswerNumber.toString()
    }

    override fun getItemCount(): Int {
        return highScoreList.size
    }
}