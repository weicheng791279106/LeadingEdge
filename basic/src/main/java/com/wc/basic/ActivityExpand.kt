package com.wc.basic

import android.view.LayoutInflater
import android.view.View
import androidx.activity.ComponentActivity
import androidx.annotation.MainThread
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

/**
 *
 * ProjectName:  LeadingEdge
 * Desc: Activity扩展函数
 * Author: weicheng
 * Date:  2022/10/31
 *
 */
fun <T : View> ComponentActivity.id(id:Int) = lazy {
    findViewById<T>(id)
}



