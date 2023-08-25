package com.genband.mobilesdkdemo.ui.login

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.genband.mobile.*
import com.genband.mobile.api.utilities.*
import com.genband.mobilesdkdemo.helpers.SharedPrefsHelper
import com.genband.mobilesdkdemo.model.Accounts
import com.genband.mobilesdkdemo.model.AccountsData
import com.genband.mobilesdkdemo.repository.AccountsRepository


class RegisterViewModel(private val serviceProvider: ServiceProvider) : ViewModel(){


    private val configuration: Configuration = Configuration.getInstance()
    private var accountsRepository = AccountsRepository()
    val accounts = MutableLiveData<Accounts>()
    private val TAG = "RegisterViewModel"


    val registrationStateChanged = MutableLiveData<RegistrationStates?>()
    val registrationDropped = MutableLiveData<MobileError?>()
    val onInternalError = MutableLiveData<MobileError?>()
    val registrationSuccess = MutableLiveData<Boolean>()
    val registrationFail = MutableLiveData<MobileError?>()

    companion object{
        val notificationStateChanged = MutableLiveData<NotificationStates?>()
    }
    fun getAccountsFirebase(){
        accountsRepository.getAccounts {
            accounts.value = it
        }
    }

    fun getMobileSdkVersion(): String {
        return serviceProvider.version
    }

    fun register(){

        val registrationService : RegistrationService = serviceProvider.registrationService

        val registrationApplicationListener = object : RegistrationApplicationListener{
            override fun registrationStateChanged(p0: RegistrationStates?) {
                registrationStateChanged.value=p0
            }
            override fun registrationDropped(p0: MobileError?) {
                registrationDropped.value = p0
            }
            override fun notificationStateChanged(p0: NotificationStates?) {
                notificationStateChanged.value = p0
                when(p0){
                    NotificationStates.CONNECTED -> SharedPrefsHelper.putString(SharedPrefsHelper.NOTIFICATION_STATE,"CONNECTED")
                    NotificationStates.DISCONNECTED -> SharedPrefsHelper.putString(SharedPrefsHelper.NOTIFICATION_STATE,"DISCONNECTED")
                    else -> SharedPrefsHelper.putString(SharedPrefsHelper.NOTIFICATION_STATE,"")
                }



            }
            override fun onInternalError(p0: MobileError?) {
                onInternalError.value = p0
            }
        }
        registrationService.setRegistrationApplicationListener(registrationApplicationListener)
        registrationService.registerToServer(3600, object : OnCompletionListener{
            override fun onSuccess() {
                registrationSuccess.value = true
                SharedPrefsHelper.putBoolean(SharedPrefsHelper.SESSION_TOKEN,true)
                Log.d(TAG,"Registration Success")
            }
            override fun onFail(p0: MobileError?) {
                registrationFail.value = p0
                Log.d(TAG,"Registration Fail: $p0")
            }
        })
    }
    fun setSharedPrefs(accountsData: AccountsData, useTurn: Boolean){
        SharedPrefsHelper.putString(SharedPrefsHelper.DEVICE_USER,accountsData.device_user)
        SharedPrefsHelper.putString(SharedPrefsHelper.DEVICE_PASSWORD,accountsData.device_pass)
        SharedPrefsHelper.putString(SharedPrefsHelper.DEFAULT_DOMAIN,accountsData.default_domain)
        SharedPrefsHelper.putString(SharedPrefsHelper.REST_IP,accountsData.config.restServerIP)
        SharedPrefsHelper.putString(SharedPrefsHelper.REST_PORT,accountsData.config.restServerPort)
        SharedPrefsHelper.putString(SharedPrefsHelper.SOCKET_IP,accountsData.config.webSocketServerIP)
        SharedPrefsHelper.putString(SharedPrefsHelper.SOCKET_PORT,accountsData.config.webSocketServerPort)
        SharedPrefsHelper.putString(SharedPrefsHelper.PUSH_URL,accountsData.pushServerURL)
        SharedPrefsHelper.putInt(SharedPrefsHelper.ICE_TIMEOUT,accountsData.config.ICECollectionTimeout)
        SharedPrefsHelper.putStringSet(SharedPrefsHelper.TURN_ADDRESS, accountsData.ICEServers.servers.toSet())
        SharedPrefsHelper.putBoolean(SharedPrefsHelper.USE_TURN,useTurn)
        SharedPrefsHelper.putBoolean(SharedPrefsHelper.SESSION_TOKEN,true)
    }


    fun setConfiguration(accountsData: AccountsData){
        configuration.username = accountsData.device_user
        configuration.password = accountsData.device_pass
        configuration.restServerIp = accountsData.config.restServerIP
        configuration.restServerPort = accountsData.config.restServerPort.toInt()
        val iceServers = ICEServers()
        accountsData.ICEServers.servers.forEach {
            iceServers.addICEServer(it)
        }
        configuration.iceServers = iceServers
        configuration.webSocketServerIp = accountsData.config.webSocketServerIP
        configuration.webSocketServerPort = accountsData.config.webSocketServerPort.toInt()

        Log.d(TAG,"Configurations: " + printConfiguration())
    }

    fun setAdvancedConfigurations(ringingFeedbackOption: String, tcpConnection: Boolean){
        SharedPrefsHelper.putString(SharedPrefsHelper.RINGING_FEEDBACK_OPTION,ringingFeedbackOption)
        SharedPrefsHelper.putBoolean(SharedPrefsHelper.TCP_CONNECTION,tcpConnection)
        when(ringingFeedbackOption){
            "APP" -> configuration.ringingFeedbackOption = RingingFeedbackOptions.APP
            "SERVER" -> configuration.ringingFeedbackOption = RingingFeedbackOptions.SERVER
            "AUTO" -> configuration.ringingFeedbackOption = RingingFeedbackOptions.AUTO
        }
    }



    private fun printConfiguration(): String {
        // return all fields of configuration each in a new line with the following format:
        // field_name: field_value
        return "" +
                "username: ${configuration.username}" +
                "password: ${configuration.password}" +
                "restServerIp: ${configuration.restServerIp}" +
                "restServerPort: ${configuration.restServerPort}" +
                "iceServers: ${configuration.iceServers}" +
                "webSocketServerIp: ${configuration.webSocketServerIp}" +
                "webSocketServerPort: ${configuration.webSocketServerPort}"
    }



}