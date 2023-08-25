package com.genband.mobilesdkdemo


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ProgressBar
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.genband.mobile.api.services.call.CallInterface
import com.genband.mobile.api.services.call.IncomingCallInterface
import com.genband.mobile.impl.services.call.CallState
import com.genband.mobilesdkdemo.databinding.ActivityMainBinding
import com.genband.mobilesdkdemo.helpers.CallConstants
import com.genband.mobilesdkdemo.helpers.PermissionHelper
import com.genband.mobilesdkdemo.helpers.SharedPrefsHelper
import com.genband.mobilesdkdemo.helpers.hide
import com.genband.mobilesdkdemo.helpers.show
import com.genband.mobilesdkdemo.model.AccountsData
import com.genband.mobilesdkdemo.ui.call.CallActivity
import com.genband.mobilesdkdemo.ui.call.SharedCallViewModel
import com.genband.mobilesdkdemo.ui.factory.RegistrationViewModelFactory
import com.genband.mobilesdkdemo.ui.login.RegisterViewModel
import com.genband.mobilesdkdemo.ui.main.MainFragment
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {
    private val registerViewModel : RegisterViewModel by viewModels{
        RegistrationViewModelFactory((this.application as App).serviceProvider)
    }
    private val sharedCallViewModel : SharedCallViewModel by viewModels()

    private lateinit var binding: ActivityMainBinding
    private lateinit var bottomNavigationView : BottomNavigationView
    private lateinit var progressBar : ProgressBar
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private  var accountsData = AccountsData()
    private val bundle = Bundle()
    private val TAG = "MainActivity"
    private var currentCall: CallInterface? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view=binding.root
        setContentView(view)

        PermissionHelper.requestAppPermissions(this)
        SharedPrefsHelper.init(this)
        bottomNavigationView = binding.bottomNavigationView
        progressBar = binding.progressBar
        getSharedPref()
        validateSessionToken(bundle)

        observeCallStatus()

    }

    private fun getSharedPref(){
        accountsData.device_user = SharedPrefsHelper.getString(SharedPrefsHelper.DEVICE_USER,"")
        accountsData.device_pass = SharedPrefsHelper.getString(SharedPrefsHelper.DEVICE_PASSWORD,"")
        accountsData.default_domain = SharedPrefsHelper.getString(SharedPrefsHelper.DEFAULT_DOMAIN,"")
        accountsData.pushServerURL = SharedPrefsHelper.getString(SharedPrefsHelper.PUSH_URL,"")
        accountsData.useTurn = SharedPrefsHelper.getBoolean(SharedPrefsHelper.USE_TURN,true)
        accountsData.ICEServers.servers = SharedPrefsHelper.getStringSet(SharedPrefsHelper.TURN_ADDRESS, setOf()).toList()
        accountsData.config.restServerIP = SharedPrefsHelper.getString(SharedPrefsHelper.REST_IP,"")
        accountsData.config.restServerPort = SharedPrefsHelper.getString(SharedPrefsHelper.REST_PORT,"")
        accountsData.config.webSocketServerPort = SharedPrefsHelper.getString(SharedPrefsHelper.SOCKET_PORT,"")
        accountsData.config.webSocketServerIP = SharedPrefsHelper.getString(SharedPrefsHelper.SOCKET_IP,"")
        accountsData.config.ICECollectionTimeout = SharedPrefsHelper.getInt(SharedPrefsHelper.ICE_TIMEOUT,3)

        bundle.putParcelable("accountsData", accountsData)
    }

    private fun validateSessionToken(bundle: Bundle){
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController
        bottomNavigationView.setupWithNavController(navController)
        setBottomNavigationBar(bundle)

        val sessionToken = SharedPrefsHelper.getBoolean(SharedPrefsHelper.SESSION_TOKEN,false)

        //User not logged in before, redirect to login screen
        if (!sessionToken){
            navController.navigate(R.id.registerFragment,bundle)
        }
        //User logged in, redirect to home screen
        else{
            getSharedPref()
            progressBar.show()
            registerViewModel.setConfiguration(accountsData)
            registerViewModel.register()
            registerViewModel.registrationSuccess.observe(this){
                navController.navigate(R.id.mainFragment,bundle)
                progressBar.hide()
            }
            registerViewModel.registrationFail.observe(this){
                progressBar.hide()
            }
        }
    }

    private fun setBottomNavigationBar(bundle: Bundle) {
        bottomNavigationView.selectedItemId = R.id.callFragment
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)
        bottomNavigationView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener{_, destination, _ ->
            when(destination.id){
                R.id.mainFragment ->{
                    bottomNavigationView.show()
                    supportActionBar?.title = "Start Call"
                }
                R.id.settingsFragment ->{
                    bottomNavigationView.show()
                    supportActionBar?.title = "Settings"
                }
                else -> bottomNavigationView.hide()

            }

        }
    }

    private fun showIncomingCallActionDialog(call: IncomingCallInterface?){
        val incomingCallActionDialog = AlertDialog.Builder(this)

        val incomingCallIntent = Intent(this, CallActivity::class.java)
        val bundle = Bundle()
        bundle.putString(CallConstants.CALL_PARAMS_CALL_ID_KEY, call?.id)
        bundle.putString(CallConstants.CALL_PARAMS_CALLER_KEY, call?.callerAddress)
        bundle.putString(CallConstants.CALL_PARAMS_CALLER_NAME_KEY, call?.callerName)
        bundle.putInt(CallConstants.CALL_INTENT_KEY, CallConstants.CALL_INTENT_INCOMING_CALL)
        incomingCallIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
        incomingCallIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
        incomingCallIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        incomingCallIntent.putExtras(bundle)
        incomingCallIntent.putExtra(CallConstants.CALL_TYPE_KEY, CallConstants.CallType.INCOMING_CALL)
        call?.canSendVideo()?.let { bundle.putBoolean(CallConstants.CALL_PARAMS_CALL_CAN_SEND_VIDEO, it) }

        incomingCallActionDialog.setMessage("Incoming Call From: "+call?.callerName)

        //checking if the call has a video m line:
        if (call?.canReceiveVideo()==true){

            //If call has video m line and you want to answer with video:
            incomingCallActionDialog.setPositiveButton("Accept"){ dialog, _ ->
                SharedPrefsHelper.putString(SharedPrefsHelper.CALL_TYPE,"AUDIO + VIDEO")
                dialog.dismiss()
                call.acceptCall(true)
                startActivity(incomingCallIntent)
            }
            //OR if you want to answer with audio only:
            incomingCallActionDialog.setNeutralButton("Audio Only"){ dialog, _ ->
                SharedPrefsHelper.putString(SharedPrefsHelper.CALL_TYPE,"AUDIO")
                dialog.dismiss()
                call.acceptCall(false)
                startActivity(incomingCallIntent)
            }
        }else{
            //If call has only one m line, the call will be answered with audio only
            incomingCallActionDialog.setNeutralButton("Audio Only"){ dialog, _ ->
                SharedPrefsHelper.putString(SharedPrefsHelper.CALL_TYPE,"AUDIO")
                dialog.dismiss()
                call?.acceptCall(false)
                startActivity(incomingCallIntent)
            }
        }
        incomingCallActionDialog.setNegativeButton("Decline"){ dialog, _ ->
            dialog.dismiss()
            call?.rejectCall()
            dialog.cancel()
        }
        val dialog = incomingCallActionDialog.create()
        dialog.show()
    }
    private fun observeCallStatus(){

        //incoming call
        sharedCallViewModel.incomingCall.observe(this){
            Log.d(TAG,"Incoming Call")

            showIncomingCallActionDialog(it)
            currentCall = it
        }

        //call status change
        sharedCallViewModel.callState.observe(this){
            Log.d(TAG,"callStatusChanged $it")
            if (it?.type == CallState.Type.ENDED){
                supportFragmentManager.beginTransaction().replace(R.id.fragmentContainerView, MainFragment()).commit()
            }
        }

        //end call succeeded
        sharedCallViewModel.endCallSucceeded.observe(this){
            Log.d(TAG,"endCallSucceeded")
            supportFragmentManager.beginTransaction().replace(R.id.fragmentContainerView, MainFragment()).commit()
        }

    }
}