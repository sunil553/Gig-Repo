package com.llyod.data.network

import com.llyod.domain.model.UserMobileData
import com.llyod.domain.model.UserMobileOtpResponse
import com.llyod.domain.model.access.AccessTokenResponse
import com.llyod.domain.model.access.RefreshTokenRequest
import com.llyod.domain.model.form.CompanyReponse
import com.llyod.domain.model.form.DistrictsForStatesResponse
import com.llyod.domain.model.form.QualificationResponse
import com.llyod.domain.model.form.ServiceResponse
import com.llyod.domain.model.form.StatesModelResponse
import com.llyod.domain.model.form.VehicleTypeResponse
import com.llyod.domain.model.register.RegisterResponse
import com.llyod.domain.model.userregistration.RegisteredUserResponse
import com.llyod.domain.model.verify.VerifyReponse
import com.llyod.domain.model.verify.VerifyRequest
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path


interface LoginService {
/*
    @Headers("Content-Type: application/json")
    @POST("/sendotp/")
    suspend fun sendOtp(@Body body : UserMobileData) : Response<UserMobileOtpResponse>


    @Headers("Content-Type: application/json")
    @POST("/verifyotp/")
    suspend fun verifyOtp(@Body body : VerifyRequest) : Response<VerifyReponse>

*/


    @Headers("Content-Type: application/json")
    @POST("api/token/refresh/")
    suspend fun getAccessToken(@Body boResponse: RefreshTokenRequest) : Response<AccessTokenResponse>

    @POST("add/user/")
    suspend fun registerUser(
        /*@Header("Authorization") token: String,*/
        @Body file: MultipartBody, /*@Body requestBody: RegisterRequestModel*/
    ) : Response<RegisterResponse>

    @GET("/get/states/")
    @Headers("Content-Type: application/json")
    suspend fun getStates() : Response<StatesModelResponse>


    @GET("/get/districts/{id}")
    @Headers("Content-Type: application/json")
    suspend fun getDistrictByStateId(@Path("id") searchById:String)  : Response<DistrictsForStatesResponse>


    @GET("/get/qualifications/")
    @Headers("Content-Type: application/json")
    suspend fun getQualificationTypes() : Response<QualificationResponse>

    @GET("/get/services/")
    @Headers("Content-Type: application/json")
    suspend fun getServices() : Response<ServiceResponse>

    @GET("/get/vehicletypes/")
    @Headers("Content-Type: application/json")
    suspend fun getVehicleTypes() : Response<VehicleTypeResponse>

    @GET("/get/companies/")
    @Headers("Content-Type: application/json")
    suspend fun getCompanies() : Response<CompanyReponse>


    @GET("/get/user/")
    @Headers("Content-Type: application/json")
    suspend fun getRegisteredUser() : Response<RegisteredUserResponse>


    @Multipart
    @POST("add/user/")
    fun uploadFormData(
        @Header("Authorization") token: String,
        @Part("aadhaar_number") aadhaarNumber: RequestBody?,
        @Part("account_no") accountNo: RequestBody?,
        @Part("applicant_name") applicantName: RequestBody?,
        @Part("branch_name") branchName: RequestBody?,
        @Part("colony_area") colonyArea: RequestBody?,
        @Part("district") district: RequestBody?,
        @Part("dob") dob: RequestBody?,
        @Part("doj") doj: RequestBody?,
        @Part("email") email: RequestBody?,
        @Part("father_name") fatherName: RequestBody?,
        @Part("gender") gender: RequestBody?,
        @Part("house_flat_no") houseFlatNo: RequestBody?,
        @Part("ifsc_code") ifscCode: RequestBody?,
        @Part("is_address_same") isAddressSame: RequestBody?,
        @Part("landmark") landmark: RequestBody?,
        @Part("mobile") mobile: RequestBody?,
        @Part("nominee_account_no") nomineeAccountNo: RequestBody?,
        @Part("nominee_age") nomineeAge: RequestBody?,
        @Part("nominee_branch") nomineeBranch: RequestBody?,
        @Part("nominee_ifsc_code") nomineeIfscCode: RequestBody?,
        @Part("nominee_name") nomineeName: RequestBody?,
        @Part("nominee_relation") nomineeRelation: RequestBody?,
        @Part("office_address") officeAddress: RequestBody?,
        @Part("other_qualification") otherQualification: RequestBody?,
        @Part("pan_number") panNumber: RequestBody?,
        @Part("pincode") pincode: RequestBody?,
        @Part("primary_company") primaryCompany: RequestBody?,
        @Part("primary_working_id") primaryWorkingId: RequestBody?,
        @Part("profile_image") profileImage: Part?,
        @Part("qualification") qualification: RequestBody?,
        @Part("service") service: RequestBody?,
        @Part("state") state: RequestBody?,
        @Part("street_name") streetName: RequestBody?,
        @Part("temp_colony_area") tempColonyArea: RequestBody?,
        @Part("temp_district") tempDistrict: RequestBody?,
        @Part("temp_house_no") tempHouseNo: RequestBody?,
        @Part("temp_landmark") tempLandmark: RequestBody?,
        @Part("temp_pincode") tempPincode: RequestBody?,
        @Part("temp_state") tempState: RequestBody?,
        @Part("temp_street_name") tempStreetName: RequestBody?,
        @Part("vehicle") vehicle: RequestBody?,
        @Part("worker_type") workerType: RequestBody?,
        @Part("working_hours") workingHours: RequestBody?,
        @Part("working_type") workingType: RequestBody?
    ): Response<RegisterResponse>


}