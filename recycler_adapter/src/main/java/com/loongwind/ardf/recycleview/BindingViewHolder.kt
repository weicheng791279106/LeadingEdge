package com.loongwind.ardf.recycleview

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

class BindingViewHolder<T>(val binding: ViewDataBinding)
    : RecyclerView.ViewHolder(binding.root){

    fun bind(t: T?) {
        binding.setVariable(BR.item, t)
    }

}
