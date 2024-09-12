package com.llyod.task

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class PersonalInformation(val workerProfile: WorkerProfile) : Parcelable

@Parcelize
data class WorkerProfile(
    val passportSizePhoto: String, // This should be a path or URL to the image file
    val fullName: String,
    val gender: String,
    val dateOfBirth: String, // Consider using LocalDate for better date handling
    val aadharNumber: String,
    val panNumber: String,
    val mobileNumber: String,
    val qualification: String,
    val typeOfWorker: WorkerType,
    val vehicleType: VehicleType,
    val typeOfService: String,
    val workingHoursPerDay: Int,
    val email: String,
    val fatherName: String,
//    remove if not required
    val permanentAddress: Address,
    val presentAddress: Address,
    val isPresentAddressSame: Boolean,
    val bankAccountDetails: BankAccountDetails,
    val nominee: Nominee
) : Parcelable {
    enum class WorkerType {
        DRIVER, NON_DRIVER
    }

    enum class VehicleType {
        TWO_WHEELER, THREE_WHEELER, FOUR_WHEELER
    }

    @Parcelize
    data class Address(
        val houseFlatNo: String,
        val streetName: String?,
        val colonyArea: String?,
        val landMark: String?,
        val cityDistrict: String,
        val pinCode: String
    ) : Parcelable

    @Parcelize
    data class BankAccountDetails(
        val accountNumber: String,
        val ifscCode: String,
        val branch: String?
    ) : Parcelable

    @Parcelize
    data class Nominee(
        val nomineeName: String,
        val relation: String,
        val age: Int,
        val nomineeBankAccountDetails: NomineeBankAccountDetails
    ) : Parcelable


    @Parcelize
    data class NomineeBankAccountDetails(
        val accountNumber: String,
        val ifscCode: String,
        val branch: String
    ) : Parcelable
}
