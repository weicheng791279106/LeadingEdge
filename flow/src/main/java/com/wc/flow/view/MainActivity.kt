package com.wc.flow.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.annotation.MainThread
import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.CreationExtras
import com.wc.basic.id
import com.wc.flow.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val mainViewModel:MainViewModel by viewModels()
    private val tv:TextView by id(R.id.tv)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tv.text = "hello world"


    }
}

class MainViewModel : ViewModel(){

    val mainText = MutableLiveData<String>();



}


