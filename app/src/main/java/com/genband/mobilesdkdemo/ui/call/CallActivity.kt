package com.genband.mobilesdkdemo.ui.call

import android.content.ContentValues
import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.hardware.Camera.CameraInfo
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.genband.mobile.impl.services.call.CallState
import com.genband.mobilesdkdemo.R
import com.genband.mobilesdkdemo.databinding.ActivityCallBinding
import com.genband.mobilesdkdemo.helpers.CallConstants
import com.genband.mobilesdkdemo.helpers.SharedPrefsHelper
import com.genband.mobilesdkdemo.helpers.hide
import com.genband.mobilesdkdemo.helpers.show
import com.genband.mobilesdkdemo.ui.main.MainFragment
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class CallActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCallBinding
    private val sharedCallViewModel : SharedCallViewModel by viewModels()

    private var accountData: String? = null
    private var callee: String? = null

    private val TAG ="CallActivity"
    private var muteStatus: Boolean = true
    private var videoMuteStatus: Boolean = true
    private var holdStatus: Boolean = true
    private var camPositionStatus: Boolean = true
    private var switchViewStatus : Boolean = true



    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCallBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        parseCallIntent(intent)
        // READ intent if it is an incoming call
        callSettings()
        sharedCallViewModel.callState.observe(this){
            Log.d(TAG,"callStatusChanged"+it.toString())
            if (it?.type == CallState.Type.ENDED){
                supportFragmentManager.beginTransaction().replace(R.id.fragmentContainerView, MainFragment()).commit()
            }
        }
    }
    private fun parseCallIntent(intent: Intent?) {
        if (intent != null && intent.extras != null) {
            val extras = intent.extras
            val callIntent = extras!!.getInt(CallConstants.CALL_INTENT_KEY)

            if (callIntent == CallConstants.CALL_INTENT_OUTGOING_CALL) {
                accountData = intent.getStringExtra("AccountData")
                callee = intent.getStringExtra("Callee")
                sharedCallViewModel.createCall(accountData.toString(),callee.toString(),findViewById(R.id.localVideoView),findViewById(R.id.remoteVideoView))

            } else if (callIntent == CallConstants.CALL_INTENT_INCOMING_CALL) {

                val incomingCallIntent = intent.getStringExtra(CallConstants.CALL_PARAMS_CALL_ID_KEY)
                if (incomingCallIntent != null) {
                    sharedCallViewModel.incomingCall(incomingCallIntent,findViewById(R.id.localVideoView),findViewById(R.id.remoteVideoView))
                }
            }
        }
    }

    private fun callSettings(){
        onActionLayoutClick()
        holdCall()
        muteVideo()
        muteMic()
        stopCall()
        takeSnapshot()
        switchCam()
        switchViews()
    }
    private fun switchViews(){
        val localVideoView = binding.localVideoView
        val remoteVideoView = binding.remoteVideoView
        val remoteVideoParams = remoteVideoView.layoutParams
        val localVideoParams = localVideoView.layoutParams

        binding.switchViewsBtn.setOnClickListener {
            if (switchViewStatus){
                remoteVideoView.layoutParams = localVideoParams
                localVideoView.elevation = 0.dpToPx()
                localVideoView.layoutParams = remoteVideoParams
                remoteVideoView.elevation = 10.dpToPx()

            }else{
                remoteVideoView.layoutParams = remoteVideoParams
                remoteVideoView.elevation = 0.dpToPx()
                localVideoView.layoutParams = localVideoParams
                localVideoView.elevation = 10.dpToPx()

            }
            switchViewStatus = !switchViewStatus
            remoteVideoView.requestLayout()
            localVideoView.requestLayout()

        }
    }
    private fun switchCam(){
        binding.switchCameraBtn.setOnClickListener {
            camPositionStatus = if (camPositionStatus){
                //Back
                SharedCallViewModel.activeCall?.setCaptureDevice(CameraInfo.CAMERA_FACING_BACK,null,null)
                false
            } else{
                //Front
                SharedCallViewModel.activeCall?.setCaptureDevice(CameraInfo.CAMERA_FACING_FRONT,null,null)
                true
            }
        }
    }
    private fun takeSnapshot(){
        binding.takeSnapshotBtn.setOnClickListener {
            val rootView = window.decorView.rootView
            rootView.isDrawingCacheEnabled = true
            val bitmap = Bitmap.createBitmap(rootView.drawingCache)
            rootView.isDrawingCacheEnabled = false

            val filename = generateFilename()
            val contentValues = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, filename)
                put(MediaStore.Images.Media.MIME_TYPE, "image/png")
            }

            val contentResolver = contentResolver
            val imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

            try {
                val outputStream: OutputStream? = contentResolver.openOutputStream(imageUri!!)
                outputStream?.use {
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
                }

                Toast.makeText(this, "Screenshot saved.", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(this, "An error occurred while saving the screenshot.", Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            }
        }
    }
    private fun generateFilename(): String {
        val timestamp = SimpleDateFormat("yyyyMMdd-HHmmss", Locale.getDefault()).format(Date())
        return "screenshot_$timestamp.png"
    }
    private fun muteMic(){
        val muteButton = binding.muteButton
        muteButton.setOnClickListener {
            if (muteStatus){
                SharedCallViewModel.activeCall?.mute()
                muteButton.setImageResource(R.drawable.unmute)
            } else{
                SharedCallViewModel.activeCall?.unMute()
                muteButton.setImageResource(R.drawable.mute)
            }
            muteStatus = !muteStatus
        }
    }
    private fun muteVideo(){
        //video mute-unmute and video start-stop
        val muteVideoButton = binding.muteVideoButton
        when(SharedPrefsHelper.getString(SharedPrefsHelper.CALL_TYPE, "AUDIO + VIDEO")){
            "AUDIO" ->{
                muteVideoButton.setImageResource(R.drawable.video_unmute)
                muteVideoButton.setOnClickListener {
                    if (videoMuteStatus){
                        SharedCallViewModel.activeCall?.videoStart()
                        muteVideoButton.setImageResource(R.drawable.video_mute)
                    }else{
                        SharedCallViewModel.activeCall?.videoStop()
                        muteVideoButton.setImageResource(R.drawable.video_unmute)
                    }
                    videoMuteStatus = !videoMuteStatus
                }
            }
            "AUDIO + VIDEO" ->{
                muteVideoButton.setOnClickListener {
                    if (videoMuteStatus){
                        SharedCallViewModel.activeCall?.videoMute()
                        muteVideoButton.setImageResource(R.drawable.video_unmute)
                    }else {
                        SharedCallViewModel.activeCall?.videoUnMute()
                        muteVideoButton.setImageResource(R.drawable.video_mute)

                    }
                    videoMuteStatus = !videoMuteStatus
                }
            }
        }
    }
    private fun holdCall(){
        val holdButton = binding.holdBtn
        holdButton.setOnClickListener {

            if (holdStatus ){
                SharedCallViewModel.activeCall?.holdCall()
                holdButton.setImageResource(R.drawable.unhold)
            }else{
                SharedCallViewModel.activeCall?.unHoldCall()
                holdButton.setImageResource(R.drawable.hold)
            }
            holdStatus = !holdStatus

        }
    }
    private fun stopCall(){
        binding.stopCallButton.setOnClickListener {
            SharedCallViewModel.activeCall?.endCall()
            if (SharedCallViewModel.activeCall?.callState?.type == CallState.Type.ENDED){
                supportFragmentManager.beginTransaction().replace(R.id.fragmentContainerView, MainFragment()).commit()
            }
            Log.d(TAG,"call ended")
        }
    }
    private fun onActionLayoutClick(){
        //opening call operation panel
        val actionButton = binding.actionButton
        val rootLayout = binding.rootLayout
        actionButton.setOnClickListener {
            if (rootLayout.isVisible){
                rootLayout.hide()
                actionButton.setBackgroundResource(R.drawable.arrow_up)
            }
            else{
                rootLayout.show()
                actionButton.setBackgroundResource(R.drawable.arrow_down)
            }
        }
    }

    //Converting dp value to pixel with extension function
    fun Int.dpToPx(): Float {
        return this * Resources.getSystem().displayMetrics.density
    }
}



