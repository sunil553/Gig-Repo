package com.llyod.domain.model.userregistration

data class BankDetails(
    val account_no: String,
    val branch_name: String,
    val ifsc_code: String
)