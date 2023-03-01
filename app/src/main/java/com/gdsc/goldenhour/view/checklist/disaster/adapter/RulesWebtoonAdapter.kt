package com.gdsc.goldenhour.view.checklist.disaster.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gdsc.goldenhour.databinding.RulesWebtoonItemBinding
import com.gdsc.goldenhour.network.model.DisasterWebtoon

class RulesWebtoonAdapter(
    private val context: Context?,
    private val data: List<DisasterWebtoon>
) : RecyclerView.Adapter<RulesWebtoonAdapter.DisasterWebtoonViewHolder>() {

    inner class DisasterWebtoonViewHolder(
        private val binding: RulesWebtoonItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(imgUrl: String) {
            if (context != null) {
                Glide.with(context)
                    .load(imgUrl)
                    .into(binding.webtoonImage)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DisasterWebtoonViewHolder {
        val view = RulesWebtoonItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)
        return DisasterWebtoonViewHolder(view)
    }

    override fun onBindViewHolder(holder: DisasterWebtoonViewHolder, position: Int) {
        holder.bind(data[position].imgUrl)
    }

    override fun getItemCount(): Int = data.size
}