package com.gdsc.goldenhour.view.checklist.normal.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gdsc.goldenhour.databinding.NormalContactItemBinding
import com.gdsc.goldenhour.network.model.Contact

class NormalContactAdapter(
    private val contacts: MutableList<Contact>
): RecyclerView.Adapter<NormalContactAdapter.ContactViewHolder>() {
    fun addItem(item: Contact){
        contacts.add(item)
    }

    class ContactViewHolder(
        private val binding: NormalContactItemBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Contact){
            binding.tvName.text = item.name
            binding.tvPhoneNumber.text = item.phoneNumber
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val view = NormalContactItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)
        return ContactViewHolder(view)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        holder.bind(contacts[position])
    }

    override fun getItemCount(): Int = contacts.size
}