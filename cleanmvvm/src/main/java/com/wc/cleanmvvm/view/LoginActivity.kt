package com.wc.cleanmvvm.view

import android.view.View
import androidx.lifecycle.MutableLiveData
import com.wc.cleanmvvm.*
import com.wc.cleanmvvm.MainViewModel
import com.wc.cleanmvvm.databinding.ActivityLoginBinding

/**
 * ProjectName:  LeadingEdge
 * Desc:登录/详情页演示
 * Author: weicheng
 * Date:  2022/11/2
 *
 */
class LoginActivity : BaseActivity<ActivityLoginBinding, LoginViewModel>(){}

class LoginViewModel : BaseViewModel(){

    val userInfo = MutableLiveData<UserInfo>(UserInfo("click to start request"))

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.tv -> infoRequest(userInfo, request = { api.getUserData() })
        }
    }

}