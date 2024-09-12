package com.llyod.task.viewmodel

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.llyod.data.repository.UserPreferencesRepo
import com.llyod.data.repository.UserPreferencesRepository
import com.llyod.domain.common.Result
import com.llyod.domain.repository.LoginOtpValidationRepository
import com.llyod.task.SharedPreferencesManager
import com.llyod.task.activity.LoginActivity
import com.llyod.task.activity.LoginActivity.Companion
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
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

    private val _errorMessages = MutableLiveData<String>()
    val errorMessages: LiveData<String> = _errorMessages

    init {
        otpFieldVisibility.set(false)
        loginButtonVisibility.set(true)
    }
    private fun validateMobileNo(mobileNo: String): Boolean {
        return mobileNo.length == 10
    }
    fun getAccessToken(): Boolean {
        return userPreferencesRepository.getString(MOBILE_NO,"").isNotEmpty()
    }


    fun getOtpForMobileNo(){
        if (otpNumber.get()?.let { validateOtp(otp = it) } == true) {
            val mobileNumber = phoneNumber.get()
            viewModelScope.launch {
                progressBarVisibility.set(true)
                when(val response = loginOtpValidationRepository.validateOtp(mobile = mobileNumber.toString(),
                    otp = otpNumber.get()!!, login = TRUE)){
                    is Result.Error -> {
                        progressBarVisibility.set(false)
                        _errorMessages.postValue("Invalid OTP")
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
                        progressBarVisibility.set(false)
                        loginButtonVisibility.set(false)
                        otpFieldVisibility.set(true)
                        _loginLiveData.postValue(true)
                    }
                }
            }
        }

    }

    fun validateOtp(otp: String): Boolean {
        return otp.length == 6
    }

    fun login(){
        val mobileNumber = phoneNumber.get()
        if (mobileNumber?.let { validateMobileNo(it).not() } != true) {
            viewModelScope.launch {
                progressBarVisibility.set(true)
                when(val response = loginOtpValidationRepository.generateOtp(mobileNumber.toString())){
                    is Result.Error -> {
                        progressBarVisibility.set(false)

                    }

                    is Result.Success -> {
                        progressBarVisibility.set(false)
                        loginButtonVisibility.set(false)
                        otpFieldVisibility.set(true)


                    }
                }

            }
        }

    }



    companion object{
        private const val ACCESS_TOKEN = "ACCESS_TOKEN"
        private const val REFRESH_TOKEN = "REFRESH_TOKEN"
        private const val MOBILE_NO = "MOBILE_NO"
        private const val TRUE = "True"
    }
}