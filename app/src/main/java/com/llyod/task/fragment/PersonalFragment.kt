package com.llyod.task.fragment

import android.Manifest.permission.READ_MEDIA_IMAGES
import android.Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity.RESULT_OK
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.llyod.domain.model.form.Detail
import com.llyod.domain.model.form.QualificationResponse
import com.llyod.domain.model.form.ServiceResponse
import com.llyod.task.R
import com.llyod.task.R.*
import com.llyod.task.common.Utils
import com.llyod.task.common.Utils.convertBitmapToFile
import com.llyod.task.common.Utils.loadBitmap
import com.llyod.task.common.Utils.rotateBitmap
import com.llyod.task.databinding.FragmentPersonalBinding
import com.llyod.task.viewmodel.SharedViewModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.concurrent.Executors


class PersonalFragment : Fragment() {


    private var qualificationType: Int? = 0
    private var servieType: Int? = 0
    private lateinit var leftImageFile: File
    private var _binding: FragmentPersonalBinding? = null
    private val binding get() = _binding!!


    private val sharedViewModel: SharedViewModel by activityViewModels()
    var image_uri: Uri? = null
    var typeOfWorker = ""
    var vehicleType = ""
    var gender = ""


    private var galleryActivityResultLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(), ActivityResultCallback {
            if (it.resultCode === RESULT_OK) {
                image_uri = it.data?.data
                val inputImage = Utils.uriToBitmap(requireContext(),image_uri!!)
                val rotated = rotateBitmap(requireContext(), image_uri!!,inputImage!!)
                val compressed  = Utils.getResizedBitmap(rotated,512)
                leftImageFile = convertBitmapToFile(requireContext(),"gigworker", compressed)
                binding.imageView.setImageBitmap(rotated)
            }
        }
    )


    private var cameraActivityResultLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode === RESULT_OK) {
            val inputImage = Utils.uriToBitmap(requireContext(),image_uri!!)
            val rotated = rotateBitmap(requireContext(), image_uri!!,inputImage!!)
            val compressed  = Utils.getResizedBitmap(rotated,512)
            leftImageFile = convertBitmapToFile(requireContext(),"gigworker", compressed)
            binding.imageView.setImageBitmap(compressed)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPersonalBinding.inflate(inflater, container, false)
        return binding.root
    }
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedViewModel.errorMessages.observe(viewLifecycleOwner,::onErrorMessage)


        sharedViewModel.personalDetailsModel?.let {
            try {
                leftImageFile = it.profile_photo?.let { it1 -> File(it1) }!!
                Glide.with(this)
                    .load(it.profile_photo)
                    .into(_binding!!.imageView)
            }catch (e : Exception){

            }
            try {
                it?.photo?.let {
                    _binding?.imageView?.setImageURI(Uri.fromFile(it))
                }
            }catch (e: Exception) {
                e.printStackTrace()
            }

            /* it?.photo?.let {
                 _binding?.imageView?.setImageURI(Uri.fromFile(it))
             }*/
            _binding!!.workerFullNameEditText.setText(it.name)
            _binding!!.dobEditText.setText(it.dob)
            _binding!!.aadharNumberEditText.setText(it.aadhar)
            _binding!!.mobileNumberEditText.setText(it.mobile)
            _binding!!.panNumberEditText.setText(it.pan)
            _binding!!.emailEditText.setText(it.email)
            it.otherQualification?.let { qualification ->
                if(qualification.isNotEmpty()) {
                    _binding?.qualificationEditText?.visibility = View.VISIBLE
                }
                _binding?.qualificationEditText?.setText(qualification)
            }



            try {
                selectSpinnerItem(_binding?.spinnerQualification!!, it.qualification?.toInt() ?: 0)
            }catch (e :Exception){
                it.qualification?.let { it1 ->
                    selectSpinnerItemByValue(_binding?.spinnerQualification!!,
                        it1
                    )
                }
            }
            try {
                selectSpinnerItem(_binding?.typeOfService!!, it.service?.toInt() ?: 0)
            } catch (e : Exception){
                it.service?.let { it1 ->
                    selectSpinnerItemByValue(_binding?.typeOfService!!,
                        it1
                    )
                }
            }
//            binding!!.genderRadioGroup.check(it1)
//            _binding?.workingTypeEditText?.setText(it.typeOfWork)
//            _binding?.vehicleTypeEditText?.setText(it.vehicleType)
            _binding?.workingTypeEditText?.setText(it.workingType)
            _binding?.workingDaysEditText?.setText(it.noOfHours)
            _binding?.fatherEditText?.setText(it.fatherName)
        }

        _binding?.imageView?.setOnClickListener {
            if (
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                (
                        ContextCompat.checkSelfPermission(requireContext(), READ_MEDIA_IMAGES) == PERMISSION_GRANTED)
            ) {
                // Full access on Android 13 (API level 33) or higher
                showCameraGalleryDialog(requireContext())
            }
            if (
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE &&
                ContextCompat.checkSelfPermission(requireContext(), READ_MEDIA_VISUAL_USER_SELECTED) == PERMISSION_GRANTED
            ) {
                showCameraGalleryDialog(requireContext())
            }
            if (requireContext().checkSelfPermission(android.Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_DENIED ||
                requireContext().checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_DENIED
//                ||
//                requireContext().checkSelfPermission(Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_DENIED ||
//                requireContext().checkSelfPermission(Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED) == PackageManager.PERMISSION_DENIED
            ) {
                val permission = arrayOf<String>(
                    android.Manifest.permission.CAMERA,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                    Manifest.permission.READ_MEDIA_IMAGES,
//                    Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED
                )
                requestPermissions(permission, 112)
            } else {
                showCameraGalleryDialog(requireContext())
            }
        }
        _binding?.btnNext?.setOnClickListener {
            if (validateFields()){
                navigateToDetails()
            }
        }
        _binding?.dobEditText?.setOnClickListener {
            showDatePicker()
        }
        _binding!!.genderRadioGroup.setOnCheckedChangeListener { radioGroup, id ->
            when (id) {
                R.id.genderradioButton1 -> {
                    val radio: RadioButton = radioGroup.findViewById(id)
                    gender = radio.text.toString()
                }
                R.id.genderradioButton2 -> {
                    val radio: RadioButton = radioGroup.findViewById(id)
                    gender = radio.text.toString()
                }
                R.id.genderradioButton3 -> {
                    val radio: RadioButton = radioGroup.findViewById(id)
                    gender = radio.text.toString()
                }

                else -> {
                }
            }
        }

        _binding!!.typeOfWorker.setOnCheckedChangeListener { radioGroup, id ->
            when (id) {
                R.id.radioButton1 -> {
                    _binding!!.vehicleTyperadioGroup.visibility = View.VISIBLE
                    _binding!!.workingTypeTextInputLayout.visibility = View.GONE
                    val radio: RadioButton = radioGroup.findViewById(id)
                    typeOfWorker = radio.text.toString()
                }
                R.id.radioButton2 -> {
                    _binding!!.vehicleTyperadioGroup.visibility = View.GONE
                    _binding!!.workingTypeTextInputLayout.visibility = View.VISIBLE
                    val radio: RadioButton = radioGroup.findViewById(id)
                    typeOfWorker = radio.text.toString()
                }

                else -> {
                }
            }
        }
        _binding!!.vehicleTyperadioGroup.setOnCheckedChangeListener { radioGroup, id ->
            when (id) {
                R.id.vehicelTyperadioButton1 -> {
                    val radio: RadioButton = radioGroup.findViewById(id)
                    vehicleType = "1"
                }

                R.id.vehicelTyperadioButton2 -> {
                    vehicleType = "2"

                }
                R.id.vehicelTyperadioButton3 -> {
                    vehicleType = "3"

                }

                else -> {
                }
            }
        }

        sharedViewModel.getQualifications()
        sharedViewModel.getServiceType()
        sharedViewModel.getRegisterUser(requireContext())


        sharedViewModel.loading.observe(viewLifecycleOwner,::showLoader)
        sharedViewModel.qualificationLiveData.observe(viewLifecycleOwner,::qualificationData)
        sharedViewModel.servicesLiveData.observe(viewLifecycleOwner,::serviceTypeData)
        _binding?.mobileNumberEditText?.setText(sharedViewModel.getUserMobileNumberFromPref())

        _binding!!.workingDaysEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                try {
                    val input = s.toString().toInt()
                    if (input == 0) {
                        _binding!!.workingDaysEditText.setText("") // Set to minimum value
                        _binding!!.workingDaysEditText.text?.length?.let {
                            _binding!!.workingDaysEditText.setSelection(
                                it
                            )

                        }
                    } else if (input > 23) {
                        _binding!!.workingDaysEditText.setText("23") // Set to maximum value
                        _binding!!.workingDaysEditText.text?.length?.let {
                            _binding!!.workingDaysEditText.setSelection(
                                it
                            )
                        } // Move cursor to end
                    }
                } catch (e: NumberFormatException) {
                    // Handle non-numeric input if needed
                }
            }
        })

        _binding?.progressLayout?.bar1?.setBackgroundColor(requireContext().resources.getColor(color.purple_indicator))
        _binding?.progressLayout?.bar2?.setBackgroundColor(requireContext().resources.getColor(color.disable_indicator))
        _binding?.progressLayout?.bar3?.setBackgroundColor(requireContext().resources.getColor(color.disable_indicator))
        _binding?.progressLayout?.bar4?.setBackgroundColor(requireContext().resources.getColor(color.disable_indicator))

        _binding?.progressLayout?.framelayout1?.background  = ResourcesCompat.getDrawable(resources, drawable.circle_purple, null)
        _binding?.progressLayout?.framelayout2?.background =  ResourcesCompat.getDrawable(resources, drawable.circle_grey, null)
        _binding?.progressLayout?.framelayout3?.background =  ResourcesCompat.getDrawable(resources, drawable.circle_grey, null)
        _binding?.progressLayout?.framelayout4?.background =  ResourcesCompat.getDrawable(resources, drawable.circle_grey, null)

        _binding?.progressLayout?.framelayout2?.setOnClickListener {
            sharedViewModel.navigate(SharedViewModel.Navigator.ADDRESS)
        }
        _binding?.progressLayout?.framelayout3?.setOnClickListener {
            sharedViewModel.navigate(SharedViewModel.Navigator.BANK)
        }
        _binding?.progressLayout?.framelayout4?.setOnClickListener {
            sharedViewModel.navigate(SharedViewModel.Navigator.COMPANY)
        }

