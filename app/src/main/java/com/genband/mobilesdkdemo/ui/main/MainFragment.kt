package com.genband.mobilesdkdemo.ui.main

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import com.genband.mobile.NotificationStates
import com.genband.mobilesdkdemo.App
import com.genband.mobilesdkdemo.R
import com.genband.mobilesdkdemo.ui.factory.MainFragmentViewModelFactory
import com.genband.mobilesdkdemo.databinding.FragmentMainBinding
import com.genband.mobilesdkdemo.helpers.CallConstants
import com.genband.mobilesdkdemo.helpers.SharedPrefsHelper
import com.genband.mobilesdkdemo.model.AccountsData
import com.genband.mobilesdkdemo.ui.call.CallActivity
import com.genband.mobilesdkdemo.ui.login.RegisterViewModel
import com.google.android.material.button.MaterialButton


class MainFragment : Fragment() {
    private val viewModel : MainFragmentViewModel by viewModels{
        MainFragmentViewModelFactory((activity?.application as App).serviceProvider)
    }

    private lateinit var binding: FragmentMainBinding
    private var accountData: AccountsData? = null
    private var accountsData : ArrayList<AccountsData> ?= arrayListOf()
    private var accountNames : List<String> ?= listOf()
    private val TAG = "MainFragment"
    private val connected = "CONNECTED"
    private val registered = "REGISTERED"
    private val unregistered = "UNREGISTERED"
    private val disconnected = "DISCONNECTED"
    private var calleeAdress : String? = null
    private var callerAdress : String? = null
    private var calleeList : List<AccountsData> ?= listOf()
    private var calleeAccountNames : ArrayList<String> ?= arrayListOf()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            accountData = it.getParcelable("accountsData")
            Log.d(TAG,"Arguments Values: ${accountData?.device_user}")
        }

        uiOperations()

    }

    private fun uiOperations(){
        selectOutgoingCallType()
        onStartCallClicked()
        observeConnectionStatus()
        selectCalleeAddress()
    }

    private fun selectOutgoingCallType(){
        //default call type
        binding.audioandVideoBtn.isChecked = true

        //selecting call type
        binding.callTypeToggleGroup.addOnButtonCheckedListener { group, checkedId, isChecked ->
            val checkedButton = group.findViewById<MaterialButton>(checkedId)
            if (isChecked){
                SharedPrefsHelper.putString(SharedPrefsHelper.CALL_TYPE,checkedButton.text.toString())
            }
        }
    }
    private fun observeConnectionStatus(){
        //printing observed states to ui
        RegisterViewModel.notificationStateChanged.observe(viewLifecycleOwner){
            val webSocketStatus = binding.textViewWebSocket
            val registrationStatus = binding.textViewRegistration
            val callerName = binding.textViewCaller
            val serverUrl = binding.textViewServerUrl

            callerName.text = accountData?.device_user
            serverUrl.text = accountData?.config?.webSocketServerIP

            if (it == NotificationStates.CONNECTED){
                Log.d(TAG,"Connected")
                webSocketStatus.text = connected
                registrationStatus.text = registered
                webSocketStatus.setTextColor(Color.GREEN)
                registrationStatus.setTextColor(Color.GREEN)
            } else{
                Log.d(TAG,"DisConnected")
                webSocketStatus.text = disconnected
                registrationStatus.text = unregistered
                webSocketStatus.setTextColor(Color.RED)
                registrationStatus.setTextColor(Color.RED)
            }
        }
    }
    private fun selectCalleeAddress(){
        viewModel.getAccountsFirebase()
        val calleeAdressSpinner = binding.calleeAdressSpinner
        viewModel.accounts.observe(viewLifecycleOwner){ it ->
            it.let { it ->
                accountNames = it.accountNames
                accountsData = it.userSetList

                calleeList = it.userSetList?.filter { it.config.restServerIP == accountData?.config?.restServerIP }
                calleeAccountNames = calleeList?.map { it.device_user }?.filterNot { it == accountData?.device_user } as ArrayList<String>
            }

            val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, calleeAccountNames as List<*> )
            calleeAdressSpinner.setAdapter(arrayAdapter)
            calleeAdressSpinner.isEnabled = true

            calleeAdressSpinner.setOnItemClickListener { adapterView, view, position, l ->
                calleeAdress = accountsData?.get(position).toString()
            }
        }
    }
    private fun onStartCallClicked(){
        binding.startCallButton.setOnClickListener{
            calleeAdress = binding.calleeAdressSpinner.text.toString()
            val intent = Intent(context,CallActivity::class.java)
            val user = accountData?.device_user
            intent.putExtra(CallConstants.CALL_INTENT_KEY, CallConstants.CALL_INTENT_OUTGOING_CALL)
            intent.putExtra("AccountData", user)
            intent.putExtra("Callee", calleeAdress)
            startActivity(intent)
        }
    }


}






