package com.llyod.domain.model.userregistration

data class PresentAddress(
    val colony_area: String,
    val district: String,
    val house_no: String,
    val landmark: String,
    val pincode: String,
    val state: String,
    val street_name: String
)