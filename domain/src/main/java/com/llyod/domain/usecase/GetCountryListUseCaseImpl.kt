package com.llyod.domain.usecase

import com.llyod.domain.common.Result
import com.llyod.domain.entity.country.CountryListUI
import com.llyod.domain.repository.LocationRepository
import javax.inject.Inject

class GetCountryListUseCaseImpl @Inject constructor(private val locationRepository: LocationRepository) : GetCountryListUseCase {
    override suspend fun invoke(): Result<CountryListUI?>  =
        locationRepository.getLocationList()
}