package com.llyod.domain.model.userregistration

data class PersonalDetails(
    val aadhaar_number: String,
    val application_id: String,
    val dob: String,
    val email: String,
    val father_name: String,
    val gender: String,
    val id: Int,
    val identity_card_number: String,
    val mobile_number: String,
    val name: String,
    val pan_number: String,
    val profile_image: String,
    val qualification: String,
    val worker_type: String,
    val working_hours: String,
    val working_type: String
)