package com.llyod.domain.model.form

data class DistrictsForStatesResponse(
    val details: List<Detail>,
    val state_name: String,
    val status: Int
)