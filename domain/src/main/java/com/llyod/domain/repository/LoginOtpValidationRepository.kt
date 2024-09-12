package com.llyod.domain.repository

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

interface LoginOtpValidationRepository {

    suspend fun validateOtp(mobile: String, otp: String, login: String): Result<VerifyReponse?>

    suspend fun generateOtp(mobileNumber: String): Result<UserMobileOtpResponse?>

   suspend fun getAccessToken(refreshToken : String): Result<AccessTokenResponse?>

    suspend fun getStates(): com.llyod.domain.common.Result<StatesModelResponse?>

    suspend fun getDistricts( stateId : String): com.llyod.domain.common.Result<DistrictsForStatesResponse?>

    suspend fun getServiceTypes(): com.llyod.domain.common.Result<ServiceResponse?>

    suspend fun getVehicleTypes(): com.llyod.domain.common.Result<VehicleTypeResponse?>

    suspend fun getQualificationTypes(): com.llyod.domain.common.Result<QualificationResponse?>

    suspend fun getCompanyTypes(): com.llyod.domain.common.Result<CompanyReponse?>

    suspend fun registerUser(registerRequest: RegisterRequestModel): com.llyod.domain.common.Result<RegisterResponse?>

    suspend fun getRegisteredUser(): com.llyod.domain.common.Result<RegisteredUserResponse?>

}