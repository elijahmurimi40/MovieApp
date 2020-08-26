package com.fortie40.movieapp.helperclasses

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ViewModelFactory(private val repository: Any, private val arg: Any? = null) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (arg == null)
            modelClass.getConstructor(repository::class.java).newInstance(repository)
        else
            modelClass.getConstructor(repository::class.java, arg::class.java).newInstance(repository, arg)
    }
}

class ViewModelFactoryD(private val repository: Any, private val arg: Int) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(repository::class.java, arg::class.java).newInstance(repository, arg)
    }
}