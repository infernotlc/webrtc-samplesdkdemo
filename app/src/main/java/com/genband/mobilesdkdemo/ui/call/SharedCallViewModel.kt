package com.genband.mobilesdkdemo.ui.call

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.genband.mobile.ServiceProvider
import com.genband.mobile.api.services.call.CallApplicationListener
import com.genband.mobile.api.services.call.CallInterface
import com.genband.mobile.api.services.call.CallServiceInterface
import com.genband.mobile.api.services.call.IncomingCallInterface
import com.genband.mobile.api.services.call.OutgoingCallCreateInterface
import com.genband.mobile.api.services.call.OutgoingCallInterface
import com.genband.mobile.api.utilities.MobileError
import com.genband.mobile.core.webrtc.view.VideoView
import com.genband.mobile.impl.services.call.CallState
import com.genband.mobile.impl.services.call.MediaAttributes
import com.genband.mobile.impl.utilities.Globals.applicationContext
import com.genband.mobilesdkdemo.helpers.SharedPrefsHelper

class SharedCallViewModel : ViewModel() , CallApplicationListener {

    private var callService: CallServiceInterface

    val incomingCall = MutableLiveData<IncomingCallInterface?>()
    val currentCall = MutableLiveData<CallInterface?>()
    val callState = MutableLiveData<CallState?>()
    val endCallSucceeded = MutableLiveData<CallInterface?>()

    private val TAG ="SharedCallViewModel"

    companion object{
        var activeCall : CallInterface? = null
    }

    init {
        Log.d(TAG,"init")
        val serviceProvider = ServiceProvider.getInstance(applicationContext)
        callService = serviceProvider.callService
        callService.setCallApplication(this)
    }

    fun getCall(id: String): CallInterface? {
        val it: Iterator<CallInterface>  = callService.activeCalls.listIterator()
        while (it.hasNext()) {
            val call = it.next()
            if (call.id != null && call.id == id) {
                return call
            }
        }
        return null
    }
    fun createCall(accountData: String, callee : String, localVideoView: VideoView, remoteVideoView: VideoView){

        val callType = SharedPrefsHelper.getString(SharedPrefsHelper.CALL_TYPE,"AUDIO + VIDEO")

        callService.createOutgoingCall(accountData,callee,object:
            OutgoingCallCreateInterface {
            override fun callCreated(callInterface: OutgoingCallInterface?) {
                if (callInterface != null) {
                    activeCall = callInterface
                    //currentCall.value = callInterface
                }
                callInterface?.setLocalVideoView(localVideoView)
                callInterface?.setRemoteVideoView(remoteVideoView)

                when(callType){
                    "AUDIO + VIDEO" -> callInterface?.establishCall(true)
                    "AUDIO" -> callInterface?.establishAudioCall()
                }

                Log.d(TAG,"call created")
            }
            override fun callCreationFailed(error: MobileError?) {
                Log.d(TAG,"$error")
            }
        })
    }
    fun incomingCall(callId: String, localVideoView: VideoView, remoteVideoView: VideoView){

        val incomingCallInterface : IncomingCallInterface = getCall(callId) as IncomingCallInterface

        incomingCallInterface.setLocalVideoView(localVideoView)
        incomingCallInterface.setRemoteVideoView(remoteVideoView)
        incomingCallInterface.acceptCall(true)
        activeCall = incomingCallInterface

    }

    override fun incomingCall(call: IncomingCallInterface?) {
        incomingCall.value = call

    }

    override fun callStatusChanged(p0: CallInterface?, p1: CallState?) {
        callState.value = p1
        Log.d(TAG,"Call Status Changed "+p1.toString())

    }

    override fun mediaAttributesChanged(p0: CallInterface?, p1: MediaAttributes?) {
        Log.d(TAG,"Media Attributes Changed "+p1.toString())

    }

    override fun callAdditionalInfoChanged(p0: CallInterface?, p1: MutableMap<String, String>?) {
        Log.d(TAG,"Call Additional Info Changed "+p1.toString())
    }

    override fun errorReceived(p0: CallInterface?, p1: MobileError?) {
        Log.d(TAG,"Error Received "+p1.toString())
    }

    override fun errorReceived(p0: MobileError?) {
        Log.d(TAG,"Error Received "+p0.toString())
    }

    override fun establishCallSucceeded(p0: OutgoingCallInterface?) {
        Log.d(TAG,"Establish Call Succeeded ")
    }

    override fun establishCallFailed(p0: OutgoingCallInterface?, p1: MobileError?) {
        Log.d(TAG,"Establish Call Failed "+p1.toString())
    }

    override fun acceptCallSucceed(p0: IncomingCallInterface?) {
        Log.d(TAG,"Establish Call Succeeded ")
    }

    override fun acceptCallFailed(p0: IncomingCallInterface?, p1: MobileError?) {
        Log.d(TAG,"Accept Call Failed "+p1.toString())
    }

