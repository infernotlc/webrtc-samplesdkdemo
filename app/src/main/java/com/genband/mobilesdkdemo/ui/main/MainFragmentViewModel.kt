package com.genband.mobilesdkdemo.ui.main


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.genband.mobile.ServiceProvider
import com.genband.mobilesdkdemo.model.Accounts
import com.genband.mobilesdkdemo.repository.AccountsRepository


class MainFragmentViewModel(private val serviceProvider: ServiceProvider) : ViewModel(){

    private var accountsRepository = AccountsRepository()
    val accounts = MutableLiveData<Accounts>()

    fun getAccountsFirebase(){
        accountsRepository.getAccounts {
            accounts.value = it

        }
    }




}