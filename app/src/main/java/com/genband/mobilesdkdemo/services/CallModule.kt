package com.genband.mobilesdkdemo.services

import com.genband.mobile.api.services.call.CallApplicationListener
import com.genband.mobile.api.services.call.CallInterface
import com.genband.mobile.api.services.call.IncomingCallInterface
import com.genband.mobile.api.services.call.OutgoingCallInterface
import com.genband.mobile.api.utilities.MobileError
import com.genband.mobile.impl.services.call.CallState
import com.genband.mobile.impl.services.call.MediaAttributes

class CallModule() : CallApplicationListener {

    companion object {
        var instance = CallModule()
    }

    override fun incomingCall(p0: IncomingCallInterface?) {
        TODO("Not yet implemented")
    }

    override fun callStatusChanged(p0: CallInterface?, p1: CallState?) {
        TODO("Not yet implemented")
    }

    override fun mediaAttributesChanged(p0: CallInterface?, p1: MediaAttributes?) {
        TODO("Not yet implemented")
    }

    override fun callAdditionalInfoChanged(p0: CallInterface?, p1: MutableMap<String, String>?) {
        TODO("Not yet implemented")
    }

    override fun errorReceived(p0: CallInterface?, p1: MobileError?) {
        TODO("Not yet implemented")
    }

    override fun errorReceived(p0: MobileError?) {
        TODO("Not yet implemented")
    }

    override fun establishCallSucceeded(p0: OutgoingCallInterface?) {
        TODO("Not yet implemented")
    }

    override fun establishCallFailed(p0: OutgoingCallInterface?, p1: MobileError?) {
        TODO("Not yet implemented")
    }

    override fun acceptCallSucceed(p0: IncomingCallInterface?) {
        TODO("Not yet implemented")
    }

    override fun acceptCallFailed(p0: IncomingCallInterface?, p1: MobileError?) {
        TODO("Not yet implemented")
    }

    override fun rejectCallSuccedded(p0: IncomingCallInterface?) {
        TODO("Not yet implemented")
    }

    override fun rejectCallFailed(p0: IncomingCallInterface?, p1: MobileError?) {
        TODO("Not yet implemented")
    }

    override fun ignoreSucceed(p0: IncomingCallInterface?) {
        TODO("Not yet implemented")
    }

    override fun ignoreFailed(p0: IncomingCallInterface?, p1: MobileError?) {
        TODO("Not yet implemented")
    }

    override fun forwardCallSucceeded(p0: IncomingCallInterface?) {
        TODO("Not yet implemented")
    }

    override fun forwardCallFailed(p0: IncomingCallInterface?, p1: MobileError?) {
        TODO("Not yet implemented")
    }

    override fun videoStopSucceed(p0: CallInterface?) {
        TODO("Not yet implemented")
    }

    override fun videoStopFailed(p0: CallInterface?, p1: MobileError?) {
        TODO("Not yet implemented")
    }

    override fun videoStartSucceed(p0: CallInterface?) {
        TODO("Not yet implemented")
    }

    override fun videoStartFailed(p0: CallInterface?, p1: MobileError?) {
        TODO("Not yet implemented")
    }

    override fun videoUnMuteSucceed(p0: CallInterface?) {
        TODO("Not yet implemented")
    }

    override fun videoUnMuteFailed(p0: CallInterface?, p1: MobileError?) {
        TODO("Not yet implemented")
    }

    override fun videoMuteSucceed(p0: CallInterface?) {
        TODO("Not yet implemented")
    }

    override fun videoMuteFailed(p0: CallInterface?, p1: MobileError?) {
        TODO("Not yet implemented")
    }

    override fun muteCallSucceed(p0: CallInterface?) {
        TODO("Not yet implemented")
    }

    override fun muteCallFailed(p0: CallInterface?, p1: MobileError?) {
        TODO("Not yet implemented")
    }

    override fun unMuteCallSucceed(p0: CallInterface?) {
        TODO("Not yet implemented")
    }

    override fun unMuteCallFailed(p0: CallInterface?, p1: MobileError?) {
        TODO("Not yet implemented")
    }

    override fun holdCallSucceed(p0: CallInterface?) {
        TODO("Not yet implemented")
    }

    override fun holdCallFailed(p0: CallInterface?, p1: MobileError?) {
        TODO("Not yet implemented")
    }

    override fun transferCallSucceed(p0: CallInterface?) {
        TODO("Not yet implemented")
    }

    override fun transferCallFailed(p0: CallInterface?, p1: MobileError?) {
        TODO("Not yet implemented")
    }

    override fun unHoldCallSucceed(p0: CallInterface?) {
        TODO("Not yet implemented")
    }

    override fun unHoldCallFailed(p0: CallInterface?, p1: MobileError?) {
        TODO("Not yet implemented")
    }

    override fun sendCustomParametersSuccess(p0: CallInterface?) {
        TODO("Not yet implemented")
    }

    override fun sendCustomParametersFail(p0: CallInterface?, p1: MobileError?) {
        TODO("Not yet implemented")
    }

    override fun joinSucceeded(p0: CallInterface?) {
        TODO("Not yet implemented")
    }

    override fun joinFailed(p0: CallInterface?, p1: MobileError?) {
        TODO("Not yet implemented")
    }

    override fun endCallSucceeded(p0: CallInterface?) {
        TODO("Not yet implemented")
    }

    override fun endCallFailed(p0: CallInterface?, p1: MobileError?) {
        TODO("Not yet implemented")
    }

    override fun ringingFeedbackSucceeded(p0: IncomingCallInterface?) {
        TODO("Not yet implemented")
    }

    override fun ringingFeedbackFailed(p0: IncomingCallInterface?, p1: MobileError?) {
        TODO("Not yet implemented")
    }

    override fun notifyCallProgressChange(p0: CallInterface?) {
        TODO("Not yet implemented")
    }
}