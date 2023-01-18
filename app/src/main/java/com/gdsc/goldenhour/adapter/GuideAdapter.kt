package com.gdsc.goldenhour.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gdsc.goldenhour.R
import com.gdsc.goldenhour.model.GuideItem

class GuideAdapter(
    private val context: Context?,
    private val dataset: List<GuideItem>
): RecyclerView.Adapter<GuideAdapter.GuideViewHolder>() {

    class GuideViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.item_title)
        val imageView: ImageView = view.findViewById(R.id.item_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GuideViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.guide_list_item, parent, false)
        return GuideViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: GuideViewHolder, position: Int) {
        val item = dataset[position]
        holder.textView.text = context?.resources?.getString(item.stringResourceId)
        holder.imageView.setImageResource(item.imageResourceId)
    }

    override fun getItemCount(): Int = dataset.size
}