package com.genband.mobilesdkdemo.repository

import com.genband.mobilesdkdemo.model.Accounts
import com.genband.mobilesdkdemo.source.FirebaseAccountsDataSource


class AccountsRepository() : IAccountsRepository {

    private val accountsDataSource = FirebaseAccountsDataSource()


    private val TAG = "AccountsRepository"
    override fun getAccounts(callback: (accountList: Accounts) -> Unit) {
        accountsDataSource.getAccounts{
            callback(it)
        }
    }


}