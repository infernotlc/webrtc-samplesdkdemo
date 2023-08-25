package com.genband.mobilesdkdemo.ui.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.genband.mobile.ServiceProvider

import com.genband.mobilesdkdemo.ui.settings.SettingsViewModel

class SettingsViewModelFactory(private val serviceProvider: ServiceProvider) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SettingsViewModel(serviceProvider) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}