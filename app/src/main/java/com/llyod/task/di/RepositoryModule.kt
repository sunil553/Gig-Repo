package com.llyod.task.di

import android.content.Context
import android.content.SharedPreferences
import com.llyod.data.datasource.forecast.LocationDataSource
import com.llyod.data.datasource.login.LoginDataSource
import com.llyod.data.repository.LocationRepositoryImpl
import com.llyod.data.repository.LoginOtpValidationRepositoryImpl
import com.llyod.data.repository.UserPreferencesRepo
import com.llyod.data.repository.UserPreferencesRepository
import com.llyod.domain.repository.LocationRepository
import com.llyod.domain.repository.LoginOtpValidationRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class SharedPref

    @Provides
    @Singleton
    fun provideCountryRepository(
        locationDataSource: LocationDataSource
    ): LocationRepository {
        return LocationRepositoryImpl(locationDataSource)
    }
    @Provides
    @Singleton
    fun provideLoginRepository(
        loginDataSource: LoginDataSource
    ): LoginOtpValidationRepository {
        return LoginOtpValidationRepositoryImpl(loginDataSource)
    }
    @Provides
    @Singleton
    fun provideUserPreferences(@SharedPref sharedPreferences: SharedPreferences): UserPreferencesRepo {
        return UserPreferencesRepository(sharedPreferences)
    }
    @Provides
    @Singleton
    @SharedPref
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("GIGWorkerPref", Context.MODE_PRIVATE)

        /* @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("GIGWorkerPref", Context.MODE_PRIVATE)
    }*/

    }

}