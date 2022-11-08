package com.wc.basic

import androidx.databinding.ObservableArrayList
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * ProjectName:  LeadingEdge
 * Desc:ViewModel扩展函数
 * Author: weicheng
 * Date:  2022/11/1
 *
 */
fun <T : Any> ViewModel.liveData(data:T?) = lazy {
    val liveData: MutableLiveData<T> = MutableLiveData<T>()
    liveData.postValue(data)
    liveData
}
