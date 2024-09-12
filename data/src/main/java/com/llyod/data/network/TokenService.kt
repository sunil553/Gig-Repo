package com.llyod.data.network

import com.llyod.domain.model.UserMobileData
import com.llyod.domain.model.UserMobileOtpResponse
import com.llyod.domain.model.access.AccessTokenResponse
import com.llyod.domain.model.access.RefreshTokenRequest
import com.llyod.domain.model.verify.VerifyReponse
import com.llyod.domain.model.verify.VerifyRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface TokenService {

    @Headers("Content-Type: application/json")
    @POST("/sendotp/")
    suspend fun sendOtp(@Body body : UserMobileData) : Response<UserMobileOtpResponse>


    @Headers("Content-Type: application/json")
    @POST("/verifyotp/")
    suspend fun verifyOtp(@Body body : VerifyRequest) : Response<VerifyReponse>


}