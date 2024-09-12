package com.llyod.data.datasource

import com.llyod.data.datasource.forecast.LocationDataSource
import com.llyod.data.repository.LocationRepositoryImpl
import com.llyod.domain.common.Result
import com.llyod.domain.repository.LocationRepository
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.verify

class LocationRepositoryImplTest {

    @Mock
    private lateinit var locationDataSourceMock: LocationDataSource

    private lateinit var  sut: LocationRepository
    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        sut = LocationRepositoryImpl(locationDataSourceMock)
    }

    @Test
    fun `getLocationList returns Success result from data source`(): Unit = runTest {
        // When
          sut.getLocationList()

        // Then
        verify(locationDataSourceMock).getLocationList()
    }

}