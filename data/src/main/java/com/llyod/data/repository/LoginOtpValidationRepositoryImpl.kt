package com.llyod.data.repository

//import com.llyod.task.SharedPreferencesManager
import com.llyod.data.datasource.login.LoginDataSource
import com.llyod.domain.common.Result
import com.llyod.domain.model.UserMobileOtpResponse
import com.llyod.domain.model.access.AccessTokenResponse
import com.llyod.domain.model.form.CompanyReponse
import com.llyod.domain.model.form.DistrictsForStatesResponse
import com.llyod.domain.model.form.QualificationResponse
import com.llyod.domain.model.form.ServiceResponse
import com.llyod.domain.model.form.StatesModelResponse
import com.llyod.domain.model.form.VehicleTypeResponse
import com.llyod.domain.model.register.RegisterRequestModel
import com.llyod.domain.model.register.RegisterResponse
import com.llyod.domain.model.userregistration.RegisteredUserResponse
import com.llyod.domain.model.verify.VerifyReponse
import com.llyod.domain.repository.LoginOtpValidationRepository
import javax.inject.Inject

class LoginOtpValidationRepositoryImpl @Inject constructor(private val loginDataSource: LoginDataSource) : LoginOtpValidationRepository {

    var stateReponse: StatesModelResponse? = null

    var districtResponse: DistrictsForStatesResponse? = null

    var serviceResponse: ServiceResponse? = null

    var vehicleResponse: VehicleTypeResponse? = null

    var qualification: QualificationResponse? = null

    var companyResponse: CompanyReponse? = null

    override suspend fun validateOtp(mobile: String, otp: String, login: String): Result<VerifyReponse?> {
        return loginDataSource.validateOtp(mobile, otp, login)
    }

    override suspend fun generateOtp(mobileNumber: String): Result<UserMobileOtpResponse?> {
        return loginDataSource.generateOtp(mobileNumber)
    }

    override suspend fun getAccessToken(refreshToken: String): Result<AccessTokenResponse?> {
        return loginDataSource.getAccessToken(refreshToken)
    }

    override suspend fun getStates(): Result<StatesModelResponse?> {
        return if (stateReponse !=null) {
            Result.Success(stateReponse)
        } else {
            when(val response  = loginDataSource.getStates()){
                is Result.Error -> {
                    Result.Error(Exception())
                }
                is Result.Success -> {
                    stateReponse = response.data
                    Result.Success(stateReponse)
                }
            }
        }
    }

    override suspend fun getDistricts(stateId: String): Result<DistrictsForStatesResponse?> {
        return if (districtResponse !=null) {
            Result.Success(districtResponse)
        } else {
            when(val response  = loginDataSource.getDistricts(stateId)){
                is Result.Error -> {
                    Result.Error(Exception())
                }
                is Result.Success -> {
                    districtResponse = response.data
                    Result.Success(districtResponse)
                }
            }
        }
    }

    override suspend fun getServiceTypes(): Result<ServiceResponse?> {
        return if (serviceResponse !=null) {
            Result.Success(serviceResponse)
        } else {
            when(val response  = loginDataSource.getServiceTypes()){
                is Result.Error -> {
                    Result.Error(Exception())
                }
                is Result.Success -> {
                    serviceResponse = response.data
                    Result.Success(serviceResponse)
                }
            }
        }
    }

    override suspend fun getVehicleTypes(): Result<VehicleTypeResponse?> {
        return if (vehicleResponse !=null) {
            Result.Success(vehicleResponse)
        } else {
            when(val response  = loginDataSource.getVehicleTypes()){
                is Result.Error -> {
                    Result.Error(Exception())
                }
                is Result.Success -> {
                    vehicleResponse = response.data
                    Result.Success(vehicleResponse)
                }
            }
        }
    }

    override suspend fun getQualificationTypes(): Result<QualificationResponse?> {
        return if (qualification !=null) {
            Result.Success(qualification)
        } else {
            when(val response  = loginDataSource.getQualificationTypes()){
                is Result.Error -> {
                    Result.Error(Exception())
                }
                is Result.Success -> {
                    qualification = response.data
                    Result.Success(qualification)
                }
            }
        }
    }

    override suspend fun getCompanyTypes(): Result<CompanyReponse?> {
        return if (companyResponse !=null) {
            Result.Success(companyResponse)
        } else {
            when(val response  = loginDataSource.getCompanyTypes()){
                is Result.Error -> {
                    Result.Error(Exception())
                }
                is Result.Success -> {
                    companyResponse = response.data
                    Result.Success(companyResponse)
                }
            }
        }
    }

    override suspend fun registerUser(registerRequest: RegisterRequestModel): Result<RegisterResponse?> {
        return loginDataSource.registerUser(registerRequest)
    }

    override suspend fun getRegisteredUser(): Result<RegisteredUserResponse?> {
        return loginDataSource.getRegisteredUser()
    }


}