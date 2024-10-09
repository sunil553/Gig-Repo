package com.llyod.task.viewmodel

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.llyod.data.common.NetworkStatus
import com.llyod.data.repository.UserPreferencesRepo
import com.llyod.domain.common.Result
import com.llyod.domain.repository.LoginOtpValidationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val networkStatus: NetworkStatus,
    private val loginOtpValidationRepository: LoginOtpValidationRepository,
    private val userPreferencesRepository: UserPreferencesRepo
) : ViewModel() {


    var progressBarVisibility = ObservableField<Boolean>()
    var loginButtonVisibility = ObservableField<Boolean>()
    var otpFieldVisibility = ObservableField<Boolean>()
    var phoneNumber = ObservableField<String>()
    var otpNumber = ObservableField<String>()

    //
    private val _loginLiveData = MutableLiveData<Boolean>()
    val loginLiveData: LiveData<Boolean> = _loginLiveData
    //
    private val _showMessageLiveData = MutableLiveData<String>()
    val showMessageLiveData: LiveData<String> = _showMessageLiveData

    private val _errorMessages = MutableLiveData<String>()
    val errorMessages: LiveData<String> = _errorMessages

    init {
        otpFieldVisibility.set(false)
        loginButtonVisibility.set(true)
    }
    private fun validateMobileNo(mobileNo: String): Boolean {
        return mobileNo.length == 10 && isValidMobileNumber(mobileNo)
    }
    fun getAccessToken(): Boolean {
        return userPreferencesRepository.getString(MOBILE_NO,"").isNotEmpty()
    }


    fun getOtpForMobileNo(){
        if (!networkStatus.isOnline()){
            _errorMessages.postValue("Please check your internet connection")
            return
        }
        if (otpNumber.get()?.let { validateOtp(otp = it) } == true) {
            val mobileNumber = phoneNumber.get()
            viewModelScope.launch {
                progressBarVisibility.set(true)
                when(val response = loginOtpValidationRepository.validateOtp(mobile = mobileNumber.toString(),
                    otp = otpNumber.get()!!, login = TRUE)){
                    is Result.Error -> {
                        progressBarVisibility.set(false)
//                        _errorMessages.postValue("Please enter valid OTP")
                    }
                    is Result.Success -> {
                        val apiresponse  =  response.data
                        userPreferencesRepository.saveString(
                            MOBILE_NO,
                            mobileNumber.toString()
                        )
                        apiresponse?.session.let { session ->
                            session?.access.let {
                                session?.access?.let { it1 ->
                                    userPreferencesRepository.saveString(ACCESS_TOKEN,
                                        it1
                                    )
                                }
                                if (session != null) {
                                    userPreferencesRepository.saveString(REFRESH_TOKEN, session.refresh)
                                }
                            }

                        }
                        _showMessageLiveData.postValue("User Validated Successfully")
                        progressBarVisibility.set(false)
                        loginButtonVisibility.set(false)
                        otpFieldVisibility.set(true)
                        _loginLiveData.postValue(true)
                    }
                }
            }
        } else {
            _errorMessages.postValue("Please enter valid OTP")
        }

    }



    private fun validateOtp(otp: String): Boolean {
        return otp.length == 6
    }

    private fun isValidMobileNumber(mobileNumber: String): Boolean {
        val regex = Regex("^[6-9]\\d{9}$")
        return regex.matches(mobileNumber)
    }


    fun login(){
        if (!networkStatus.isOnline()){
            _errorMessages.postValue("Please check your internet connection")
            return
        }
        val mobileNumber = phoneNumber.get()
        if (mobileNumber?.let { validateMobileNo(it).not() } != true) {
            viewModelScope.launch {
                progressBarVisibility.set(true)
                when(val response = loginOtpValidationRepository.generateOtp(mobileNumber.toString())){
                    is Result.Error -> {
                        progressBarVisibility.set(false)
                    }
                    is Result.Success -> {
                        _showMessageLiveData.postValue("OTP Send Successfully")
                        progressBarVisibility.set(false)
                        loginButtonVisibility.set(false)
                        otpFieldVisibility.set(true)
                    }
                }

            }
        } else {
            _errorMessages.postValue("Enter Valid Phone Number")
        }

    }



    companion object{
        private const val ACCESS_TOKEN = "ACCESS_TOKEN"
        private const val REFRESH_TOKEN = "REFRESH_TOKEN"
        private const val MOBILE_NO = "MOBILE_NO"
        private const val TRUE = "True"
    }
}