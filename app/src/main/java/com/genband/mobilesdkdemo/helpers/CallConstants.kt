package com.genband.mobilesdkdemo.helpers

object CallConstants {
    const val LOG_OPTION = "logOption"
    const val TRICKLE_ICE_OPTION = "trickleIceOption"
    const val DTLS_OPTION = "dtlsOption"
    const val EARLY_MEDIA_OPTION = "earlyMediaOption"
    const val RINGING_FEEDBACK_OPTION = "ringingFeedbackOption"
    const val AUDIO_BANDWIDTH_LIMIT_OPTION = "audioBandwidthLimitOption"
    const val DEFAULT_RESOLUTION_OPTION = "defaultVideoResolutionOption"
    const val RINGTONE_OPTION = "ringtoneOption"
    const val ICE_TIMEOUT_VALUE = "iceTimeoutValue"

    // Call start intents
    const val CALL_INTENT_KEY = "callIntent"
    const val CALL_INTENT_INCOMING_CALL = 1
    const val CALL_INTENT_OUTGOING_CALL = 2

    // Call start param keys
    const val CALL_PARAMS_CALL_ID_KEY = "callId"
    const val CALL_PARAMS_CALLER_KEY = "caller"
    const val CALL_TYPE_KEY = "callType"
    const val CALL_PARAMS_CALLER_NAME_KEY = "callerName"
    const val CALL_PARAMS_CALLEE_KEY = "callee"
    const val CALL_PARAMS_CALLEE_NAME_KEY = "calleeName"
    const val CALL_PARAMS_CALL_CAN_SEND_VIDEO = "callCanSendVideo"
    const val CALL_PARAMS_CALL_DOUBLE_M_LINE = "isDoubleMLine"

    // Call event publishing keys
    const val EVENT_KEY = "notificationMessage"
    const val EVENT_CALL_STATUS_CHANGED = "CALL_STATE_CHANGED"
    const val EVENT_MEDIA_ATTRIBUTES_CHANGED = "MEDIA_STATE_CHANGED"
    const val EVENT_AUDIO_ROUTE_CHANGED = "AUDIO_ROUTE_CHANGED"
    const val EVENT_CALL_OPERATION_PERFORMED = "CALL_OPERATION_PERFORMED"

    // Call events
    const val CALL_OPERATION_HOLD_SUCCESS = "CALL_OPERATION_HOLD_SUCCESS"
    const val CALL_OPERATION_UNHOLD_SUCCESS = "CALL_OPERATION_UNHOLD_SUCCESS"
    const val CALL_OPERATION_UNHOLD_FAIL = "CALL_OPERATION_UNHOLD_FAIL"
    const val CALL_OPERATION_HOLD_FAIL = "CALL_OPERATION_HOLD_FAIL"
    const val CALL_OPERATION_MUTE_SUCCESS = "CALL_OPERATION_MUTE_SUCCESS"
    const val CALL_OPERATION_MUTE_FAIL = "CALL_OPERATION_MUTE_FAIL"
    const val CALL_OPERATION_UNMUTE_FAIL = "CALL_OPERATION_UNMUTE_FAIL"

    enum class CallType(val callType: String) {
        INCOMING_CALL("incoming_call"), OUTGOING_CALL("outgoing_call"), REJECTED_CALL("rejected_call");

    }
}
