package com.wc.cleanmvvm.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.annotation.NonNull
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableArrayList
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.loongwind.ardf.recycleview.BaseBindingAdapter
import com.loongwind.ardf.recycleview.BaseBindingAdapter.ListClick
import com.wc.basic.liveData
import com.wc.basic.observe
import com.wc.cleanmvvm.*
import com.wc.cleanmvvm.databinding.*

class MultiUsersActivity : BaseActivity<ActivityMultiUsersBinding, MultiUsersViewModel>(){

    override fun initView() {
        /*添加header*/
        val binding = HeaderUserBinding.inflate(LayoutInflater.from(this))
        binding.vm = mViewModel
        (mBinding.rcvUsers.adapter as BaseBindingAdapter<*,*>).headers.add(binding)
    }

}

class MultiUsersViewModel : BaseViewModel() , ListClick<UserInfo> {

    val users = ObservableArrayList<UserInfo>()
    val headerText = "User Header"

    val itemView = object : BaseBindingAdapter.ItemViewTypeCreator {
        override fun getItemViewType(position: Int, item: Any?): Int = if (position%2 == 0) 1 else 0
        override fun getItemLayout(viewType: Int): Int  = if(viewType == 1) R.layout.item_user else R.layout.item_user2
    }

    override fun onResume() = listRequest(users,{api.getUsers()})

    override fun onClick(v: View?) =
        Toast.makeText(v?.context,"Header Click",Toast.LENGTH_SHORT).show()

    override fun onItemClick(v: View, item: UserInfo, pos: Int) =
        Toast.makeText(v.context,"Item点击 $pos",Toast.LENGTH_SHORT).show()

    override fun onViewClick(v: View, item: UserInfo, pos: Int) =
        Toast.makeText(v.context,"Item view 点击 $pos",Toast.LENGTH_SHORT).show()

}












