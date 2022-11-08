package com.wc.basic

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData

/**
 * ProjectName:  LeadingEdge
 * Desc:LifecycleOwner扩展函数
 * Author: weicheng
 * Date:  2022/10/31
 */
fun <T> LifecycleOwner.observe(liveData:LiveData<T>, observer:(t:T) -> Unit){
    liveData.observe(this,{observer(it)})
}