//        sharedViewModel.isNavigateLiveData.observe(viewLifecycleOwner,::navigate)
    }
    private fun onErrorMessage(errorMessage: String?) {
        errorMessage?.let {
            Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
        }
    }
    private fun navigate(navigator: SharedViewModel.Navigator?) {
        when(navigator){
            SharedViewModel.Navigator.ADDRESS -> {
                findNavController().navigate(R.id.action_personalFragment_to_addressFragment)
            }
            SharedViewModel.Navigator.BANK -> {
//                findNavController().navigate(R.id.action_personalFragment_to_bankFragment)
            }
            SharedViewModel.Navigator.COMPANY -> {
//                findNavController().navigate(R.id.action_personalFragment_to_companyFragment)
            }
            SharedViewModel.Navigator.PERSONAL -> {
//                findNavController().navigate(R.id.action_personalFragment_to_personalFragment)
            }
            null -> {
            }
        }
    }

    private fun isRegisteredLiveData(isRegisteredUser: Boolean?) {
        if (isRegisteredUser == true){
            sharedViewModel.personalDetailsModel?.let {
                _binding?.progressLayout?.bar1?.setBackgroundColor(requireContext().resources.getColor(
                    color.purple_indicator))
                _binding?.progressLayout?.bar2?.setBackgroundColor(requireContext().resources.getColor(
                    color.purple_indicator))
                _binding?.progressLayout?.bar3?.setBackgroundColor(requireContext().resources.getColor(
                    color.purple_indicator))
                _binding?.progressLayout?.bar4?.setBackgroundColor(requireContext().resources.getColor(
                    color.purple_indicator))
                _binding?.progressLayout?.bar5?.setBackgroundColor(requireContext().resources.getColor (
                    color.purple_indicator))

                _binding?.progressLayout?.framelayout1?.background  = ResourcesCompat.getDrawable(resources, drawable.circle_purple, null)
                _binding?.progressLayout?.framelayout2?.background =  ResourcesCompat.getDrawable(resources, drawable.circle_purple, null)
                _binding?.progressLayout?.framelayout3?.background =  ResourcesCompat.getDrawable(resources, drawable.circle_purple, null)
                _binding?.progressLayout?.framelayout4?.background =  ResourcesCompat.getDrawable(resources, drawable.circle_purple, null)

                /* it?.photo?.let {
                     _binding?.imageView?.setImageURI(Uri.fromFile(it))
                 }*/
                try {
                    Glide.with(this)
                        .load(it.profile_photo)
                        .into(_binding!!.imageView)
// Declaring and initializing an Executor and a Handler
                    val myExecutor = Executors.newSingleThreadExecutor()
                    val myHandler = Handler(Looper.getMainLooper())
                    myExecutor.execute {
                        try {
                            it.profile_photo?.let {
                                val bitmap = loadBitmap(it)
                                leftImageFile =
                                    bitmap?.let { it1 ->
                                        convertBitmapToFile(requireContext(),"gigworker",
                                            it1
                                        )
                                    }!!
                            }
                        }catch (exception : Exception) {

                        }


                    }

                }catch (e : Exception){

                }
                _binding!!.workerFullNameEditText.setText(it.name)
                _binding!!.dobEditText.setText(it.dob)
                _binding!!.aadharNumberEditText.setText(it.aadhar)
                _binding!!.mobileNumberEditText.setText(it.mobile)
                _binding!!.panNumberEditText.setText(it.pan)
                _binding!!.emailEditText.setText(it.email)
                _binding?.workingTypeEditText?.setText(it.typeOfWork)
                _binding?.qualificationEditText?.setText(it.otherQualification)

                disableView(_binding!!.aadharNumberEditText)
                disableView(_binding!!.panNumberEditText)
                disableView(_binding!!.emailEditText)
                disableView(_binding!!.dobEditText)
                disableView(_binding!!.workerFullNameEditText)

                if(it.typeOfWork.equals("Driver", ignoreCase = true)) {
                    _binding?.typeOfWorker?.check(R.id.radioButton1);
                }else {
                    _binding?.typeOfWorker?.check(R.id.radioButton2);
                    _binding?.workingTypeEditText?.setText(it.workingType)
                }

                if(it.gender.equals("Male", ignoreCase = true)) {
                    _binding?.genderRadioGroup?.check(R.id.genderradioButton1);
                }else if (it.gender.equals("Female", ignoreCase = true)){
                    _binding?.genderRadioGroup?.check(R.id.genderradioButton2);
                } else {
                    _binding?.genderRadioGroup?.check(R.id.genderradioButton3);
                }

                if(it.vehicleType.equals("Two Wheeler", ignoreCase = true)) {
                    _binding?.vehicleTyperadioGroup?.check(R.id.vehicelTyperadioButton1);
                }else if(it.vehicleType.equals("Three Wheeler", ignoreCase = true)) {
                    _binding?.vehicleTyperadioGroup?.check(R.id.vehicelTyperadioButton2);
                }else {
                    _binding?.vehicleTyperadioGroup?.check(R.id.vehicelTyperadioButton3);
                }

                _binding?.workingDaysEditText?.setText(it.noOfHours)
                _binding?.fatherEditText?.setText(it.fatherName)
                it.qualification?.let { it1 ->
                    selectSpinnerItemByValue(binding.spinnerQualification,
                        it1
                    )
                }
                it.service?.let { service ->
                    selectSpinnerItemByValue(binding.typeOfService,
                        service
                    )
                    /*sharedViewModel.getServiceResponse()?.details?.let { details ->

                        val detail =  details.filter { it.id.toString() == service }
                        Log.e("SERVICE",service)
                        if (detail.isNotEmpty()) {
                            selectSpinnerItemByValueByList(binding.typeOfService,
                                service,
                                details

                            )
                        } else {
                            val detail1 =  details.filter { it.name == service }
                            if (detail1.isNotEmpty()) {
                                selectSpinnerItemByValueByList(binding.typeOfService,
                                    service,
                                    detail1
                                )
                            }

                        }

                    }*/
                }
            }
        }
    }
    private fun selectSpinnerItemByValueByList(spnr: Spinner, value: String?, details: List<Detail>) {
        try {
            val adapter = spnr.adapter
            for (position in 0 until adapter.count) {
                if (adapter.getItem(position).toString().equals(details.firstOrNull()?.name,ignoreCase = true)) {
                    spnr.setSelection(position)
                    return
                }
            }
        }catch (exception : Exception){

        }

    }

    private fun selectSpinnerItemByValueByList2(spnr: Spinner, value: String?, details: List<Detail>) {
        try {
            val adapter = spnr.adapter
            for (position in 0 until adapter.count) {
                if (adapter.getItem(position).toString().equals(details.firstOrNull()?.name,ignoreCase = true)) {
                    spnr.setSelection(position)
                    return
                }
            }
        }catch (exception : Exception){

        }

    }
    private fun selectSpinnerItemByValue(spnr: Spinner, value: String) {
        try {
            val adapter = spnr.adapter
            for (position in 0 until adapter.count) {
                if (adapter.getItem(position).toString().equals(value,ignoreCase = true)) {
                    spnr.setSelection(position)
                    return
                }
            }
        }catch (exception : Exception){

        }

    }
    private fun selectSpinnerItem(spnr: Spinner, value: Int) {
        try {
            val adapter = spnr.adapter
            for (position in 0 until adapter.count) {
                if (position == value) {
                    spnr.setSelection(position)
                    return
                }
            }
        }catch (exception : Exception){

        }

    }


    private fun showLoader(showLoader: Boolean?) {
        if (showLoader == true){
            _binding?.progressBar?.visibility = View.VISIBLE
        }else{
            _binding?.progressBar?.visibility = View.GONE
        }
    }

    /**
     * vaidate fields
     */

    private fun validateFields(): Boolean {
        val name = _binding!!.workerFullNameEditText.text.toString()
        val dob = _binding!!.dobEditText.text.toString()
        val aadhar = _binding!!.aadharNumberEditText.text.toString()
        val mobile = _binding!!.mobileNumberEditText.text.toString()
        val gender = gender
        val pan = _binding!!.panNumberEditText.text.toString()
        val email = _binding!!.emailEditText.text.toString()
        val qualification = qualificationType
        val serviceType = servieType
        val  typeOfWork = typeOfWorker
        val vehicleType = vehicleType
        val  noOfHours = _binding?.workingDaysEditText?.text.toString().trim()
        val fatherName = _binding?.fatherEditText?.text.toString()
        val otherQualification = _binding?.qualificationEditText?.text.toString()

        if (name.isEmpty()){
            Toast.makeText(requireContext(), "Please enter name", Toast.LENGTH_SHORT).show()
            return false
        }

        if (gender.isEmpty()){
            Toast.makeText(requireContext(), "Please select gender", Toast.LENGTH_SHORT).show()
            return false
        }
        if (dob.isEmpty()){
            Toast.makeText(requireContext(), "Please enter dob", Toast.LENGTH_SHORT).show()
            return false
        }
        if(aadhar.isEmpty()){
            Toast.makeText(requireContext(), "Please enter aadhaar number", Toast.LENGTH_SHORT).show()
            return false
        }
        if(aadhar.length < 12){
            Toast.makeText(requireContext(), "Please enter valid aadhaar number", Toast.LENGTH_SHORT).show()
            return false
        }
        if (!isValidEmail(email)){
            Toast.makeText(requireContext(), "Invalid Email", Toast.LENGTH_SHORT).show()
            return false
        }
        if (qualification == 0){
            Toast.makeText(requireContext(), "Please select qualification", Toast.LENGTH_SHORT).show()
            return false
        }
        if (binding.spinnerQualification.selectedItemPosition == 6){
            if (otherQualification.isEmpty()) {
                Toast.makeText(requireContext(), "Please enter Other qualification", Toast.LENGTH_SHORT).show()
                return false
            }
        }

        if (serviceType == 0){
            Toast.makeText(requireContext(), "Please select service type", Toast.LENGTH_SHORT).show()
            return false
        }
        if (typeOfWork == "Driver") {
            if (vehicleType.isEmpty()){
                Toast.makeText(requireContext(), "Please select vehicle type", Toast.LENGTH_SHORT).show()
                return false
            }
        } else if (typeOfWork == "Non Driver") {
            if (_binding?.workingTypeEditText?.text?.toString()?.isEmpty() == true){
                Toast.makeText(requireContext(), "Please select provide details", Toast.LENGTH_SHORT).show()
                return false
            }
        } else if (typeOfWork.isEmpty()) {
            Toast.makeText(requireContext(), "Please select Type of Worker", Toast.LENGTH_SHORT).show()
            return false
        }

        if (mobile.isEmpty()){
            Toast.makeText(requireContext(), "Please enter mobile number", Toast.LENGTH_SHORT).show()
            return false
        }
        if (mobile.length<=9) {
            Toast.makeText(requireContext(), "Mobile number should be 10 ", Toast.LENGTH_SHORT).show()
            return false
        }
        if (pan.isEmpty()){
            Toast.makeText(requireContext(), "Please enter PAN number", Toast.LENGTH_SHORT).show()
            return false
        }
        if (!isValidPanNumber(pan)){
            Toast.makeText(requireContext(), "Please enter valid PAN number", Toast.LENGTH_SHORT).show()
            return false
        }
        if (fatherName.isEmpty()){
            Toast.makeText(requireContext(), "Please enter father name", Toast.LENGTH_SHORT).show()
            return false
        }

        if (noOfHours.isEmpty() || noOfHours.toInt() > 23 ){
            Toast.makeText(requireContext(), "Please enter working hours between 1 to 23", Toast.LENGTH_SHORT).show()
            return false
        }
        if (::leftImageFile.isInitialized) {
            return true
        } else {
            Toast.makeText(requireContext(), "Please upload photo", Toast.LENGTH_SHORT).show()
            return false
        }


    }
    private fun isValidPanNumber(panNumber: String): Boolean {
        val regex = Regex("[A-Z]{5}[0-9]{4}[A-Z]")
        return regex.matches(panNumber)
    }
    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun serviceTypeData(response: ServiceResponse?) {
        val details = response?.details?.map { it.name }

        val toMutableList = details!!.toMutableList()
        toMutableList.add(0,"Type of Service")
        val adapter: ArrayAdapter<String> =
            ArrayAdapter<String>(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                toMutableList
            )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        _binding?.typeOfService?.adapter = adapter
        _binding!!.typeOfService.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view:View?, position: Int, id: Long) {

                val selectedItem = parent?.getItemAtPosition(position)
                if (selectedItem?.equals("Type of Service") == true) {
                    servieType = 0
//                    Toast.makeText(requireContext(), "Please select a valid Service", Toast.LENGTH_SHORT).show();
                } else {
                    servieType =
                        response?.details?.filter { it.name == selectedItem }?.firstOrNull()?.id
                }
                // Do something with the selected item
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do something when nothing is selected
            }}

        sharedViewModel.personalDetailsModel?.let { personal ->
            val serviceDetails = response.details.filter { it.name == personal.service }
            if (serviceDetails.isNotEmpty()) {
                personal.service?.let { it1 ->
                    selectSpinnerItemByValueByList(binding.typeOfService,
                        it1,serviceDetails
                    )
                }
            } else {
                val serviceDetailss = response.details.filter { it.id.toString() == personal.service }
                personal.service?.let { it1 ->
                    selectSpinnerItemByValueByList(binding.typeOfService,
                        it1,serviceDetailss
                    )
                }
            }

        }
        sharedViewModel.isRegisteredLiveData.observe(viewLifecycleOwner,::isRegisteredLiveData)

    }

    private fun qualificationData(response: QualificationResponse?) {
        val details = response?.details?.map { it.name }

        val toMutableList = details!!.toMutableList()
        toMutableList.add(0,"Select Qualification")


        val adapter: ArrayAdapter<String> =
            ArrayAdapter<String>(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                toMutableList!!.toMutableList()
            )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        _binding?.spinnerQualification?.adapter = adapter

        _binding!!.spinnerQualification.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view:View?, position: Int, id: Long) {
                val selectedItem = parent?.getItemAtPosition(position)
                if (selectedItem?.equals("Select Qualification") == true) {
                    qualificationType = 0
                    binding.otherQualificationTextInputLayout.visibility = View.GONE
                } else if (selectedItem?.equals("Other") == true){
                    qualificationType =
                        response?.details?.filter { it.name == selectedItem }?.firstOrNull()?.id!!
                    binding.otherQualificationTextInputLayout.visibility = View.VISIBLE
                }else {
                    binding.otherQualificationTextInputLayout.visibility = View.GONE
                    qualificationType =
                        response?.details?.filter { it.name == selectedItem }?.firstOrNull()?.id!!
                }
                // Do something with the selected item
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do something when nothing is selected
            }}


        sharedViewModel.personalDetailsModel?.let { personal ->
            val qualificationDetails = response.details.filter { it.name == personal.qualification }
            if (qualificationDetails.isNotEmpty()) {
                personal.qualification?.let { it1 ->
                    selectSpinnerItemByValueByList(binding.spinnerQualification,
                        it1,qualificationDetails
                    )
                }
            } else {
                val qualificationDetails1 = response.details.filter { it.id.toString() == personal.qualification }
                personal.qualification?.let { it1 ->
                    selectSpinnerItemByValueByList(binding.spinnerQualification,
                        it1,qualificationDetails1
                    )
                }
            }

        }

    }




    fun showCameraGalleryDialog(context: Context) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Choose Image Source")
        builder.setItems(arrayOf("Camera", "Gallery")) { dialog, which ->
            when (which) {
                0 -> {
                    //Handle camera button click
                    openCamera()
                }
                1 -> {
                    // Handle gallery button click
                    openGallery()
                }
            }
        }
        builder.show()
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        galleryActivityResultLauncher.launch(intent)
    }

    private fun showDatePicker() {
        val cal = Calendar.getInstance()
        cal.add(Calendar.YEAR, -18)
        cal.add(Calendar.DATE, -1);

        val datePickerDialog = DatePickerDialog(
            requireContext(), {
                    DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, monthOfYear, dayOfMonth)
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val formattedDate = dateFormat.format(selectedDate.time)
                _binding!!.dobEditText.setText(formattedDate)
            },
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.datePicker.maxDate =   cal.timeInMillis
        datePickerDialog.show()
    }
    private fun openCamera() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "New Picture")
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera")
        image_uri = requireContext().contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri)
        cameraActivityResultLauncher.launch(cameraIntent)
    }

    private fun navigateToDetails() {
        sharedViewModel.savePersonalDetails(
            photo = leftImageFile,
            profile_photo = null,
            name = _binding!!.workerFullNameEditText.text.toString(),
            gender = gender,
            dob = _binding!!.dobEditText.text.toString(),
            aadhar = _binding!!.aadharNumberEditText.text.toString(),
            mobile = _binding!!.mobileNumberEditText.text.toString(),
            pan = _binding!!.panNumberEditText.text.toString(),
            email = _binding!!.emailEditText.text.toString(),
            qualification = qualificationType.toString(),
            typeOfWork = typeOfWorker,
            workingType = _binding?.workingTypeEditText?.text.toString(),
            vehicleType = vehicleType,
            noOfHours = _binding!!.workingDaysEditText.text.toString(),
            fatherName = _binding?.fatherEditText?.text.toString(),
            serviceType = servieType.toString(),
            otherQualification = _binding?.qualificationEditText?.text.toString() ?: ""
        )
        findNavController().navigate(R.id.action_personalFragment_to_addressFragment)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun disableView(view: EditText){
        view.isClickable = false
        view.isFocusable = false
        view.isFocusableInTouchMode = false
        view.setOnClickListener(null)
    }




}