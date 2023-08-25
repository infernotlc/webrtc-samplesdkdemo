package com.genband.mobilesdkdemo.model

data class Accounts(
    val accountNames : ArrayList<String> ?= arrayListOf(),
    val userSetList : ArrayList<AccountsData> ?= arrayListOf()

)
