package com.llyod.data.network

import com.llyod.domain.model.access.AccessTokenResponse
import com.llyod.domain.model.access.RefreshTokenRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface TokenService {

    @Headers("Content-Type: application/json")
    @POST("api/token/refresh/")
    suspend fun getAccessToken(@Body boResponse: RefreshTokenRequest) : Response<AccessTokenResponse>

}