    override fun rejectCallSuccedded(p0: IncomingCallInterface?) {
        Log.d(TAG,"Reject Call Succeeded ")
    }

    override fun rejectCallFailed(p0: IncomingCallInterface?, p1: MobileError?) {
        Log.d(TAG,"Reject Call Failed "+p1.toString())
    }

    override fun ignoreSucceed(p0: IncomingCallInterface?) {
        Log.d(TAG,"Ignore  Succeeded")
    }

    override fun ignoreFailed(p0: IncomingCallInterface?, p1: MobileError?) {
        Log.d(TAG,"Ignore Failed "+p1.toString())
    }

    override fun forwardCallSucceeded(p0: IncomingCallInterface?) {
        Log.d(TAG,"Forward Call Succeeded")
    }

    override fun forwardCallFailed(p0: IncomingCallInterface?, p1: MobileError?) {
        Log.d(TAG,"Forward Call Failed "+p1.toString())
    }

    override fun videoStopSucceed(p0: CallInterface?) {
        Log.d(TAG,"Video Stop  Succeeded")
    }

    override fun videoStopFailed(p0: CallInterface?, p1: MobileError?) {
        Log.d(TAG,"Video Stop Failed "+p1.toString())
    }

    override fun videoStartSucceed(p0: CallInterface?) {
        Log.d(TAG,"Video Start  Succeeded")
    }

    override fun videoStartFailed(p0: CallInterface?, p1: MobileError?) {
        Log.d(TAG,"Video Start Failed "+p1.toString())
    }

    override fun videoUnMuteSucceed(p0: CallInterface?) {
        Log.d(TAG,"Video UnMute  Succeeded")
    }

    override fun videoUnMuteFailed(p0: CallInterface?, p1: MobileError?) {
        Log.d(TAG,"Video UnMute Failed "+p1.toString())
    }

    override fun videoMuteSucceed(p0: CallInterface?) {
        Log.d(TAG,"Video Mute Succeeded")
    }

    override fun videoMuteFailed(p0: CallInterface?, p1: MobileError?) {
        Log.d(TAG,"Video Mute Failed "+p1.toString())
    }

    override fun muteCallSucceed(p0: CallInterface?) {
        Log.d(TAG,"Mute Call Succeeded")

    }

    override fun muteCallFailed(p0: CallInterface?, p1: MobileError?) {
        Log.d(TAG,"Mute Call Failed "+p1.toString())
    }

    override fun unMuteCallSucceed(p0: CallInterface?) {
        Log.d(TAG,"UnMute Call Succeeded")
    }

    override fun unMuteCallFailed(p0: CallInterface?, p1: MobileError?) {
        Log.d(TAG,"UnMute Call Failed "+p1.toString())
    }

    override fun holdCallSucceed(p0: CallInterface?) {
        Log.d(TAG, "Hold Succeeded")
    }

    override fun holdCallFailed(p0: CallInterface?, p1: MobileError?) {
        Log.d(TAG, "Hold Failed")
    }

    override fun transferCallSucceed(p0: CallInterface?) {
        Log.d(TAG,"Transfer Call Succeeded")
    }

    override fun transferCallFailed(p0: CallInterface?, p1: MobileError?) {
        Log.d(TAG,"Transfer Call Failed "+p1.toString())
    }

    override fun unHoldCallSucceed(p0: CallInterface?) {
        Log.d(TAG, "Unhold Succeeded")
    }

    override fun unHoldCallFailed(p0: CallInterface?, p1: MobileError?) {
        Log.d(TAG, "Unhold Failed")
    }

    override fun sendCustomParametersSuccess(p0: CallInterface?) {
        Log.d(TAG,"Send Custom Parameters Succeeded")
    }

    override fun sendCustomParametersFail(p0: CallInterface?, p1: MobileError?) {
        Log.d(TAG,"Send Custom Parameters Fail "+p1.toString())
    }

    override fun joinSucceeded(p0: CallInterface?) {
        Log.d(TAG,"Join Succeeded")

    }

    override fun joinFailed(p0: CallInterface?, p1: MobileError?) {
        Log.d(TAG,"Join Failed "+p1.toString())
    }

    override fun endCallSucceeded(p0: CallInterface?) {
        endCallSucceeded.value = p0
        Log.d(TAG,"End Call Succeeded")
    }

    override fun endCallFailed(p0: CallInterface?, p1: MobileError?) {
        Log.d(TAG,"End Call Failed "+p1.toString())
    }

    override fun ringingFeedbackSucceeded(p0: IncomingCallInterface?) {
        Log.d(TAG,"Ringing Feedback Succeeded")
    }

    override fun ringingFeedbackFailed(p0: IncomingCallInterface?, p1: MobileError?) {
        Log.d(TAG,"Ringing Feedback Failed "+p1.toString())
    }

    override fun notifyCallProgressChange(p0: CallInterface?) {
        Log.d(TAG,"Notify Call Process Change")
    }
}