package com.loongwind.ardf.recycleview

import android.util.Log
import android.view.View
import androidx.annotation.LayoutRes
import androidx.databinding.BindingAdapter
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.loongwind.ardf.recycleview.BaseBindingAdapter.ListClick

@BindingAdapter(value = ["data", "itemLayout", "listClick","itemViewType"], requireAll = false)
fun setData(
    recyclerView: RecyclerView,
    data: List<Any>?,
    @LayoutRes itemLayout: Int,
    listClick: ListClick<*>,
    itemViewTypeCreator: BaseBindingAdapter.ItemViewTypeCreator?,
) {
    val adapter = recyclerView.adapter
    if (adapter == null) {
        val simpleBindingAdapter = BaseBindingAdapter<Any,ViewDataBinding>(itemLayout)
        simpleBindingAdapter.data = data
        simpleBindingAdapter.listClick = listClick as ListClick<Any>
        simpleBindingAdapter.itemViewTypeCreator = itemViewTypeCreator
        recyclerView.adapter = simpleBindingAdapter
    } else if (adapter is BaseBindingAdapter<*, *>) {
        (adapter as BaseBindingAdapter<Any, ViewDataBinding>).data = data
        adapter.itemViewTypeCreator = itemViewTypeCreator
        adapter.listClick = listClick as ListClick<Any>
    }
}
