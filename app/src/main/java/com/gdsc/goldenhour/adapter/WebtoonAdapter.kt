package com.gdsc.goldenhour.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gdsc.goldenhour.databinding.WebtoonListItemBinding
import com.gdsc.goldenhour.network.model.WebtoonItem

class WebtoonAdapter(
    private val context: Context?,
    private val imageList: List<WebtoonItem>
) : RecyclerView.Adapter<WebtoonAdapter.WebtoonViewHolder>() {

    inner class WebtoonViewHolder(
        private val binding: WebtoonListItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(imgUrl: String) {
            if (context != null) {
                Glide.with(context)
                    .load(imgUrl)
                    .into(binding.imageView)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WebtoonViewHolder {
        val view = WebtoonListItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return WebtoonViewHolder(view)
    }

    override fun onBindViewHolder(holder: WebtoonViewHolder, position: Int) {
        holder.bind(imageList[position].imgUrl)
    }

    override fun getItemCount(): Int = imageList.size
}