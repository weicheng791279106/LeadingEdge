package com.wc.cleanmvvm

import android.os.Bundle
import android.os.PersistableBundle
import android.provider.ContactsContract
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.wc.basic.id
import java.lang.reflect.ParameterizedType

/**
 * ProjectName:  LeadingEdge
 * Desc:Activity基类
 * Author: weicheng
 * Date:  2022/10/31
 *
 */
abstract class BaseActivity<VB:ViewDataBinding , VM:BaseViewModel>() : AppCompatActivity(){

    val fl_root: FrameLayout by id(R.id.fl_root)
    val tv_status: TextView by id(R.id.tv_status)

    lateinit var mViewModel: VM
    lateinit var mBinding: VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
        mBinding = getViewDataBinding()
        mBinding.setLifecycleOwner(this)
        fl_root.addView(mBinding.root,0) //内容view添加到最底层让状态View在上面优先显示
        mViewModel = getViewModel()!!
        lifecycle.addObserver(mViewModel)
        mBinding.setVariable(BR.vm,mViewModel)
        mViewModel.status.observe(this){
            when(it){
                NORMAL -> {
                    tv_status.visibility = View.GONE
                }
                ERROR -> {
                    tv_status.visibility = View.VISIBLE
                    tv_status.text = "error"
                }
                LOADING -> {
                    tv_status.visibility = View.VISIBLE
                    tv_status.text = "loading..."
                }
            }
        }
        mBinding.root.post(){ initView() }
    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycle.removeObserver(mViewModel)
    }

    open fun initView() {}

    /**
     * 获取ViewModel泛型的实例
     * */
    private fun getViewModel(): VM? {
        val type = javaClass.genericSuperclass
        if (type != null && type is ParameterizedType) {
            val actualTypeArguments = type.actualTypeArguments
            val tClass = actualTypeArguments[1]
            return ViewModelProvider(
                this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(application)
            ).get(tClass as Class<VM>)
        }
        return null
    }

    /**
     * 获取ViewDataBinding实例
     * */
    private fun getViewDataBinding():VB{
        val type = javaClass.genericSuperclass
        val actualTypeArguments = (type as ParameterizedType).actualTypeArguments
        val tClass = actualTypeArguments[0]
        return (tClass as? Class<*>)// 获取 ViewDataBinding 泛型实际类型
            ?.getMethod("inflate", LayoutInflater::class.java) // 反射获取 inflate 方法
            ?.invoke(null, LayoutInflater.from(this)) as VB // 通过反射调用 inflate 方法
    }

}

