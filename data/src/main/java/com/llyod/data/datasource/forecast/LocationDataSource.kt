package com.llyod.data.datasource.forecast

import com.llyod.domain.entity.country.CountryListUI
import com.llyod.domain.common.Result

interface LocationDataSource {

    suspend fun getLocationList() : Result<CountryListUI?>
}