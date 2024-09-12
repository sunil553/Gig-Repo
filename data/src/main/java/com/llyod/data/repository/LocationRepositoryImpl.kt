package com.llyod.data.repository

import com.llyod.data.datasource.forecast.LocationDataSource
import com.llyod.domain.common.Result
import com.llyod.domain.entity.country.CountryListUI
import com.llyod.domain.repository.LocationRepository
import javax.inject.Inject

class LocationRepositoryImpl @Inject constructor(private val locationDataSource: LocationDataSource) : LocationRepository{
    override suspend fun getLocationList(
    ): Result<CountryListUI?>  = locationDataSource.getLocationList()

}