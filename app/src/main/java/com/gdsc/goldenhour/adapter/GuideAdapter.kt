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
) : RecyclerView.Adapter<GuideAdapter.GuideViewHolder>() {

    // 커스텀 리스너 인터페이스 정의
    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    // 전달된 리스너 객체를 저장할 변수 정의
    private var mListener: OnItemClickListener? = null
    fun setMyItemClickListener(listener: OnItemClickListener){
        mListener = listener
    }

    // 외부 변수 mListener를 참조하기 위해 inner class 사용
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

        val textView: TextView = itemView.findViewById(R.id.item_title)
        val imageView: ImageView = itemView.findViewById(R.id.item_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GuideViewHolder {
        val adapterLayout =
            LayoutInflater.from(parent.context).inflate(R.layout.guide_list_item, parent, false)
        return GuideViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: GuideViewHolder, position: Int) {
        val item = dataset[position]
        holder.textView.text = context?.resources?.getString(item.stringResourceId)
        holder.imageView.setImageResource(item.imageResourceId)
    }

    override fun getItemCount(): Int = dataset.size
}