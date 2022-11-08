package com.wc.coroutine

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.wc.basic.liveData
import com.wc.basic.observe
import com.wc.cleanmvvm.BaseActivity
import com.wc.cleanmvvm.BaseViewModel
import com.wc.cleanmvvm.R

class MainActivity : BaseActivity<MainViewModel>(R.layout.activity_main){

    val tv: TextView by clickId(R.id.tv)

    override fun init() {
        observe(mViewModel.text) {
            tv.text = it
        }
    }

}

class MainViewModel : BaseViewModel(){

    val text by liveData<String>("hello world")

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.tv -> text.postValue("you had click this text !")
        }
    }

}