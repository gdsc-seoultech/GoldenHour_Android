package com.gdsc.goldenhour.view.guide.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gdsc.goldenhour.databinding.GuideWebtoonItemBinding
import com.gdsc.goldenhour.network.model.GuideWebtoon

class GuideWebtoonAdapter(
    private val context: Context,
    private val imageList: List<GuideWebtoon>
) : RecyclerView.Adapter<GuideWebtoonAdapter.GuideWebtoonViewHolder>() {

    inner class GuideWebtoonViewHolder(
        private val binding: GuideWebtoonItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(imgUrl: String) {
            Glide.with(context)
                .load(imgUrl)
                .into(binding.webtoonImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GuideWebtoonViewHolder {
        val view = GuideWebtoonItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return GuideWebtoonViewHolder(view)
    }

    override fun onBindViewHolder(holder: GuideWebtoonViewHolder, position: Int) {
        holder.bind(imageList[position].imgUrl)
    }

    override fun getItemCount(): Int = imageList.size
}