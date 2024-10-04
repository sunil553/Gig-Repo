package com.llyod.data.datasource.login

import android.content.Context
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.llyod.data.TokenAuthenticator.Companion.ACCESS_TOKEN
import com.llyod.data.common.NetworkStatus
import com.llyod.data.network.LoginService
import com.llyod.data.network.TokenService
import com.llyod.data.repository.UserPreferencesRepo
import com.llyod.domain.common.Result
import com.llyod.domain.common.Result.Success
import com.llyod.domain.exception.NoNetworkException
import com.llyod.domain.exception.NotFoundException
import com.llyod.domain.exception.UnAuthorizedApi
import com.llyod.domain.model.ErrorResponse
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
import com.llyod.domain.model.register.RegisterRequestModel
import com.llyod.domain.model.register.RegisterResponse
import com.llyod.domain.model.userregistration.RegisteredUserResponse
import com.llyod.domain.model.verify.VerifyReponse
import com.llyod.domain.model.verify.VerifyRequest
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import javax.inject.Inject


class LoginDataSourceImpl @Inject constructor(
    private val context : Context,
    private val tokenService: TokenService,
    private val loginService: LoginService,
    private val networkStatus: NetworkStatus,
    private val userPreferencesRepo: UserPreferencesRepo,
    private val ioDispatcher : CoroutineDispatcher = Dispatchers.IO) : LoginDataSource {


    override suspend fun validateOtp(mobile: String, otp: String, login: String): Result<VerifyReponse?> {
        val verifyRequest = VerifyRequest(mobile,otp,login)
        return try {
            if (networkStatus.isOnline()) {
                withContext(ioDispatcher) {
                    val response = tokenService.verifyOtp(verifyRequest)
                    if (response.isSuccessful) {
                        if (response.isSuccessful) {
                            return@withContext Success(response.body())
                        } else {
                            return@withContext Result.Error(NotFoundException())
                        }
                    } else {
                        return@withContext Result.Error(UnAuthorizedApi())
                    }
                }
            } else {
                Result.Error(NoNetworkException())
            }
        } catch (exception: Exception) {
            return Result.Error(exception)
        }
    }

    override suspend fun generateOtp(mobileNumber: String): Result<UserMobileOtpResponse?> {
        return try {
            if (networkStatus.isOnline()) {
                withContext(ioDispatcher) {
                    val userMobileData = UserMobileData(mobileNumber)
                    val response = tokenService.sendOtp(userMobileData)
                    if (response.isSuccessful) {
                        if (response.isSuccessful) {
                            return@withContext Success(response.body())
                        } else {
                            return@withContext Result.Error(NotFoundException())
                        }
                    } else {
                        return@withContext Result.Error(UnAuthorizedApi())
                    }
                }
            } else {
                Result.Error(NoNetworkException())
            }
        } catch (exception: Exception) {
            return Result.Error(exception)
        }
    }

    override suspend fun getAccessToken(refreshToken: String): Result<AccessTokenResponse?> {
        return try {
            if (networkStatus.isOnline()) {
                withContext(ioDispatcher) {
                    val refreshToken = RefreshTokenRequest(refreshToken)
                    val response = loginService.getAccessToken(refreshToken)
                    if (response.isSuccessful) {
                        if (response.isSuccessful) {
                            response.body()?.let { session ->
                                userPreferencesRepo.saveString(ACCESS_TOKEN,
                                    session.access
                                )
                            }
                            return@withContext Success(response.body())
                        } else {
                            return@withContext Result.Error(NotFoundException())
                        }
                    } else {
                        return@withContext Result.Error(UnAuthorizedApi())
                    }
                }
            } else {
                Result.Error(NoNetworkException())
            }
        } catch (exception: Exception) {
            return Result.Error(exception)
        }
    }
    override suspend fun getStates(): Result<StatesModelResponse?> {
        return try {
            if (networkStatus.isOnline()) {
                withContext(ioDispatcher) {
                    val response = loginService.getStates()
                    if (response.isSuccessful) {
                        if (response.isSuccessful) {
                            return@withContext Success(response.body())
                        } else {
                            return@withContext Result.Error(NotFoundException())
                        }
                    } else {
                        return@withContext Result.Error(UnAuthorizedApi())
                    }
                }
            } else {
                Result.Error(NoNetworkException())
            }
        } catch (exception: Exception) {
            return Result.Error(exception)
        }
    }

    override suspend fun getDistricts(stateId: String): Result<DistrictsForStatesResponse?> {
        return try {
            if (networkStatus.isOnline()) {
                withContext(ioDispatcher) {
                    val response = loginService.getDistrictByStateId(stateId)
                    if (response.isSuccessful) {
                        if (response.isSuccessful) {
                            return@withContext Success(response.body())
                        } else {
                            return@withContext Result.Error(NotFoundException())
                        }
                    } else {
                        return@withContext Result.Error(UnAuthorizedApi())
                    }
                }
            } else {
                Result.Error(NoNetworkException())
            }
        } catch (exception: Exception) {
            return Result.Error(exception)
        }       }

    override suspend fun getServiceTypes(): Result<ServiceResponse?> {
        return try {
            if (networkStatus.isOnline()) {
                withContext(ioDispatcher) {
                    val response = loginService.getServices()
                    if (response.isSuccessful) {
                        if (response.isSuccessful) {
                            return@withContext Success(response.body())
                        } else {
                            return@withContext Result.Error(NotFoundException())
                        }
                    } else {
                        return@withContext Result.Error(UnAuthorizedApi())
                    }
                }
            } else {
                Result.Error(NoNetworkException())
            }
        } catch (exception: Exception) {
            return Result.Error(exception)
        }       }

    override suspend fun getVehicleTypes(): Result<VehicleTypeResponse?> {
        return try {
            if (networkStatus.isOnline()) {
                withContext(ioDispatcher) {
                    val response = loginService.getVehicleTypes()
                    if (response.isSuccessful) {
                        if (response.isSuccessful) {
                            return@withContext Success(response.body())
                        } else {
                            return@withContext Result.Error(NotFoundException())
                        }
                    } else {
                        return@withContext Result.Error(UnAuthorizedApi())
                    }
                }
            } else {
                Result.Error(NoNetworkException())
            }
        } catch (exception: Exception) {
            return Result.Error(exception)
        }       }

    override suspend fun getQualificationTypes(): Result<QualificationResponse?> {
        return try {
            if (networkStatus.isOnline()) {
                withContext(ioDispatcher) {
                    val response = loginService.getQualificationTypes()
                    if (response.isSuccessful) {
                        if (response.isSuccessful) {
                            return@withContext Success(response.body())
                        } else {
                            return@withContext Result.Error(NotFoundException())
                        }
                    } else {
                        return@withContext Result.Error(UnAuthorizedApi())
                    }
                }
            } else {
                Result.Error(NoNetworkException())
            }
        } catch (exception: Exception) {
            return Result.Error(exception)
        }       }

    override suspend fun getCompanyTypes(): Result<CompanyReponse?> {
        return try {
            if (networkStatus.isOnline()) {
                withContext(ioDispatcher) {
                    val response = loginService.getCompanies()
                    if (response.isSuccessful) {
                        if (response.isSuccessful) {
                            return@withContext Success(response.body())
                        } else {
                            return@withContext Result.Error(NotFoundException())
                        }
                    } else {
                        return@withContext Result.Error(UnAuthorizedApi())
                    }
                }
            } else {
                Result.Error(NoNetworkException())
            }
        } catch (exception: Exception) {
            return Result.Error(exception)
        }       }

    override suspend fun registerUser( registerRequestModel: RegisterRequestModel): Result<RegisterResponse?> {
        return try {
            if (networkStatus.isOnline()) {
                withContext(ioDispatcher) {
                    val requestBody = buildData(registerRequestModel)
                    val response = loginService.registerUser(requestBody)
                    if (response.isSuccessful) {
                        return@withContext Success(response.body())
                    } else {
                        val gson = Gson()
                        val type = object : TypeToken<ErrorResponse>() {}.type
                        var errorResponse: ErrorResponse? = gson.fromJson(response.errorBody()!!.charStream(), type)
                        withContext(Dispatchers.Main){
                            Toast.makeText(context, errorResponse?.message, Toast.LENGTH_SHORT).show()
                        }
                        return@withContext Result.Error(UnAuthorizedApi())
                    }
                }
            } else {
                Result.Error(NoNetworkException())
            }
        } catch (exception: Exception) {
            return Result.Error(exception)
        }
    }

    override suspend fun getRegisteredUser(): Result<RegisteredUserResponse?> {
        return try {
            if (networkStatus.isOnline()) {
                withContext(ioDispatcher) {
                    val response = loginService.getRegisteredUser()
                    if (response.isSuccessful) {
                        return@withContext Success(response.body())
                    } else {
                        return@withContext Result.Error(UnAuthorizedApi())
                    }
                }
            } else {
                Result.Error(NoNetworkException())
            }
        } catch (exception: Exception) {
            return Result.Error(exception)
        }
    }
 private fun buildData(registerRequestModel: RegisterRequestModel): MultipartBody {

        return MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart("profile_image","Demo",
                registerRequestModel.profile_image?.asRequestBody("application/octet-stream".toMediaType())!!)
            .addFormDataPart("applicant_name", registerRequestModel.applicant_name ?: "")
            .addFormDataPart("email", registerRequestModel.email ?: "")
            .addFormDataPart("father_name", registerRequestModel.father_name!!)
            .addFormDataPart("gender",  registerRequestModel.gender ?:"")
            .addFormDataPart("dob", registerRequestModel.dob?:"")
            .addFormDataPart("aadhaar_number", registerRequestModel.aadhaar_number!!)
            .addFormDataPart("pan_number", registerRequestModel.pan_number!!)
            .addFormDataPart("mobile", registerRequestModel.mobile!!)
            .addFormDataPart("qualification", registerRequestModel.qualification?:"")
            .addFormDataPart("worker_type", registerRequestModel.worker_type?:"")
            .addFormDataPart("working_type","NON DRIVER")
            .addFormDataPart("working_hours", registerRequestModel.working_hours!!)
            .addFormDataPart("house_flat_no", registerRequestModel.house_flat_no!!)
            .addFormDataPart("street_name", registerRequestModel.street_name!!)
            .addFormDataPart("colony_area", registerRequestModel.colony_area!!)
            .addFormDataPart("landmark", registerRequestModel.landmark!!)
            .addFormDataPart("pincode", registerRequestModel.pincode!!)
            .addFormDataPart("state", registerRequestModel.state!!)
            .addFormDataPart("district", registerRequestModel.district!!)
            .addFormDataPart("is_address_same", registerRequestModel.is_address_same?:"")
            .addFormDataPart("temp_house_no", registerRequestModel.temp_house_no!!)
            .addFormDataPart("temp_street_name", registerRequestModel.temp_street_name!!)
            .addFormDataPart("temp_colony_area", registerRequestModel.temp_colony_area ?:"")
            .addFormDataPart("temp_landmark", registerRequestModel.temp_landmark?:"")
            .addFormDataPart("temp_pincode", registerRequestModel.temp_pincode?:"")
            .addFormDataPart("temp_state", "36")
            .addFormDataPart("temp_district", registerRequestModel?.temp_district?:"")
            .addFormDataPart("account_no", registerRequestModel?.account_no?:"")
            .addFormDataPart("ifsc_code", registerRequestModel.ifsc_code!!)
            .addFormDataPart("branch_name", registerRequestModel.branch_name ?:"")
            .addFormDataPart("nominee_name", registerRequestModel.nominee_name ?:"")
            .addFormDataPart("nominee_age", registerRequestModel?.nominee_age?:"")
            .addFormDataPart("nominee_relation", registerRequestModel?.nominee_relation?:"")
            .addFormDataPart("nominee_account_no", registerRequestModel?.nominee_account_no?:"")
            .addFormDataPart("nominee_ifsc_code", registerRequestModel.nominee_ifsc_code!!)
            .addFormDataPart("nominee_branch", registerRequestModel?.nominee_branch?:"")
            .addFormDataPart("doj", registerRequestModel?.doj ?:"")
            .addFormDataPart("office_address", registerRequestModel.office_address!!)
            .addFormDataPart("primary_working_id", registerRequestModel.primary_working_id!!)
            .addFormDataPart("vehicle", registerRequestModel.vehicle!!)
            .addFormDataPart("service", registerRequestModel.service!!)
            .addFormDataPart("other_qualification", registerRequestModel.other_qualification!!)
            .addFormDataPart("primary_company", registerRequestModel.primary_company!!)
            .build()
    }


/* private fun buildPostman(registerRequestModel: RegisterRequestModel): MultipartBody {
  val mediaType = "text/plain".toMediaType()
return MultipartBody.Builder().setType(MultipartBody.FORM)
      .addFormDataPart(
          name = "profile_image",
          filename = "passphoto.jpg",
          body = registerRequestModel.profile_image!!.asRequestBody("application/octet-stream".toMediaType()))
      .addFormDataPart("applicant_name","Sunil")
      .addFormDataPart("email","testborra@gmail.com")
      .addFormDataPart("father_name","Sree Sree")
      .addFormDataPart("gender","Male")
      .addFormDataPart("dob","2004-06-23")
      .addFormDataPart("aadhaar_number","491712341239")
      .addFormDataPart("pan_number","DCBAE1234B")
      .addFormDataPart("mobile","9879879870")
      .addFormDataPart("qualification","6")
      .addFormDataPart("worker_type","Non Driver")
      .addFormDataPart("working_type","NON DRIVER")
      .addFormDataPart("working_hours","12")
      .addFormDataPart("house_flat_no","H.No 1234")
      .addFormDataPart("street_name","Teachers Colony")
      .addFormDataPart("colony_area","RTC Colony")
      .addFormDataPart("landmark","Reliance")
      .addFormDataPart("pincode","500015")
      .addFormDataPart("state","36")
      .addFormDataPart("district","536")
      .addFormDataPart("is_address_same","1")
      .addFormDataPart("temp_house_no","HNO 123")
      .addFormDataPart("temp_street_name","james Street")
      .addFormDataPart("temp_colony_area","Colony")
      .addFormDataPart("temp_landmark","landmark")
      .addFormDataPart("temp_pincode","500015")
      .addFormDataPart("temp_state","36")
      .addFormDataPart("temp_district","536")
      .addFormDataPart("account_no","1234567890")
      .addFormDataPart("ifsc_code","SBIN0021422")
      .addFormDataPart("branch_name","JUPITER COLONY")
      .addFormDataPart("nominee_name","SREE SREE")
      .addFormDataPart("nominee_age","56")
      .addFormDataPart("nominee_relation","Father")
      .addFormDataPart("nominee_account_no","1234567")
      .addFormDataPart("nominee_ifsc_code","SBI0021422")
      .addFormDataPart("nominee_branch","SBI")
      .addFormDataPart("primary_company","1")
      .addFormDataPart("doj","2008-06-12")
      .addFormDataPart("office_address","One West")
      .addFormDataPart("primary_working_id","1234567890")
      .addFormDataPart("vehicle","1")
      .addFormDataPart("service","1")
      .addFormDataPart("other_qualification","other education")
      .build()
  /*val mediaType = "text/plain".toMediaType()
  return MultipartBody.Builder().setType(MultipartBody.FORM)
      .addFormDataPart("profile_image","Demo",
          registerRequestModel.profile_image?.asRequestBody("application/octet-stream".toMediaType())!!)
      .addFormDataPart("applicant_name","S")
      .addFormDataPart("email","testtest@gmail.com")
      .addFormDataPart("father_name","Sree Sree")
      .addFormDataPart("gender","Male")
      .addFormDataPart("dob","2004-06-23")
      .addFormDataPart("aadhaar_number","133445341239")
      .addFormDataPart("pan_number","ABCDK1234B")
      .addFormDataPart("mobile","9490004399")
      .addFormDataPart("qualification","6")
      .addFormDataPart("worker_type","Non Driver")
      .addFormDataPart("working_type","NON DRIVER")
      .addFormDataPart("working_hours","12")
      .addFormDataPart("house_flat_no","H.No 1234")
      .addFormDataPart("street_name","Teachers Colony")
      .addFormDataPart("colony_area","RTC Colony")
      .addFormDataPart("landmark","Reliance")
      .addFormDataPart("pincode","500015")
      .addFormDataPart("state","36")
      .addFormDataPart("district","536")
      .addFormDataPart("is_address_same","1")
      .addFormDataPart("temp_house_no","HNO 123")
      .addFormDataPart("temp_street_name","james Street")
      .addFormDataPart("temp_colony_area","Colony")
      .addFormDataPart("temp_landmark","landmark")
      .addFormDataPart("temp_pincode","500015")
      .addFormDataPart("temp_state","36")
      .addFormDataPart("temp_district","536")
      .addFormDataPart("account_no","1234567890")
      .addFormDataPart("ifsc_code","SBIN0021422")
      .addFormDataPart("branch_name","JUPITER COLONY")
      .addFormDataPart("nominee_name","SREE SREE")
      .addFormDataPart("nominee_age","56")
      .addFormDataPart("nominee_relation","Father")
      .addFormDataPart("nominee_account_no","1234567")
      .addFormDataPart("nominee_ifsc_code","SBI0021422")
      .addFormDataPart("nominee_branch","SBI")
      .addFormDataPart("primary_company","1")
      .addFormDataPart("doj","2008-06-12")
      .addFormDataPart("office_address","One West")
      .addFormDataPart("primary_working_id","1234567890")
      .addFormDataPart("vehicle","1")
      .addFormDataPart("service","1")
      .addFormDataPart("other_qualification","other education")
      .build()*/
}*/


}