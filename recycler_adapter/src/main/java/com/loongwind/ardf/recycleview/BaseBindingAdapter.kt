package com.loongwind.ardf.recycleview

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableList
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
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











    interface LoadMoreCallback{
        fun onLoadMore()
    }

    /**监听滑动到底部*/
    class OnLoadModeCallback(val loadModeCallback: LoadMoreCallback)
        : RecyclerView.OnScrollListener() {
        /**用来标记是否正在向上滑动*/
        private var isSlidingUpward = false

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            val manager = recyclerView.layoutManager as LinearLayoutManager?
            if (newState == RecyclerView.SCROLL_STATE_IDLE) { // 当不滑动时
                //获取最后一个完全显示的itemPosition
                val lastItemPosition = manager!!.findLastCompletelyVisibleItemPosition()
                val itemCount = manager.itemCount
                // 判断是否滑动到了最后一个item，并且是向上滑动
                if (lastItemPosition == itemCount - 1 /*&& isSlidingUpward*/) {
                    //加载更多
                    loadModeCallback.onLoadMore()
                }
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            // 大于0表示正在向上滑动，小于等于0表示停止或向下滑动
            isSlidingUpward = dy > 0
        }

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




