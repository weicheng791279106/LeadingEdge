package com.wc.cleanmvvm.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.annotation.NonNull
import androidx.databinding.BindingAdapter
import androidx.databinding.ObservableArrayList
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.loongwind.ardf.recycleview.BaseBindingAdapter
import com.loongwind.ardf.recycleview.BaseBindingAdapter.ListClick
import com.wc.basic.observe
import com.wc.cleanmvvm.*
import com.wc.cleanmvvm.databinding.ActivityMainBinding
import com.wc.cleanmvvm.databinding.ActivityUsersBinding
import com.wc.cleanmvvm.databinding.ItemUserBinding

class UsersActivity : BaseActivity<ActivityUsersBinding, UsersViewModel>(){}

class UsersViewModel : BaseViewModel() , ListClick<UserInfo> {

    val users = ObservableArrayList<UserInfo>()

    override fun onResume() {
        listRequest(users,{api.getUsers()})
    }

    override fun onItemClick(v: View, item: UserInfo, pos: Int) {
        Toast.makeText(v.context,"Item点击 $pos",Toast.LENGTH_SHORT).show()
    }

    override fun onViewClick(v: View, item: UserInfo, pos: Int) {
        Toast.makeText(v.context,"Item view 点击 $pos",Toast.LENGTH_SHORT).show()
    }

}












