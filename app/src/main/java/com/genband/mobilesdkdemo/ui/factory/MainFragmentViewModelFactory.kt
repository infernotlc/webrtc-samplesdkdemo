package com.genband.mobilesdkdemo.ui.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.genband.mobilesdkdemo.ui.main.MainFragmentViewModel
import com.genband.mobile.ServiceProvider

class MainFragmentViewModelFactory(private val serviceProvider: ServiceProvider) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainFragmentViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainFragmentViewModel(serviceProvider) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")

    }
}