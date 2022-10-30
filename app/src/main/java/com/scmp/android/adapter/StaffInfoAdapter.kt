package com.scmp.android.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.scmp.android.databinding.LoadMoreItemBinding
import com.scmp.android.databinding.StaffInfoItemBinding
import com.scmp.android.model.StaffInfo

class StaffInfoAdapter(private val context: Context) : ListAdapter<Any, BindingViewHolder>(object : DiffUtil.ItemCallback<Any>() {
    override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
        return areItemsTheSame(oldItem,newItem)
    }

}) {
    enum class ItemType {
        STAFF_ITEM,
        LOAD_MORE_ITEM
    }

    override fun onBindViewHolder(holder: BindingViewHolder, position: Int) {
        when(getItem(position)) {
            is StaffInfo -> {
                val binding = holder.binding as StaffInfoItemBinding
                val staffInfo = getItem(position) as StaffInfo
                binding.name.text = staffInfo.name
                binding.year.text = staffInfo.year.toString()
            }
            else -> {

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder {
        when(viewType) {
             ItemType.STAFF_ITEM.ordinal -> {
                 val binding = StaffInfoItemBinding.inflate(LayoutInflater.from(context),parent,false)
                 return BindingViewHolder(binding)
             }
            else -> {
                val binding = LoadMoreItemBinding.inflate(LayoutInflater.from(context),parent,false)
                return BindingViewHolder(binding)
            }
        }
    }


    override fun getItemViewType(position: Int): Int {
        return when(getItem(position)) {
            is StaffInfo -> ItemType.STAFF_ITEM.ordinal
            else -> ItemType.LOAD_MORE_ITEM.ordinal
        }
    }
}