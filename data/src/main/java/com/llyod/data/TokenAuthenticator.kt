package com.llyod.data

import android.util.Log
import com.llyod.data.network.LoginService
import com.llyod.data.network.TokenService
import com.llyod.data.repository.UserPreferencesRepo
import com.llyod.domain.common.Result
import com.llyod.domain.common.Result.Success
import com.llyod.domain.exception.NotFoundException
import com.llyod.domain.exception.UnAuthorizedApi
import com.llyod.domain.model.access.RefreshTokenRequest
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

class TokenAuthenticator @Inject constructor(
    private  val userPreferencesRepo: UserPreferencesRepo) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request {
        synchronized(this) {
            val sessionData = if (isRefreshNeeded(response)) {
                runBlocking { getUpdatedSessionData() }
            } else {
                getExistingSessionData()
            }
            return response.request.newBuilder()
                .header("Authorization", "Bearer "+userPreferencesRepo.getString(ACCESS_TOKEN,""))
                .build()
        }
    }

    private fun isRefreshNeeded(response: Response): Boolean {
        val oldSessionId = response.request.header(ACCESS_TOKEN)
        val oldRefreshId = response.request.header(REFRESH_TOKEN)

        val updatedSessionId =  userPreferencesRepo.getString(ACCESS_TOKEN,"")
        val updatedRefreshId =  userPreferencesRepo.getString(REFRESH_TOKEN,"")

        return (oldSessionId == updatedSessionId && oldRefreshId == updatedRefreshId)
    }

    private fun getExistingSessionData(): Boolean {
        val updatedSessionId =  userPreferencesRepo.getString(ACCESS_TOKEN,"")
        val updatedRefreshId =  userPreferencesRepo.getString(REFRESH_TOKEN,"")
        return true
    }

    private suspend fun getUpdatedSessionData(): Boolean {
        val refreshTokenRequest =
            userPreferencesRepo.getString(REFRESH_TOKEN, "")
        val resfreshToken = RefreshTokenRequest(refreshTokenRequest)
        val response = userApiService().getAccessToken(resfreshToken)
        Log.e("response",response.toString())
        if (response.isSuccessful) {
            if (response.isSuccessful) {
                response.body()?.let { session ->
                    userPreferencesRepo.saveString(ACCESS_TOKEN,session.access)
                }
                return true
            } else {
                return  false
            }
        } else{
            return false
        }
    }


    private fun userApiService(): LoginService {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        val okHttpClient = OkHttpClient.Builder().addInterceptor(loggingInterceptor).build()

        val retrofit = Retrofit.Builder()
            .baseUrl("http://13.233.163.123:8008")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()

        return retrofit.create(LoginService::class.java)
    }
    companion object {
        const val ACCESS_TOKEN = "ACCESS_TOKEN"
        const val REFRESH_TOKEN = "REFRESH_TOKEN"
        const val BEARER ="Bearer "
    }

}