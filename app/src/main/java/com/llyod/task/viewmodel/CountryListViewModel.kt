package com.llyod.task.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.llyod.domain.common.Result
import com.llyod.domain.entity.country.CountryListItem
import com.llyod.domain.usecase.GetCountryListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CountryListViewModel @Inject constructor(
    private val getCountryListUseCase: GetCountryListUseCase
) : ViewModel() {


    private val _countryListFlow : MutableStateFlow<List<CountryListItem>> = MutableStateFlow(listOf())
    val countryListFlow : StateFlow<List<CountryListItem>> get() = _countryListFlow

    private val _loading = MutableStateFlow(false)
    val loading: MutableStateFlow<Boolean> get() = _loading

    init {
        getCountryList()
    }


    fun getCountryList(){
        viewModelScope.launch {
            _loading.value = true
            when(val response = getCountryListUseCase()) {
                is Result.Error ->{
                    _loading.value = false

                }
                is Result.Success -> {
                    _loading.value = false
                    response.data?.let {
                        _countryListFlow.emit(it.countryListItem)
                    }
                }
            }
        }

    }

}