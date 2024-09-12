package com.llyod.task.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.llyod.data.OAuthInterceptor
import com.llyod.data.TokenAuthenticator
import com.llyod.data.network.CountryService
import com.llyod.data.network.LoginService
import com.llyod.data.network.TokenService
import com.llyod.data.repository.UserPreferencesRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Authenticator
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    const val BASE_URL = "http://13.233.163.123:8008"

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class NonAuthRetrofit

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class NonAuthOkHttpClient

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class CustomGsonWithTypeAdapters

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class HttpLoggingInterceptors

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class OAuthInterceptors

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class OAuthAuthenticator


    @Provides
    @NonAuthRetrofit
    @Singleton
    fun provideNonAuthRetrofit(
        @NonAuthOkHttpClient nonAuthOkHttpClient: OkHttpClient,
        @CustomGsonWithTypeAdapters gson: Gson,
    ): Retrofit {
        return Retrofit.Builder()
            .client(nonAuthOkHttpClient)
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

    }

    @Provides
    @CustomGsonWithTypeAdapters
    fun provideGson(
    ): Gson {
        val builder = GsonBuilder()
        return builder.create()
    }

    @Provides
    @NonAuthOkHttpClient
    fun provideNonAuthOkHttp(
        @HttpLoggingInterceptors loggingInterceptor: Interceptor,
        @OAuthAuthenticator authenticator: Authenticator,
//        @OAuthInterceptors oAuthInterceptor: Interceptor
    ): OkHttpClient {
        return OkHttpClient
            .Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15,TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor)
//            .addInterceptor(oAuthInterceptor)
            .authenticator(authenticator)
            .build()
    }

    @Provides
    @HttpLoggingInterceptors
    fun provideHttpLoggingInterceptor(): Interceptor {
        val levelType: HttpLoggingInterceptor.Level = HttpLoggingInterceptor.Level.BODY
        val logging = HttpLoggingInterceptor()
        logging.setLevel(levelType)
        return logging
    }

    @Provides
    @Singleton
    @OAuthInterceptors
    fun provideOAuthInterceptor(userPreferencesRepo: UserPreferencesRepo): Interceptor{
        return OAuthInterceptor(userPreferencesRepo)
    }



    @Provides
    @Singleton
    fun provideCountryService(@NonAuthRetrofit retrofit: Retrofit):
            CountryService = retrofit.create(
        CountryService::class.java
    )

    @Provides
    @Singleton
    fun provideLoginService(@NonAuthRetrofit retrofit: Retrofit):
            LoginService = retrofit.create(
        LoginService::class.java
    )
    /*
        @Provides
        @Singleton
        fun provideTokenService(@NonAuthRetrofit retrofit: Retrofit):
                TokenService = retrofit.create(
            TokenService::class.java
        )*/


    @Provides
    @Singleton
    @OAuthAuthenticator
    fun provideAuthenticatorService(userPreferencesRepo: UserPreferencesRepo):
            Authenticator {
        return TokenAuthenticator(userPreferencesRepo)
    }
}


