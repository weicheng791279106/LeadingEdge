package com.wc.leadingedge.view

import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import com.wc.basic.id
import com.wc.basic.observe
import com.wc.cleanmvvm.BaseActivity
import com.wc.cleanmvvm.BaseViewModel
import com.wc.leadingedge.R
import com.wc.leadingedge.UserInfo
import com.wc.leadingedge.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.util.*

/**
 * ProjectName:  LeadingEdge
 * Desc:主页面
 * Author: weicheng
 * Date:  2022/10/31
 */
class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>(){}

class MainViewModel : BaseViewModel(){

    val userInfo = MutableLiveData<UserInfo>(UserInfo("click to start request"))

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.bt_login -> v.context.startActivity(Intent(v.context, LoginActivity::class.java))
            R.id.bt_users -> v.context.startActivity(Intent(v.context, UsersActivity::class.java))
            R.id.bt_multi_users -> v.context.startActivity(Intent(v.context, MultiUsersActivity::class.java))
        }
    }

}

