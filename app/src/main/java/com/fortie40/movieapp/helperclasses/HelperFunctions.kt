package com.fortie40.movieapp.helperclasses

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner

object HelperFunctions {
    fun viewInflater(parent: ViewGroup, layout: Int): ViewDataBinding {
        val layoutInflater = LayoutInflater.from(parent.context)
        return DataBindingUtil.inflate(layoutInflater, layout, parent, false)
    }

    fun clearViewModel(owner: ViewModelStoreOwner, key: String) {
        ViewModelProvider(owner).get(key, EmptyViewModel::class.java)
    }
}