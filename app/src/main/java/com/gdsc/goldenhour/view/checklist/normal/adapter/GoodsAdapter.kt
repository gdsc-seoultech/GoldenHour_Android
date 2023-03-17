package com.gdsc.goldenhour.view.checklist.normal.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gdsc.goldenhour.databinding.NormalGoodsItemBinding
import com.gdsc.goldenhour.network.model.Goods

class GoodsAdapter(
    private val goods: List<Goods>
): RecyclerView.Adapter<GoodsAdapter.GoodsViewHolder>() {

    interface OnItemClickListener{
        fun onItemClick(pos: Int)
    }

    private var mListener: OnItemClickListener? = null
    fun setMyItemClickListener(listener: OnItemClickListener){
        mListener = listener
    }

    inner class GoodsViewHolder(
        private val binding: NormalGoodsItemBinding
    ): RecyclerView.ViewHolder(binding.root) {
        init {
            val itemView = binding.root
            itemView.setOnClickListener {
                val pos = adapterPosition
                if(pos != RecyclerView.NO_POSITION){
                    mListener?.onItemClick(pos)
                }
            }
        }

        fun bind(item: Goods){
            binding.tvGoodsId.text = item.id.toString()
            binding.tvGoodsName.text = item.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GoodsViewHolder {
        val view = NormalGoodsItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return GoodsViewHolder(view)
    }

    override fun onBindViewHolder(holder: GoodsViewHolder, position: Int) {
        holder.bind(goods[position])
    }

    override fun getItemCount(): Int = goods.size
}