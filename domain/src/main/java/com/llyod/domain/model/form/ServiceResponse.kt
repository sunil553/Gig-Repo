package com.llyod.domain.model.form

data class ServiceResponse(
    val details: List<Detail>,
    val status: Int
)