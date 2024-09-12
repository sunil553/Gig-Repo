package com.llyod.task.di

import com.llyod.domain.repository.LocationRepository
import com.llyod.domain.usecase.GetCountryListUseCase
import com.llyod.domain.usecase.GetCountryListUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {


    @Provides
    @Singleton
    fun provideCountryUseCase(
          locationRepository: LocationRepository
    ): GetCountryListUseCase {
        return GetCountryListUseCaseImpl(locationRepository)
    }

}