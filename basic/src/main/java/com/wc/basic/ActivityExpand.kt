package com.wc.basic

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.ComponentActivity
import androidx.databinding.ViewDataBinding

/**
 *
 * ProjectName:  LeadingEdge
 * Desc: Activity扩展函数
 * Author: weicheng
 * Date:  2022/10/31
 *
 */

/**
 * findViewById简化
 * */
fun <T : View> ComponentActivity.id(id:Int) = lazy {
    findViewById<T>(id)
}

/**
 * 获取binding
 * */
fun <T : ViewDataBinding> ComponentActivity.getBinding(
    t:Class<T> ,
    br:Int,
    vm:Any,
    viewGroup:ViewGroup? = null,
    attach:Boolean = false
) : T {
    val binding:T = Class.forName(t.name)
        ?.getMethod("inflate", LayoutInflater::class.java,ViewGroup::class.java,Boolean::class.java)
        ?.invoke(null, LayoutInflater.from(this),viewGroup,attach) as T
    binding.setVariable(br,vm)
    binding.setLifecycleOwner(this)
    return binding
}



