package com.gdsc.goldenhour.view.checklist.normal.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gdsc.goldenhour.databinding.NormalGoodsItemBinding
import com.gdsc.goldenhour.network.model.Goods

class GoodsAdapter(
    private val goods: MutableList<Goods>
): RecyclerView.Adapter<GoodsAdapter.GoodsViewHolder>() {

    interface OnItemClickListener{
        fun onItemClick(pos: Int)
    }

    interface OnItemLongClickListener{
        fun onItemLongClick(pos: Int)
    }

    private var clickListener: OnItemClickListener? = null
    fun setMyItemClickListener(listener: OnItemClickListener){
        clickListener = listener
    }

    private var longClickListener: OnItemLongClickListener? = null
    fun setMyItemLongClickListener(listener: OnItemLongClickListener){
        longClickListener = listener
    }

    inner class GoodsViewHolder(
        private val binding: NormalGoodsItemBinding
    ): RecyclerView.ViewHolder(binding.root) {
        init {
            val itemView = binding.root
            itemView.setOnClickListener {
                val pos = adapterPosition
                if(pos != RecyclerView.NO_POSITION){
                    clickListener?.onItemClick(pos)
                }
            }
            itemView.setOnLongClickListener {
                val pos = adapterPosition
                if(pos != RecyclerView.NO_POSITION){
                    longClickListener?.onItemLongClick(pos)
                }
                return@setOnLongClickListener true
            }
        }

        fun bind(item: Goods){
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

    fun addItem(item: Goods){
        goods.add(item)
    }

    fun updateItem(pos: Int, name: String){
        goods[pos].name = name
    }

    fun deleteItem(pos: Int){
        goods.removeAt(pos)
    }

    fun getItem(pos: Int): Goods {
        return goods[pos]
    }
}