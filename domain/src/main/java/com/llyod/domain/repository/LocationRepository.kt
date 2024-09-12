package com.llyod.domain.repository

import com.llyod.domain.common.Result
import com.llyod.domain.entity.country.CountryListUI

interface LocationRepository {

     suspend fun getLocationList() : Result<CountryListUI?>

}