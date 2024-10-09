package com.llyod.task.model

import java.io.File

data class PersonalDetailsModel(
    val photo : File?,
    val name : String?,
    val gender : String?,
    val dob : String?,
    val aadhar : String?,
    val  pan : String?,
    val mobile : String?,
    val email : String?,
    val qualification : String?,
    val typeOfWork : String?,
    val workingType : String?,
    val vehicleType : String?,
    val noOfHours : String?,
    val fatherName : String?,
    val service : String?,
    val profile_photo : String?,
    val otherQualification : String? ,

    )

data class AddressDetailsModel(
    val house_no: String = "",
    val street_name: String = "",
    val colony_area: String = "",
    val landmark: String = "",
    val pincode: String = "",
    val state: String = "",
    val district: String = "",
    val temp_house_no: String = "",
    val temp_street_name: String = "",
    val temp_colony_area: String = "",
    val temp_landmark: String = "",
    val temp_pincode: String = "",
    val temp_state: String = "",
    val temp_district: String = "",
    val is_address_same : String = "0"
)

data class BankDetailsModel(
    val account_no: String?,
    val ifsc_code: String?,
    val bank_name: String?,
    val branch: String?,
    val nominee_account_no: String?,
    val nominee_age: String?,
    val nominee_branch: String?,
    val nominee_ifsc_code: String?,
    val nominee_name: String?,
    val nominee_relation: String?,
)

data class CompanyDetailsModel(
    val comapany_name : String?,
    val doj: String?,
    val primary_company: String?,
    val primary_working_id: String?,
    val office_address : String?
    )


data class Address(
    val temp_house_no: String = "",
    val temp_street_name: String = "",
    val temp_colony_area: String = "",
    val temp_landmark: String = "",
    val temp_pincode: String = "",
    val temp_state: String = "",
    val temp_district: String = ""
)
