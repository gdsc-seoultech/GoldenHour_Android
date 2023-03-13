package com.gdsc.goldenhour.view.guide.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gdsc.goldenhour.databinding.GuideListItemBinding
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
    fun setMyItemClickListener(listener: OnItemClickListener) {
        mListener = listener
    }

    // mListener를 참조하기 위해 inner class 사용
    inner class GuideViewHolder(
        private val binding: GuideListItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            // 아이템 클릭 이벤트 핸들러에서 추상 메서드 onItemClick 호출 (클릭된 위치 전달)
            binding.root.setOnClickListener {
                val pos = adapterPosition
                if (pos != RecyclerView.NO_POSITION) {
                    mListener?.onItemClick(pos)
                }
            }
        }

        fun bind(item: Guide) {
            binding.itemTitle.text = item.name

            if (context != null) {
                Glide.with(context)
                    .load(item.imgUrl)
                    .into(binding.itemImage)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GuideViewHolder {
        val view = GuideListItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return GuideViewHolder(view)
    }

    override fun onBindViewHolder(holder: GuideViewHolder, position: Int) {
        holder.bind(guideList[position])
    }

    override fun getItemCount(): Int = guideList.size
}