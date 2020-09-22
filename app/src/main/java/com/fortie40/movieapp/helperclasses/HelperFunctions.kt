package com.fortie40.movieapp.helperclasses

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

object HelperFunctions {
    fun viewInflater(parent: ViewGroup, layout: Int): ViewDataBinding {
        val layoutInflater = LayoutInflater.from(parent.context)
        return DataBindingUtil.inflate(layoutInflater, layout, parent, false)
    }
}