package com.llyod.task.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.llyod.domain.common.Result
import com.llyod.domain.entity.country.CountryListItem
import com.llyod.domain.entity.country.CountryListUI
import com.llyod.domain.exception.NoNetworkException
import com.llyod.domain.usecase.GetCountryListUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mockito
import org.mockito.kotlin.mock

class CountryListViewModelTest {

    private lateinit var sut: CountryListViewModel

    private val getCountryListUseCase = mock<GetCountryListUseCase>()

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val testCoroutineRule = TestCoroutineRule()


    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        sut = CountryListViewModel(
            getCountryListUseCase
        )
    }

    @Test
    fun `getCountryList should emit country list when successful`() = runTest {
        // Given
        val countryList = listOf(CountryListItem("USA","USA","USA"))
        val result = Result.Success(CountryListUI(countryList))
        Mockito.`when`(getCountryListUseCase()).thenReturn(result)

        // When
        sut.getCountryList()

        // Then
        val emittedCountries = listOf( sut.countryListFlow.value)
        assertEquals(countryList, emittedCountries.first())
        assertEquals(false, sut.loading.value)
    }

    @Test
    fun `getCountryList should handle error`(): Unit = runTest {
        // Given
        val result = Result.Error(NoNetworkException())
        Mockito.`when`(getCountryListUseCase()).thenReturn(result)

        // When
        sut.getCountryList()

        // Then
        assertEquals(false, sut.loading.value)
    }


}