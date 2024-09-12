package com.llyod.data

import com.llyod.data.network.LoginService
import com.llyod.data.network.TokenService
import com.llyod.data.repository.UserPreferencesRepo
import com.llyod.domain.model.access.RefreshTokenRequest
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
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
                .header("Authorization", "Bearer "+userPreferencesRepo.getString("ACCESS_TOKEN",""))
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
        return when (val result = userApiService().getAccessToken(resfreshToken).isSuccessful ) {
            true -> {
                userPreferencesRepo.saveString(ACCESS_TOKEN,result.toString())
//                userPreferencesRepo.saveString("REFRESH_TOKEN",sessionData.refreshId)
                delay(50)
                true
            }
            false ->{
                false
            }
        }
    }


    private fun userApiService(): TokenService {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://13.233.163.123:8008")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(TokenService::class.java)
    }
    companion object {
        const val ACCESS_TOKEN = "ACCESS_TOKEN"
        const val REFRESH_TOKEN = "REFRESH_TOKEN"
        const val BEARER ="Bearer "
    }

}