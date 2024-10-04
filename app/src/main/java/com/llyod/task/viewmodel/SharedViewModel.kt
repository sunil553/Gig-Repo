package com.llyod.task.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.llyod.data.repository.UserPreferencesRepo
import com.llyod.domain.common.Result
import com.llyod.domain.model.form.CompanyReponse
import com.llyod.domain.model.form.DistrictsForStatesResponse
import com.llyod.domain.model.form.QualificationResponse
import com.llyod.domain.model.form.ServiceResponse
import com.llyod.domain.model.form.StatesModelResponse
import com.llyod.domain.model.form.VehicleTypeResponse
import com.llyod.domain.model.register.RegisterRequestModel
import com.llyod.domain.model.userregistration.RegisteredUserResponse
import com.llyod.domain.repository.LoginOtpValidationRepository
import com.llyod.task.model.AddressDetailsModel
import com.llyod.task.model.BankDetailsModel
import com.llyod.task.model.CompanyDetailsModel
import com.llyod.task.model.PersonalDetailsModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject


@HiltViewModel
class SharedViewModel @Inject constructor(
    private val userRepository: UserPreferencesRepo,
    private val configRepository: LoginOtpValidationRepository,
    private val userPreferencesRepo: UserPreferencesRepo
) : ViewModel() {


    private  var serviceResponse: ServiceResponse? = null
    var companyModel: CompanyDetailsModel? = null

    private val _statesMutableLiveData = MutableLiveData<StatesModelResponse>()
    val  statesLiveData : LiveData<StatesModelResponse> get() = _statesMutableLiveData

//    private val _statesTempMutableLiveData = MutableLiveData<StatesModelResponse>()
//    val  statesTempLiveData : LiveData<StatesModelResponse> get() = _statesTempMutableLiveData

    private val _companyMutableLiveData = MutableLiveData<CompanyReponse>()
    val  companyLiveData : LiveData<CompanyReponse> get() = _companyMutableLiveData

    private val _qualificationMutableLiveData = MutableLiveData<QualificationResponse>()
    val  qualificationLiveData  = _qualificationMutableLiveData

    private val _vehicleMutableLiveData = MutableLiveData<VehicleTypeResponse>()
    val  vehicleLiveData   get() = _vehicleMutableLiveData

    private val _districtMutableLiveData = MutableLiveData<DistrictsForStatesResponse>()
    val  districtLiveData get() = _districtMutableLiveData

    private val _districtTempMutableLiveData = MutableLiveData<DistrictsForStatesResponse>()
    val  districtTempLiveData get() = _districtTempMutableLiveData

    private val _servicesMutableLiveData = MutableLiveData<ServiceResponse>()
    val  servicesLiveData get() = _servicesMutableLiveData

    private val _registerSuccess = MutableLiveData<String>()
    val  registerSuccessLiveData get() = _registerSuccess

    private val _isRegisteredLiveData = MutableLiveData<Boolean>()
    val  isRegisteredLiveData get() = _isRegisteredLiveData

    private val _isRegisteredUserResponse = MutableLiveData<RegisteredUserResponse?>()
    val  isRegisteredUserResponse get() = _isRegisteredUserResponse

    var personalDetailsModel: PersonalDetailsModel? = null

    var addressDetailsModel: AddressDetailsModel? = null

    var bankDetailsModel: BankDetailsModel? = null

    var companyDetailsModel: CompanyDetailsModel? = null


    private val _loading = MutableLiveData<Boolean>()
    val  loading : LiveData<Boolean> get() = _loading

    lateinit var districtsForStatesResponse : DistrictsForStatesResponse
    lateinit var districtsTempForStatesResponse : DistrictsForStatesResponse


    private val _isNavigateLiveData = MutableLiveData<Navigator>()
    val  isNavigateLiveData get() = _isNavigateLiveData

    sealed class Navigator{
        object BANK : Navigator()
        object COMPANY : Navigator()
        object ADDRESS : Navigator()
        object PERSONAL : Navigator()
    }


    fun getServiceResponse() : ServiceResponse?{
        return serviceResponse
    }

    companion object{
        private const val MOBILE_NO = "MOBILE_NO"
        private const val REFRESH_TOKEN = "REFRESH_TOKEN"
        private const val ACCESS_TOKEN = "ACCESS_TOKEN"

    }


    fun savePersonalDetails(
        photo : File?,
        profile_photo : String?,
        name : String?,
        gender : String?,
        dob : String?,
        aadhar : String?,
        pan : String?,
        mobile : String?,
        email : String?,
        qualification : String?,
        typeOfWork : String?,
        workingType : String?,
        vehicleType : String?,
        noOfHours : String?,
        fatherName : String?,
        serviceType : String?,
    ){
        personalDetailsModel = PersonalDetailsModel(
            photo,
            name,
            gender,
            dob,
            aadhar,
            pan,
            mobile,
            email,
            qualification,
            typeOfWork,
            workingType,
            vehicleType,
            noOfHours,
            fatherName,
            serviceType,
            profile_photo
        )
    }

    fun saveBankDetails(
        self_account_no: String,
        self_ifsc_code: String,
        self_branch: String,
        nominee_name: String,
        nominee_relation: String,
        nominee_age: String,
        nominee_account_no: String,
        nominee_ifsc_code: String,
        nominee_branch: String
    ) {
        bankDetailsModel =  BankDetailsModel(
            account_no = self_account_no,
            ifsc_code = self_ifsc_code,
            bank_name = self_branch,
            branch = self_branch,
            nominee_account_no =  nominee_account_no,
            nominee_name = nominee_name,
            nominee_age =  nominee_age,
            nominee_ifsc_code =  nominee_ifsc_code,
            nominee_branch =  nominee_branch,
            nominee_relation =  nominee_relation
        )
    }

    fun saveCompanyDetails(  doj: String?,
                             primary_company: String?,
                             primary_working_id: String?,
                             office_address : String?){
        companyModel =  CompanyDetailsModel(comapany_name = primary_company,doj,primary_company,primary_working_id,office_address)

    }

    fun getUserMobileNumberFromPref()  = userPreferencesRepo.getString(MOBILE_NO,"")

    fun saveAddressDetails(
        house_no: String = "",
        street_name: String = "",
        colony_area: String = "",
        landmark: String = "",
        pincode: String = "",
        state: String = "",
        district: String = "",
        temp_house_no: String = "",
        temp_street_name: String = "",
        temp_colony_area: String = "",
        temp_landmark: String = "",
        temp_pincode: String = "",
        temp_state: String = "",
        temp_district: String = "",
        is_address_same : String = ""
    ) {
        addressDetailsModel = AddressDetailsModel(
            house_no,
            street_name,
            colony_area,
            landmark,
            pincode,
            state,
            district,
            temp_house_no,
            temp_street_name,
            temp_colony_area,
            temp_landmark,
            temp_pincode,
            temp_state,
            temp_district,
            is_address_same)

    }

    fun clearAllData() {
        userRepository.clearAllData()
    }

    fun getStates() {
        viewModelScope.launch {
            when(val response = configRepository.getStates()){
                is Result.Error -> {

                }
                is Result.Success -> {
                    response.data?.let {
                        _statesMutableLiveData.postValue(response.data)
                    }
                }
            }
        }
    }
    fun getDistrictsByStateId(stateId : String) {
        viewModelScope.launch {
            _loading.postValue(true)
            when(val response = configRepository.getDistricts(stateId)){
                is Result.Error -> {
                    _loading.postValue(false)
                }
                is Result.Success -> {
                    _loading.postValue(false)
                    districtsForStatesResponse = response.data!!
                    response.data?.let {
                        _districtMutableLiveData.postValue(response.data)
                    }
                }
            }
        }
    }
    fun getDistrictsByTempStateId() {
        viewModelScope.launch {
            _loading.postValue(true)
            when(val response = configRepository.getDistricts("36")){
                is Result.Error -> {
                    _loading.postValue(false)
                }
                is Result.Success -> {
                    _loading.postValue(false)
                    districtsTempForStatesResponse = response.data!!
                    response.data?.let {
                        _districtTempMutableLiveData.postValue(response.data)
                    }
                }
            }
        }
    }

    fun getServiceType() {
        viewModelScope.launch {
            _loading.postValue(true)
            when(val response = configRepository.getServiceTypes()){
                is Result.Error -> {
                    configRepository.getAccessToken(userPreferencesRepo.getString(REFRESH_TOKEN,""))
                    _loading.postValue(false)

                }
                is Result.Success -> {
                    _loading.postValue(false)
                    response.data?.let {
                        serviceResponse = response.data
                        _servicesMutableLiveData.postValue(response.data)
                    }
                }
            }
        }
    }

    fun getQualifications(){
        viewModelScope.launch {
            _loading.postValue(true)
            when(val response = configRepository.getQualificationTypes()){
                is Result.Error -> {
                    _loading.postValue(false)

                }
                is Result.Success -> {
                    _loading.postValue(false)
                    response.data?.let {
                        _qualificationMutableLiveData.postValue(response.data)
                    }
                }
            }
        }
    }

    fun getCompanyTypes(){
        viewModelScope.launch {
            _loading.postValue(true)
            when(val response = configRepository.getCompanyTypes()){

                is Result.Error -> {
                    _loading.postValue(false)

                }
                is Result.Success -> {
                    _loading.postValue(false)
                    response.data?.let {
                        _companyMutableLiveData.postValue(response.data)
                    }
                }
            }
        }
    }
    fun getVehicleTypes(){
        viewModelScope.launch {
            when(val response = configRepository.getVehicleTypes()){
                is Result.Error -> {

                }
                is Result.Success -> {
                    response.data?.let {
                        _vehicleMutableLiveData.postValue(response.data)
                    }
                }
            }
        }
    }

    fun getRegisteredUser(){
        viewModelScope.launch {
            _loading.postValue(true)
            when(val responseFromServer = configRepository.getRegisteredUser()){
                is Result.Error -> {
                    _loading.postValue(false)
                }
                is Result.Success -> {
                    responseFromServer.data
                }
            }
        }
    }
    fun getRegisterUser(requireContext: Context) {
        viewModelScope.launch {
            _loading.postValue(true)
            when(val responseFromServer = configRepository.getRegisteredUser()){
                is Result.Error -> {
                    _loading.postValue(false)
                    _isRegisteredUserResponse.postValue(null)
                }
                is Result.Success -> {
                    _loading.postValue(false)
                    responseFromServer.data?.let {
                        _isRegisteredLiveData.postValue(it.registered)
                        _isRegisteredUserResponse.postValue(it)
                        savePersonalDetails(
                            photo = null,
                            profile_photo = it.personal_details.profile_image,
                            name = it.personal_details.name,
                            gender = it.personal_details.gender,
                            dob = it.personal_details.dob,
                            aadhar = it.personal_details.aadhaar_number.replace(" ",""),
                            mobile = it.personal_details.mobile_number,
                            pan = it.personal_details.pan_number,
                            email = it.personal_details.email,
                            qualification = it.personal_details.qualification,
                            typeOfWork = it.personal_details.worker_type,
                            workingType = it.personal_details.working_type,
                            vehicleType = "1",
                            noOfHours = it.personal_details.working_hours,
                            fatherName = it.personal_details.father_name,
                            serviceType = "0"
                        )
                        saveAddressDetails(
                           it.address.permenant_address.house_no,
                            it.address.permenant_address.street_name,
                            it.address.permenant_address.colony_area,
                            it.address.permenant_address.landmark,
                            it.address.permenant_address.pincode,
                            state = it.address.permenant_address.state,
                            district = it.address.present_address.district,
                            it.address.present_address.house_no,
                            it.address.present_address.street_name,
                            it.address.present_address.colony_area,
                            it.address.present_address.landmark,
                            it.address.present_address.pincode,
                            temp_state = it.address.present_address.state,
                            it.address.present_address.district,
                            is_address_same = "0"
                        )
                        saveBankDetails(
                            self_account_no = it.bank_details.account_no,
                            self_ifsc_code = it.bank_details.ifsc_code,
                            self_branch = it.bank_details.branch_name,
                            nominee_name =it.nominee_details.name,
                            nominee_relation = it.nominee_details.relationship,
                            nominee_age = it.nominee_details.age,
                            nominee_account_no = it.nominee_details.account_no,
                            nominee_ifsc_code = it.nominee_details.ifsc_code,
                            nominee_branch = it.nominee_details.branch_name
                        )
                        if (it.company_details.isNotEmpty() ){
                            saveCompanyDetails(
                                it.company_details[0].date_of_joining,
                                it.company_details[0].comapany_name,
                                it.company_details[0].is_primary,
                                it.company_details[0].local_office_address
                            )
                        }

                    }
                }
            }
        }
    }
    fun registerUser(){
        viewModelScope.launch {
            _loading.postValue(true)
            val response = buildRegisterRequestModel(personalDetailsModel,addressDetailsModel,bankDetailsModel,companyModel)
            when(val responseFromServer = configRepository.registerUser(response)){
                is Result.Error -> {
                    _loading.postValue(false)
                    _registerSuccess.postValue("Error")
                }
                is Result.Success -> {
                    _loading.postValue(false)
                    _registerSuccess.postValue("Success")
                    responseFromServer.data?.let {
//                        _vehicleMutableLiveData.postValue(response.data)
                    }
                }
            }
        }
    }

    private fun buildRegisterRequestModel(
        personalDetailsModel: PersonalDetailsModel?,
        addressDetailsModel: AddressDetailsModel?,
        bankDetailsModel: BankDetailsModel?,
        companyModel: CompanyDetailsModel?
    ): RegisterRequestModel {

        return RegisterRequestModel(
            aadhaar_number = personalDetailsModel?.aadhar,
            applicant_name = personalDetailsModel?.name,
            gender = personalDetailsModel?.gender,
            dob = personalDetailsModel?.dob,
            email = personalDetailsModel?.email,
            father_name = personalDetailsModel?.fatherName,
            pan_number = personalDetailsModel?.pan,
            mobile = personalDetailsModel?.mobile,
            qualification = personalDetailsModel?.qualification,
            working_type = personalDetailsModel?.typeOfWork ?: "Driver",
            vehicle = personalDetailsModel?.vehicleType,
            working_hours = personalDetailsModel?.noOfHours,
            profile_image = personalDetailsModel?.photo,

            house_flat_no = addressDetailsModel?.house_no,
            street_name = addressDetailsModel?.street_name,
            colony_area = addressDetailsModel?.colony_area,
            landmark = addressDetailsModel?.landmark,
            pincode = addressDetailsModel?.pincode,
            state = addressDetailsModel?.state,
            district = addressDetailsModel?.district,
            temp_house_no = addressDetailsModel?.temp_house_no,
            temp_street_name = addressDetailsModel?.temp_street_name,
            temp_colony_area = addressDetailsModel?.temp_colony_area,
            temp_landmark = addressDetailsModel?.temp_landmark,
            temp_pincode = addressDetailsModel?.temp_pincode,
            temp_state = addressDetailsModel?.temp_state,
            temp_district = addressDetailsModel?.temp_district,
            is_address_same = addressDetailsModel?.is_address_same,


            account_no = bankDetailsModel?.account_no,
            ifsc_code = bankDetailsModel?.ifsc_code,
            nominee_name = bankDetailsModel?.nominee_name,
            nominee_relation = bankDetailsModel?.nominee_relation,
            nominee_age = bankDetailsModel?.nominee_age,
            nominee_account_no =  bankDetailsModel?.nominee_account_no,
            nominee_ifsc_code =  bankDetailsModel?.nominee_ifsc_code,
            nominee_branch =  bankDetailsModel?.nominee_branch,

            branch_name = bankDetailsModel?.branch,
            primary_working_id = companyModel?.primary_working_id,
            doj = companyModel?.doj,
            office_address = companyModel?.office_address,

            other_qualification = "Other Qualification",

            primary_company = companyModel?.primary_company,//int
            comapany_name = companyModel?.comapany_name,
            service = personalDetailsModel?.serviceType,//int
            worker_type = personalDetailsModel?.typeOfWork ?: "Non Driver" //driver
        )

    }

    fun navigate(navigator: Navigator) {
        _isNavigateLiveData.postValue(navigator)
    }


}