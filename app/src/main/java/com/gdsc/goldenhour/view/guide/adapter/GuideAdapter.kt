package com.gdsc.goldenhour.view.guide.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gdsc.goldenhour.R
import com.gdsc.goldenhour.network.model.Guide

class GuideAdapter(
    private val context: Context?,
    private val guideList: List<Guide>
) : RecyclerView.Adapter<GuideAdapter.GuideViewHolder>() {

    // 커스텀 리스너 인터페이스 정의
    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    // 리스너 객체 초기화
    private var mListener: OnItemClickListener? = null
    fun setMyItemClickListener(listener: OnItemClickListener){
        mListener = listener
    }

    // mListener를 참조하기 위해 inner class 사용
    inner class GuideViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            // 아이템 클릭 이벤트 핸들러에서 추상 메서드 onItemClick 호출 (클릭된 위치 전달)
            itemView.setOnClickListener {
                val pos = adapterPosition
                if(pos != RecyclerView.NO_POSITION){
                    mListener?.onItemClick(pos)
                }
            }
        }

        val title: TextView = itemView.findViewById(R.id.item_title)
        val image: ImageView = itemView.findViewById(R.id.item_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GuideViewHolder {
        val adapterLayout =
            LayoutInflater.from(parent.context).inflate(R.layout.guide_list_item, parent, false)
        return GuideViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: GuideViewHolder, position: Int) {
        val item = guideList[position]
        holder.title.text = item.name

//        Glide.with(context)
//            .load(item.imgUrl)
//            .override(100)
//            .into(holder.image)
    }

    override fun getItemCount(): Int = guideList.size
}