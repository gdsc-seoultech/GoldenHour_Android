package com.gdsc.goldenhour.view.checklist.disaster.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gdsc.goldenhour.databinding.ContactListItemBinding
import com.gdsc.goldenhour.network.model.Contact

class ContactAdapter(
    private val data: List<Contact>
): RecyclerView.Adapter<ContactAdapter.ContactViewHolder>() {
    class ContactViewHolder(
        private val binding: ContactListItemBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Contact){
            binding.name.text = item.name
            binding.phoneNumber.text = item.phoneNumber
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val view = ContactListItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)
        return ContactViewHolder(view)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size
}