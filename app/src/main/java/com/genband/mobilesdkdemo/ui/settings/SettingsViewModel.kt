package com.genband.mobilesdkdemo.ui.settings

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.genband.mobile.*
import com.genband.mobile.api.utilities.*
import com.genband.mobilesdkdemo.helpers.SharedPrefsHelper
import com.genband.mobilesdkdemo.model.AdvancedConfigurationsModel

class SettingsViewModel(private val serviceProvider: ServiceProvider) : ViewModel() {

    private val TAG = "SettingsViewModel"
    private val configuration = Configuration.getInstance()

    val registrationStateChanged = MutableLiveData<RegistrationStates?>()
    val registrationDropped = MutableLiveData<MobileError?>()
    val notificationStateChanged = MutableLiveData<NotificationStates?>()
    val onInternalError = MutableLiveData<MobileError?>()
    val unRegisterSuccess = MutableLiveData<Boolean>()
    val unRegisterFail = MutableLiveData<MobileError?>()


    fun unRegister(){

        val registrationListener = object : RegistrationApplicationListener{
            override fun registrationStateChanged(p0: RegistrationStates?) {
                registrationStateChanged.value=p0
            }

            override fun registrationDropped(p0: MobileError?) {
                registrationDropped.value = p0
            }

            override fun notificationStateChanged(p0: NotificationStates?) {
                notificationStateChanged.value = p0
            }

            override fun onInternalError(p0: MobileError?) {
                onInternalError.value = p0
            }

        }

        val registrationService : RegistrationService = serviceProvider.registrationService
        registrationService.unregisterFromServer(object :OnCompletionListener{
            override fun onSuccess() {
                //Handle unregistration success
                Log.d(TAG,"Clearing Shared Preferences Control")
                SharedPrefsHelper.clearSharedPrefs()
                unRegisterSuccess.value = true
            }

            override fun onFail(p0: MobileError?) {
                //Handle unregistration error
                Log.d(TAG,"Unregistration Failed: $p0")
                unRegisterFail.value = p0
            }

        })
    }

    fun getSharedOptions() : AdvancedConfigurationsModel {
        val sharedRingingFeedbackOption = SharedPrefsHelper.getString(SharedPrefsHelper.RINGING_FEEDBACK_OPTION, "AUTO")
        val sharedICEOption = SharedPrefsHelper.getBoolean(SharedPrefsHelper.TRICKLE_ICE_SWITCH_SETTINGS_KEY,false)
        val sharedOrientationMode = SharedPrefsHelper.getString(SharedPrefsHelper.CAMERA_ORIENTATION_LIST_SETTINGS_KEY,"N/A")
        val sharedPreferredCodec =SharedPrefsHelper.getBoolean(SharedPrefsHelper.PREFERRED_CODECS,false)
        val sharedAudioCodec = SharedPrefsHelper.getStringSet(SharedPrefsHelper.AUDIO_CODEC_SET, setOf())
        val sharedVideoCodec = SharedPrefsHelper.getStringSet(SharedPrefsHelper.VIDEO_CODEC_SET, setOf())
        val sharedLogLevel = SharedPrefsHelper.getString(SharedPrefsHelper.LOG_LEVEL,"")
        val sharedLoginType = SharedPrefsHelper.getString(SharedPrefsHelper.LOGIN_TYPE,"")
        val sharedICETimeout = SharedPrefsHelper.getInt(SharedPrefsHelper.ICE_TIMEOUT,0)

        return AdvancedConfigurationsModel(sharedRingingFeedbackOption,sharedICEOption,sharedOrientationMode,sharedPreferredCodec,sharedAudioCodec.toList(),sharedVideoCodec.toList(),sharedLogLevel,sharedLoginType,sharedICETimeout)
    }

    fun setRingingFeedbackOptions(feedbackOptionID : String){

        SharedPrefsHelper.putString(SharedPrefsHelper.RINGING_FEEDBACK_OPTION,feedbackOptionID)
        Log.d(TAG,"RINGING FEEDBACK -> $feedbackOptionID")

        configuration.ringingFeedbackOption = when (feedbackOptionID) {
            "APP" -> RingingFeedbackOptions.APP
            "SERVER" -> RingingFeedbackOptions.SERVER
            "AUTO" -> RingingFeedbackOptions.AUTO
            else -> throw IllegalArgumentException("Invalid feedback option ID: $feedbackOptionID")
        }
    }
    fun setTrickleICE(enable : Boolean){
        configuration.iceOption = if (enable) {
            ICEOptions.ICE_TRICKLE
        } else {
            ICEOptions.ICE_VANILLA
        }
        SharedPrefsHelper.putBoolean(SharedPrefsHelper.TRICKLE_ICE_SWITCH_SETTINGS_KEY,enable)
        Log.d(TAG, "TRICKLE ICE -> $enable")
    }
    fun setOrientationMode(orientationID : String){
        val orientationMode = when (orientationID) {
            "N/A" -> {
                OrientationMode.CAMERA_ORIENTATION_USES_NONE
            }
            "DEVICE" -> {
                OrientationMode.CAMERA_ORIENTATION_USES_DEVICE
            }
            "STATUSBAR" -> {
                OrientationMode.CAMERA_ORIENTATION_USES_STATUS_BAR
            }
            else -> throw IllegalArgumentException("Invalid orientation ID: $orientationID")
        }
        configuration.orientationMode = orientationMode
        SharedPrefsHelper.putString(SharedPrefsHelper.CAMERA_ORIENTATION_LIST_SETTINGS_KEY, orientationID)
        Log.d(TAG, "ORIENTATION -> $orientationID")
    }

    fun setAudioCodecs(audioCodec: ArrayList<String>){

        val audioCodecList: ArrayList<CodecSet.AudioCodecType> = ArrayList(6)
        val codecSet = CodecSet()
        audioCodec.forEach {
            audioCodecList.add(CodecSet.AudioCodecType.valueOf("AC_$it"))
        }
        configuration.preferredCodecSet.setAudioCodecs(audioCodecList.toArray(codecSet.audioCodecs))
        SharedPrefsHelper.putStringSet(SharedPrefsHelper.AUDIO_CODEC_SET,audioCodec.toSet())
        audioCodecList.forEach {
            Log.d(TAG,"AUDIO CODECS -> $it")
        }
    }
    fun setVideoCodecs(videoCodec: ArrayList<String>){

        val videoCodecList: ArrayList<CodecSet.VideoCodecType> = ArrayList<CodecSet.VideoCodecType>(3)
        val codecSet = CodecSet()
        videoCodec.forEach {
            videoCodecList.add(CodecSet.VideoCodecType.valueOf("VC_$it"))
        }
        configuration.preferredCodecSet.setVideoCodecs(videoCodecList.toArray(codecSet.videoCodecs))
        SharedPrefsHelper.putStringSet(SharedPrefsHelper.VIDEO_CODEC_SET,videoCodec.toSet())
        videoCodecList.forEach {
            Log.d(TAG,"VIDEO CODECS -> $it")
        }

    }






}