package com.llyod.data.network

import com.llyod.data.entity.location.country.CountryList
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CountryService {
    @GET("/locations/v1/countries/{country}")
    suspend fun getCountryList(@Path("country") country: String? = "asia", @Query("apikey") apikey : String = "GjTnYZyAtybjSX4ILrQCv5GsrjJhxjbV"/*"dGtnGkYUSdu7KkAEb1SyY52ZZxR8ejRz"*/) : Response<CountryList>
}