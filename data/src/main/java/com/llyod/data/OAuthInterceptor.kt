package com.llyod.data

import com.llyod.data.repository.UserPreferencesRepo
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class OAuthInterceptor @Inject constructor(val userPreferencesRepo: UserPreferencesRepo) :Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        request = request.newBuilder()
            .header("Authorization", "Bearer "+userPreferencesRepo.getString("ACCESS_TOKEN",""))
            .build()
        return chain.proceed(request)
    }


}