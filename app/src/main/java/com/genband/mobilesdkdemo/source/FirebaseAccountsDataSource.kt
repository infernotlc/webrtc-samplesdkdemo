package com.genband.mobilesdkdemo.source

import com.genband.mobilesdkdemo.model.Accounts
import com.genband.mobilesdkdemo.model.AccountsData
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase

class FirebaseAccountsDataSource {
    fun getAccounts(callback: (Accounts) -> Unit) {
        val ref = FirebaseDatabase.getInstance().getReference("/userSetList")
        val accountDataList = ArrayList<AccountsData>()
        val accountNameList = ArrayList<String>()
        lateinit var accounts : Accounts

        ref.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val account = snapshot.getValue(AccountsData::class.java)
                val accountName = snapshot.ref.key
                account?.let { accountDataList.add(it) }
                accountName?.let { accountNameList.add(it) }
                accounts = Accounts(accountNameList, accountDataList)
                callback(accounts)
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
}