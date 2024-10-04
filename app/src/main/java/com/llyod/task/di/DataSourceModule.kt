package com.llyod.task.di

import android.content.Context
import com.llyod.data.common.Mapper
import com.llyod.data.common.NetworkStatus
import com.llyod.data.common.NetworkStatusImpl
import com.llyod.data.datasource.forecast.LocationDataSource
import com.llyod.data.datasource.forecast.LocationDataSourceImpl
import com.llyod.data.datasource.login.LoginDataSource
import com.llyod.data.datasource.login.LoginDataSourceImpl
import com.llyod.data.entity.location.country.CountryList
import com.llyod.data.mapper.LocationDataMapperToDomain
import com.llyod.data.network.CountryService
import com.llyod.data.network.LoginService
import com.llyod.data.network.TokenService
import com.llyod.data.repository.UserPreferencesRepo
import com.llyod.domain.entity.country.CountryListUI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {


    @Provides
    @Singleton
    fun provideCountryDataSource(
        mapperToDomain: Mapper<CountryList,CountryListUI>,
        countryService: CountryService,
        networkStatus: NetworkStatus,
    ): LocationDataSource {
        return LocationDataSourceImpl(
            mapperToDomain,
            countryService,
            networkStatus
        )
    }

    @Provides
    @Singleton
    fun provideLoginDataSource(
        @ApplicationContext context: Context,
        loginService: LoginService,
        tokenService: TokenService,
         userPreferencesRepo: UserPreferencesRepo,
        networkStatus: NetworkStatus,
    ): LoginDataSource {
        return LoginDataSourceImpl(
            context,
            tokenService,
            loginService,
//            tokenService,
            networkStatus,
            userPreferencesRepo,
            )
    }


    @Provides
    @Singleton
    fun provideNetwork(@ApplicationContext context: Context) : NetworkStatus{
        return NetworkStatusImpl(context)
    }

    @Provides
    @Singleton
    fun provideLocationMapper() : Mapper<CountryList,CountryListUI>{
        return LocationDataMapperToDomain()
    }

}