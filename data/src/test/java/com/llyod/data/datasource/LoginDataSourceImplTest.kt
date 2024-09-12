package com.llyod.data.datasource

import com.llyod.data.common.Mapper
import com.llyod.data.common.NetworkStatus
import com.llyod.data.datasource.forecast.LocationDataSource
import com.llyod.data.datasource.forecast.LocationDataSourceImpl
import com.llyod.data.entity.location.country.CountryList
import com.llyod.data.mapper.LocationDataMapperToDomain
import com.llyod.data.network.CountryService
import com.llyod.domain.common.Result
import com.llyod.domain.entity.country.CountryListUI
import com.llyod.domain.exception.NoNetworkException
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import retrofit2.Response

class LoginDataSourceImplTest {

    private lateinit var sut: LocationDataSource


    @Mock
    private lateinit var  networkStatusMock: NetworkStatus

    @Mock
    private lateinit var  locationDataMapperToDomainMock: Mapper<CountryList,CountryListUI>

    @Mock
    private lateinit var  countryServiceMock: CountryService

    @Mock
    private lateinit var  countryListUIMock: CountryListUI
    @Mock
    private lateinit var  countryListMock: CountryList


    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        sut = LocationDataSourceImpl(
            mapperToDomain = locationDataMapperToDomainMock,
            networkStatus = networkStatusMock,
            countryService = countryServiceMock
        )
    }

    @Test
    fun `getLocationList returns Success when response is successful`() = runTest {
        // Given
        val countryList = countryListMock
        val countryListUI = countryListUIMock
        val response = Response.success(countryList)

        `when`(networkStatusMock.isOnline()).thenReturn(true)
        `when`(countryServiceMock.getCountryList()).thenReturn(response)
        `when`(locationDataMapperToDomainMock.mapFrom(countryList)).thenReturn(countryListUI)

        // When
        val result = sut.getLocationList()

        // Then
        assert(result is Result.Success)
        assertEquals(countryListUI, (result as Result.Success).data)
    }



    @Test
    fun `getLocationList returns Error when network is offline`() = runTest {
        // Given
        `when`(networkStatusMock.isOnline()).thenReturn(false)

        // When
        val response = sut.getLocationList()

        // Then
        Assert.assertTrue(response is Result.Error)
        val result = response as Result.Error
        Assert.assertTrue(result.throwable is NoNetworkException)
    }



}