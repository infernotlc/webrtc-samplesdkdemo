package com.genband.mobilesdkdemo.helpers


import android.content.Context
import android.content.SharedPreferences

//SharedPreferences, genel anlamda Android uygulamalarında küçük miktardaki verileri depolamak için kullanılan bir mekanizmadır.
object SharedPrefsHelper {

    const val DEFAULT_DOMAIN = "DEFAULT_DOMAIN"
    const val DEVICE_USER = "DEVICE_USER"
    const val DEVICE_PASSWORD = "DEVICE_PASSWORD"
    const val TURN_ADDRESS = "TURN_ADDRESS"
    const val PUSH_URL = "PUSH_URL"
    const val REST_IP = "REST_IP"
    const val REST_PORT = "REST_PORT"
    const val SOCKET_PORT ="SOCKET_PORT"
    const val SOCKET_IP = "SOCKET_IP"
    const val ICE_TIMEOUT = "ICE_TIMEOUT"
    const val USE_TURN = "USER_TURN"
    const val CALLEE = "CALLEE"
    const val SESSION_TOKEN = "SESSION_TOKEN"
    const val NOTIFICATION_STATE = "NOTIFICATION_STATE"
    const val TCP_CONNECTION = "TCP_CONNECTION"
    const val CALL_TYPE = "CALL_TYPE"




    const val RINGING_FEEDBACK_OPTION = "RINGING_FEEDBACK_OPTION"
    const val TRICKLE_ICE_SWITCH_SETTINGS_KEY = "TRICKLE_ICE_SWITCH_SETTINGS_KEY"
    const val CAMERA_ORIENTATION_LIST_SETTINGS_KEY = "CAMERA_ORIENTATION_LIST_SETTINGS_KEY"
    const val PREFERRED_CODECS = "REFERRED_CODECS"
    const val AUDIO_CODEC_SET = "AUDIO_CODEC_SET"
    const val VIDEO_CODEC_SET = "VIDEO_CODEC_SET"
    const val LOG_LEVEL = "LOG_LEVEL"
    const val LOGIN_TYPE = "LOGIN_TYPE"





    private const val TAG = "SharedPref"


    private lateinit var prefs: SharedPreferences
    private lateinit var prefsEdit: SharedPreferences.Editor


    fun init(context: Context){
        prefs = context.getSharedPreferences(TAG,Context.MODE_PRIVATE)
        prefsEdit = prefs.edit()
    }

    fun putString(key:String, value:String){
        prefsEdit.putString(key,value)
        prefsEdit.apply()
    }


    fun getString(key: String, defaultValue: String): String {
        return prefs.getString(key, defaultValue).toString()
    }
    fun putInt(key: String, value:Int){
        prefsEdit.putInt(key,value)
        prefsEdit.apply()
    }
    fun getInt(key: String, defaultValue: Int) : Int {
        return prefs.getInt(key,defaultValue)
    }

    fun putBoolean(key: String, value: Boolean) {
        prefsEdit.putBoolean(key, value)
        prefsEdit.apply()
    }

    fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return prefs.getBoolean(key, defaultValue)
    }

    fun putStringSet(key:String, values:Set<String>){
        prefsEdit.putStringSet(key,values)
        prefsEdit.apply()
    }

    fun getStringSet(key: String, values: Set<String>): Set<String> {
        return prefs.getStringSet(key, values) as Set<String>
    }

    fun clearSharedPrefs(){

        putString(DEVICE_USER, "")
        putString(DEVICE_PASSWORD, "")
        putString(DEFAULT_DOMAIN, "")
        putStringSet(TURN_ADDRESS, setOf())
        putString(PUSH_URL, "")
        putString(REST_IP, "")
        putString(REST_PORT, "")
        putString(SOCKET_IP, "")
        putString(SOCKET_PORT, "")
        putInt(ICE_TIMEOUT, 0)
        putBoolean(USE_TURN, true)
        putBoolean(SESSION_TOKEN, false)
        putString(CALLEE, "")
        putString(RINGING_FEEDBACK_OPTION, "AUTO")
        putBoolean(TRICKLE_ICE_SWITCH_SETTINGS_KEY,false)
        putString(CAMERA_ORIENTATION_LIST_SETTINGS_KEY, "N/A")
        putStringSet(AUDIO_CODEC_SET, setOf())
        putStringSet(VIDEO_CODEC_SET, setOf())
        putString(LOG_LEVEL,"")
        putString(LOGIN_TYPE,"")
        putBoolean(PREFERRED_CODECS,false)
        putString(NOTIFICATION_STATE,"")
        putBoolean(TCP_CONNECTION,true)
        putString(CALL_TYPE,"AUDIO + VIDEO")

    }
}