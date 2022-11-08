package com.loongwind.ardf.recycleview

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.*
import androidx.recyclerview.widget.RecyclerView

class BaseBindingAdapter<T:Any, BINDING : ViewDataBinding> (val layoutRes:Int):
    RecyclerView.Adapter<BindingViewHolder<T>>() {

    val TYPE_HEADER = -1000
    val TYPE_FOOTER = -2000

    private var listener: ObserverListChangeListener<T>? = null
    var itemViewTypeCreator: ItemViewTypeCreator? = null
    var listClick:ListClick<T>? = null
    open var headers: ObservableArrayList<ViewDataBinding> = ObservableArrayList()
    open var footers:ObservableArrayList<ViewDataBinding> = ObservableArrayList()
    val listChangeCallback = ListChangeCallback(this)

    init {
        headers.addOnListChangedCallback(listChangeCallback)
        footers.addOnListChangedCallback(listChangeCallback)
    }

    /**
     * 列表数据
     */
    var data: List<T>? = null
        @SuppressLint("NotifyDataSetChanged")
        set(data) {
            field = data
            // 判断如果是 ObservableList 类型，则为其添加 changeCallback 回调
            if (data is ObservableList<*>) {
                // 如果 listener 为空则创建 ObserverListChangeListener 对象，传入当前 Adapter
                if (listener == null) {
                    listener = ObserverListChangeListener(this)
                }
                // 将已添加的 listener 移除，防止添加多个导致重复回调
                (data as ObservableList<T>).removeOnListChangedCallback(listener)

                // 设置 List 数据改变回调
                data.addOnListChangedCallback(listener)
            }
            // 刷新界面数据
            notifyDataSetChanged()
        }

    fun getItem(position: Int): T? = data?.getOrNull(position)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder<T> {
        var type = 0
        var position = 0
        if(viewType <= TYPE_FOOTER){
            type = TYPE_FOOTER;
            position = TYPE_FOOTER - viewType;
        } else if(viewType <= TYPE_HEADER){
            type = TYPE_HEADER;
            position = TYPE_HEADER - viewType;
        } else{
            type = viewType;
        }

        when(type){
            TYPE_HEADER -> return BindingViewHolder<T>(headers.get(position))
            TYPE_FOOTER -> return BindingViewHolder<T>(footers.get(position))
            else -> {
                val layout = itemViewTypeCreator?.getItemLayout(viewType) ?: layoutRes
                val binding = DataBindingUtil.inflate<BINDING>(LayoutInflater.from(parent.context), layout, parent, false)
                val holder = BindingViewHolder<T>(binding)
                bindClick(holder, binding)
                return holder
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        if(position < headers.size){
            return TYPE_HEADER - position;
        } else if(position < getItemCount() - footers.size){
            val pos = position - headers.size
            return itemViewTypeCreator?.getItemViewType(pos, getItem(pos)) ?: super.getItemViewType(pos)
        } else {
            return TYPE_FOOTER - position + getItemCount() - footers.size;
        }
    }

    override fun onBindViewHolder(holder: BindingViewHolder<T>, position: Int) {
        if(getItemViewType(position) > TYPE_HEADER) holder.bind(getItem(position - headers.size))
    }

    override fun getItemCount(): Int = headers.size + footers.size + (data?.size ?: 0)

    /**设置上监听器*/
    protected fun bindClick(holder: BindingViewHolder<*>, binding: ViewDataBinding) {
        bindViewClick(binding.root,binding){
            val position = holder.layoutPosition - headers.size
            if(it == binding.root) listClick?.onItemClick(it,getItem(position)!!,position)
            else listClick?.onViewClick(it,getItem(position)!!,position)
        }
    }

    /**把clickable的view都设置监听*/
    fun bindViewClick(v:View,binding: ViewDataBinding,listener:View.OnClickListener){
        if(v.isClickable || v == binding.root) v.setOnClickListener(listener)
        if(v is ViewGroup) for(i in 0 until v.childCount) bindViewClick(v.getChildAt(i),binding,listener)
    }

    /**多布局创建*/
    interface ItemViewTypeCreator{
        /**
         * 通过 item 下标和数据返回布局类型
         * @param position item 下标
         * @param item item 数据
         * @return item 布局类型
         */
        fun getItemViewType(position: Int, item: Any?) : Int

        /**
         * 通过 item 布局类型返回布局资源 id
         * @param viewType item 数据类型
         * @return item 布局资源 id
         */
        fun getItemLayout(viewType: Int) : Int
    }

    /**点击监听*/
    interface ListClick<T>{
        /**Item点击*/
        fun onItemClick(v:View,item:T,pos: Int){}
        /**Item view点击*/
        fun onViewClick(v:View,item:T,pos: Int){}
    }

}

/**header & footer数据变动监听*/
class ListChangeCallback(val adapter:RecyclerView.Adapter<*>) : ObservableList.OnListChangedCallback<ObservableArrayList<ViewDataBinding>>(){
    override fun onChanged(sender: ObservableArrayList<ViewDataBinding>?) {
        adapter.notifyDataSetChanged()
    }
    override fun onItemRangeRemoved(sender: ObservableArrayList<ViewDataBinding>, positionStart: Int, itemCount: Int) {}
    override fun onItemRangeMoved(sender: ObservableArrayList<ViewDataBinding>, fromPosition: Int, toPosition: Int, itemCount: Int) {}
    override fun onItemRangeInserted(sender: ObservableArrayList<ViewDataBinding>, positionStart: Int, itemCount: Int) {}
    override fun onItemRangeChanged(sender: ObservableArrayList<ViewDataBinding>, positionStart: Int, itemCount: Int) {}
}




