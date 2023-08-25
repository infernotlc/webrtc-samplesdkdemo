package com.genband.mobilesdkdemo.ui.login
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.genband.mobilesdkdemo.App
import com.genband.mobilesdkdemo.R
import com.genband.mobilesdkdemo.ui.factory.RegistrationViewModelFactory
import com.genband.mobilesdkdemo.databinding.FragmentRegisterBinding
import com.genband.mobilesdkdemo.helpers.hide
import com.genband.mobilesdkdemo.helpers.show
import com.genband.mobilesdkdemo.model.AccountsData
import com.google.android.material.button.MaterialButton


class RegisterFragment : Fragment() {
    private lateinit var binding: FragmentRegisterBinding
    private val viewModel : RegisterViewModel by viewModels{
        RegistrationViewModelFactory((activity?.application as App).serviceProvider)
    }

    private var accountsData : ArrayList<AccountsData> ?= arrayListOf()
    private var accountNames : List<String> ?= listOf()
    private var selectedUser: AccountsData? = null
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private lateinit var useTurnSwitch: Switch
    private lateinit var progressBar: ProgressBar
    private val TAG = "RegisterFragment"


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressBar = binding.progressBar
        useTurnSwitch = binding.useTurnSwitch

        showAccounts()

        //registering to server
        binding.loginBtn.setOnClickListener {
            progressBar.show()
            selectedUser?.let { it1 -> viewModel.setConfiguration(it1) }
            viewModel.register()
            selectedUser?.let { it1 -> viewModel.setSharedPrefs(it1,useTurnSwitch.isChecked) }
        }

        registrationStateObservers()
    }

    private fun registrationStateObservers(){
        viewModel.registrationSuccess.observe(viewLifecycleOwner){
            Log.d(TAG,"Registration Success: $it")
            setArgs()
            Toast.makeText(context, "Registered Successfully", Toast.LENGTH_SHORT).show()
        }
        viewModel.registrationFail.observe(viewLifecycleOwner){
            Log.d(TAG,"Registration Fail: $it")
            Toast.makeText(context, "Registeration Failed: $it", Toast.LENGTH_SHORT).show()
            progressBar.visibility = View.GONE
        }
        viewModel.registrationDropped.observe(viewLifecycleOwner) {
            Log.d(TAG,"Registration Dropped: $it")
            Toast.makeText(context, "Registration Dropped: $it", Toast.LENGTH_SHORT).show()
        }
        RegisterViewModel.notificationStateChanged.observe(viewLifecycleOwner) {
            Log.d(TAG,"Notification State Changed: $it")
            Toast.makeText(context, "Notification State Changed: $it", Toast.LENGTH_SHORT).show()
        }

    }

    private fun setArgs(){
        val bundle = Bundle()
        bundle.putParcelable("accountsData", selectedUser)
        progressBar.hide()
        view?.findNavController()?.navigate(R.id.mainFragment,bundle)

    }

    private fun showAccounts(){

        viewModel.getAccountsFirebase()
        viewModel.accounts.observe(viewLifecycleOwner){

            it.let {
                accountNames = it.accountNames
                accountsData = it.userSetList
            }
            val arrayAdapter = ArrayAdapter(requireContext(),R.layout.dropdown_item, accountNames as List<String>)
            binding.accountDropdownMenu.setAdapter(arrayAdapter)
            binding.accountDropdownMenu.isEnabled = true

            selectedUser = accountsData?.get(0)
            binding.accountDropdownMenu.setText(arrayAdapter.getItem(0),false)
            selectedUser?.let { it1 -> setUIValues(it1) }
        }

        binding.accountDropdownMenu.setOnItemClickListener { adapterView, view, position, l ->
            selectedUser = accountsData?.get(position)
            selectedUser?.let{
                setUIValues(it)
            }
        }

    }

    private fun setUIValues(selectedUser : AccountsData){
        binding.userName.setText(selectedUser.device_user)
        binding.password.setText(selectedUser.device_pass)
        binding.restIP.setText(selectedUser.config.restServerIP)
        binding.restPort.setText(selectedUser.config.restServerPort)
        binding.iceTimeout.setText(selectedUser.config.ICECollectionTimeout.toString())
        binding.socketPort.setText(selectedUser.config.webSocketServerPort)
        binding.useTurnSwitch.isChecked = selectedUser.useTurn
        binding.ringingFeedbackToggleGroup.check(R.id.appBtn)
        binding.version.text = "Version: ${viewModel.getMobileSdkVersion()}"
        val tcpConnection = binding.enableTCPConnectionSwitch.isChecked
        binding.ringingFeedbackToggleGroup.addOnButtonCheckedListener { group, checkedId, isChecked ->
            val checkedButton = group.findViewById<MaterialButton>(checkedId)
            if (isChecked){
                viewModel.setAdvancedConfigurations(checkedButton.text.toString(),tcpConnection)
            }
        }
    }

}

