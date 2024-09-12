package com.llyod.domain.usecase

import com.llyod.domain.common.Result
import com.llyod.domain.entity.country.CountryListUI

interface GetCountryListUseCase {
    suspend operator fun invoke() : Result<CountryListUI?>
}