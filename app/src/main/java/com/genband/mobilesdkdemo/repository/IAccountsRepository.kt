package com.genband.mobilesdkdemo.repository

import com.genband.mobilesdkdemo.model.Accounts

interface IAccountsRepository {
    fun getAccounts(callback : (accountList: Accounts) -> Unit)
}