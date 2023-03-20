package com.gdsc.goldenhour.view.checklist.disaster.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gdsc.goldenhour.databinding.DisasterContactItemBinding
import com.gdsc.goldenhour.network.model.Contact

class DisasterContactAdapter(
    private val contacts: List<Contact>
): RecyclerView.Adapter<DisasterContactAdapter.ContactViewHolder>() {
    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    private var mListener: OnItemClickListener? = null
    fun setMyItemClickListener(listener: OnItemClickListener){
        mListener = listener
    }

    inner class ContactViewHolder(
        private val binding: DisasterContactItemBinding
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

        fun bind(item: Contact){
            binding.tvName.text = item.name
            binding.tvPhoneNumber.text = item.phoneNumber
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val view = DisasterContactItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)
        return ContactViewHolder(view)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        holder.bind(contacts[position])
    }

    override fun getItemCount(): Int = contacts.size
}