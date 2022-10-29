package com.scmp.android.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.scmp.android.databinding.StaffInfoItemBinding
import com.scmp.android.model.StaffInfo

class StaffInfoAdapter(private val context: Context) : PagingDataAdapter<StaffInfo, BindingViewHolder>(object : DiffUtil.ItemCallback<StaffInfo>() {
    override fun areItemsTheSame(oldItem: StaffInfo, newItem: StaffInfo): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: StaffInfo, newItem: StaffInfo): Boolean {
        return oldItem.id == newItem.id
    }

}) {
    override fun onBindViewHolder(holder: BindingViewHolder, position: Int) {
       getItem(position)?.let {
           val binding = holder.binding as StaffInfoItemBinding
           binding.name.text = it.name
           binding.year.text = it.year.toString()
       }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder {
        val binding = StaffInfoItemBinding.inflate(LayoutInflater.from(context),parent,false)
        return BindingViewHolder(binding)
    }
}