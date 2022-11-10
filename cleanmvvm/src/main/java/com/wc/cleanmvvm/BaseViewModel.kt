package com.wc.cleanmvvm

import android.util.Log
import android.view.View
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

/**
 * ProjectName:  LeadingEdge
 * Desc:ViewModel基类
 * Author: weicheng
 * Date:  2022/10/31
 */



open class BaseViewModel : ViewModel() , LifecycleEventObserver , View.OnClickListener{

    val TAG:String = this.javaClass.simpleName

    val status = MutableLiveData<Int>(NORMAL)
    val loadMoreState = MutableLiveData<String>(LOADMORE_STATE_COMPLETE)

    var pageNo = -1
    var pageSize = 6

    /**
     * 生命周期改变时逻辑处理，子类重写
     * */
    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when(event){
            Lifecycle.Event.ON_CREATE -> onCreate()
            Lifecycle.Event.ON_START -> onStart()
            Lifecycle.Event.ON_RESUME -> onResume()
            Lifecycle.Event.ON_PAUSE -> onPause()
            Lifecycle.Event.ON_STOP -> onStop()
            Lifecycle.Event.ON_DESTROY -> onDestroy()
        }
    }

    override fun onClick(v: View?) {}
    open fun onCreate() {}
    open fun onStart() {}
    open fun onResume() {}
    open fun onPause() {}
    open fun onStop() {}
    open fun onDestroy() {}

    /**
     * 请求详情数据并更新到指定liveData，统一错误处理逻辑
     * */
    fun <T> infoRequest(
        liveData: MutableLiveData<T>,
        request: suspend CoroutineScope.() -> Response<T>,
        err: (String) -> Unit = {},
        end: () -> Unit = {},
    ){
        viewModelScope.launch {
            try{
                status.postValue(LOADING)
                val response = request()
                if(response.code == SUCCEED){
                    status.postValue(NORMAL)
                    liveData.postValue(response.data)
                }else{
                    status.postValue(ERROR)
                    error(response.msg)
                }
            }catch (e: Exception){
                Log.e(TAG,"error",e)
                status.postValue(ERROR)
                error(e.message ?: "")
            }finally {
                end()
            }
        }
    }

    /**
     * 请求详情数据并更新到指定liveData，统一错误处理逻辑
     * */
    fun <T> listRequest(
        listData: ObservableArrayList<T>,
        request: suspend CoroutineScope.() -> Response<List<T>>,
        isLoadMore:Boolean = false,
        err: (String) -> Unit = {},
        end: () -> Unit = {},
    ){
        if(isLoadMore && (loadMoreState.value.equals(LOADMORE_STATE_END) || loadMoreState.value.equals(LOADMORE_STATE_LOADING))) return
        viewModelScope.launch {
            try{
                if(pageNo < 0)  status.postValue(LOADING)
                else if(isLoadMore) loadMoreState.value = LOADMORE_STATE_LOADING
                val response = request()
                if(response.code == SUCCEED){
                    status.postValue(NORMAL)
                    if(!isLoadMore) listData.clear()
                    else {
                        if(response.data.size < pageSize) loadMoreState.value = LOADMORE_STATE_END
                        else loadMoreState.value = LOADMORE_STATE_COMPLETE
                    }
                    listData.addAll(response.data)
                    pageNo++
                }else{
                    status.postValue(ERROR)
                    if(isLoadMore) loadMoreState.value = LOADMORE_STATE_COMPLETE
                    error(response.msg)
                }
            }catch (e: Exception){
                Log.e(TAG,"error",e)
                status.postValue(ERROR)
                if(isLoadMore) loadMoreState.value = LOADMORE_STATE_COMPLETE
                error(e.message ?: "")
            }finally {
                end()
            }
        }
    }

}