package com.llyod.domain.model.userregistration

data class RegisteredUserResponse(
    val aggregator_status : String,
    val admin_status : String,
    val address: Address,
    val bank_details: BankDetails,
    val company_details: List<CompanyDetail>,
    val nominee_details: NomineeDetails,
    val personal_details: PersonalDetails,
    val registered: Boolean,
    val status: Int,
    val unique_id: String
)