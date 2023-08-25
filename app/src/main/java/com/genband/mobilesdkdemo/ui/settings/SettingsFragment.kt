package com.genband.mobilesdkdemo.ui.settings

import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.genband.mobilesdkdemo.App
import com.genband.mobilesdkdemo.R
import com.genband.mobilesdkdemo.databinding.FragmentSettingsBinding
import com.genband.mobilesdkdemo.helpers.hide
import com.genband.mobilesdkdemo.helpers.show
import com.genband.mobilesdkdemo.ui.factory.SettingsViewModelFactory
import com.google.android.material.button.MaterialButton


class SettingsFragment : Fragment() {
    private val viewModel : SettingsViewModel by viewModels{
        SettingsViewModelFactory((activity?.application as App).serviceProvider)
    }

    private lateinit var binding: FragmentSettingsBinding
    private lateinit var progressBar: ProgressBar
    private val TAG = "SettingsFragment"
    private var checkedAudioItems = booleanArrayOf(false, false, false, false, false,false)
    private var checkedVideoItems = booleanArrayOf(false, false, false)



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progressBar = binding.progressBar

        //unregistering when logout button is clicked
        binding.logoutBtn.setOnClickListener {
            progressBar.show()
            viewModel.unRegister()
        }

        unRegisterStateObservers()
        drawSettingsUI()
        handleClickListener()
    }

    private fun unRegisterStateObservers(){

        viewModel.unRegisterSuccess.observe(viewLifecycleOwner) {
            Log.d(TAG,"Unregistration Success: $it")
            progressBar.hide()
            Toast.makeText(context, "Unregistered Successfully", Toast.LENGTH_SHORT).show()
            val action = SettingsFragmentDirections.actionSettingsFragmentToRegisterFragment()
            view?.findNavController()?.navigate(action)
       }
        viewModel.unRegisterFail.observe(viewLifecycleOwner){
            Log.d(TAG,"Unregistration Failed: $it")
            progressBar.hide()
            Toast.makeText(context, "Unregisteration Failed: $it", Toast.LENGTH_SHORT).show()
        }
        viewModel.registrationDropped.observe(viewLifecycleOwner) {
            Log.d(TAG,"Unregistration Dropped: $it")
            progressBar.hide()
            Toast.makeText(context, "Unregisteration Dropped: $it", Toast.LENGTH_SHORT).show()
        }
        viewModel.notificationStateChanged.observe(viewLifecycleOwner) {
            Log.d(TAG,"Notification State Changed: $it")
            progressBar.hide()
            Toast.makeText(context, "Notification State Changed: $it", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun drawSettingsUI(){
        //Ringing Feedback Option
        when(viewModel.getSharedOptions().ringingFeedbackOption){
            "APP" -> binding.appBtn.isChecked = true
            "AUTO" -> binding.autoBtn.isChecked = true
            "SERVER" -> binding.serverBtn.isChecked = true }

        //Trickle ICE
        binding.enableTrickelICESwitch.isChecked = viewModel.getSharedOptions().iceOption

        //Orientation Mode
        when(viewModel.getSharedOptions().orientationMode){
            "N/A" -> binding.naBtn.isChecked = true
            "DEVICE" -> binding.deviceBtn.isChecked = true
            "STATUSBAR" -> binding.statusBarBtn.isChecked = true }

        //Audio Codecs
        val audioCodecs = resources.getStringArray(R.array.pref_audio_codec_titles)
        val sharedPrefAudioCodecs = viewModel.getSharedOptions().audioCodecSet
        for (index in audioCodecs.indices){
            sharedPrefAudioCodecs.forEach {
                if (it == audioCodecs[index]){
                    checkedAudioItems[index] = true
                }
            }
        }

        //Video Codecs
        val videoCodecs = resources.getStringArray(R.array.pref_video_codec_values)
        val sharedPrefVideoCodecs = viewModel.getSharedOptions().videoCodecSet
        for (index in videoCodecs.indices){
            sharedPrefVideoCodecs.forEach {
                if (it == videoCodecs[index]){
                    checkedVideoItems[index] = true
                }
            }
        }
    }

    private fun handleClickListener(){

        //Ringing Feedback Option
        binding.ringingFeedbackToggleGroup.addOnButtonCheckedListener { group, checkedId, isChecked ->
            val checkedButton = group.findViewById<MaterialButton>(checkedId)
            if (isChecked)
                viewModel.setRingingFeedbackOptions(checkedButton.text.toString())
        }

        //Trickle ICE
        binding.enableTrickelICESwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked)
                viewModel.setTrickleICE(true)
            else
                viewModel.setTrickleICE(false)
        }

        //Orientation Mode
        binding.orientationToggleGroup.addOnButtonCheckedListener { group, checkedId, isChecked ->
            val checkedButton = group.findViewById<MaterialButton>(checkedId)
            if (isChecked)
                viewModel.setOrientationMode(checkedButton.text.toString())
        }

        //Preferred Codecs
        //making selections according to the activation of the preferred codecs switch
        binding.preferredCodecsSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked){
                binding.audioCodecsBtn.isEnabled = true
                binding.videoCodecsBtn.isEnabled = true
                binding.audioCodecsText.setTextColor(Color.GRAY)
                binding.videoCodecsText.setTextColor(Color.GRAY)
            }else{
                binding.audioCodecsBtn.isEnabled = false
                binding.videoCodecsBtn.isEnabled = false
                binding.audioCodecsText.setTextColor(resources.getColor(R.color.gray))
                binding.videoCodecsText.setTextColor(resources.getColor(R.color.gray))
            }
        }

        //Audio Codecs
        binding.audioCodecsBtn.setOnClickListener {
            val audioCodecs = resources.getStringArray(R.array.pref_audio_codec_titles)
            val audioCodecLists : ArrayList<String> = arrayListOf()
            val alertDialog = context?.let { it1 -> AlertDialog.Builder(it1) }
            alertDialog?.setTitle("Audio Codecs")

            alertDialog?.setMultiChoiceItems(audioCodecs,checkedAudioItems) { dialog, which, isChecked ->
                audioCodecLists.add(audioCodecs[which])
            }
            alertDialog?.setPositiveButton("OK"){ dialog, which ->
                // Actions to be taken when the OK button is clicked
                viewModel.setAudioCodecs(audioCodecLists)
            }?.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, which ->
                // Actions to be taken when Cancel button is clicked

                })
            alertDialog?.create()?.show()
        }

        //Video Codecs
        binding.videoCodecsBtn.setOnClickListener {
            val videoCodecs = resources.getStringArray(R.array.pref_video_codec_values)
            val videoCodecLists : ArrayList<String> = arrayListOf()

            val alertDialog = context?.let { it1 -> AlertDialog.Builder(it1) }
            alertDialog?.setTitle("Video Codecs")

            alertDialog?.setMultiChoiceItems(videoCodecs,checkedVideoItems){ dialog, which, isChecked ->
                //Called when item is selected or deselected
                videoCodecLists.add(videoCodecs[which])
            }
            alertDialog?.setPositiveButton("OK") { dialog, which ->
                // Actions to be taken when the OK button is clicked
                viewModel.setVideoCodecs(videoCodecLists)
            }?.setNegativeButton("Cancel") { dialog, which ->
                // Actions to be taken when Cancel button is clicked
                dialog.cancel()
            }
            val dialog = alertDialog?.create()
            dialog?.show()
        }




    }




}