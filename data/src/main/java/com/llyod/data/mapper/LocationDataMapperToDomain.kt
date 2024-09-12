package com.llyod.data.mapper

import com.llyod.data.common.Mapper
import com.llyod.data.entity.location.country.CountryList
import com.llyod.domain.entity.country.CountryListItem
import com.llyod.domain.entity.country.CountryListUI
import javax.inject.Inject

class LocationDataMapperToDomain @Inject constructor() : Mapper<CountryList, CountryListUI> {

    override fun mapFrom(from: CountryList): CountryListUI {
        val countryListItems: MutableList<CountryListItem> = mutableListOf()
        countryListItems.addAll(
            from.map { item ->
                CountryListItem(item.ID,item.EnglishName,item.LocalizedName)
            }
        )
        return  CountryListUI(countryListItems)
    }

    override fun mapTo(from: CountryListUI): CountryList {
        TODO("Not yet implemented")
    }
}