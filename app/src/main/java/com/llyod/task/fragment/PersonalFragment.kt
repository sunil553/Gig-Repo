package com.llyod.task.fragment

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity.RESULT_OK
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.llyod.domain.model.form.QualificationResponse
import com.llyod.domain.model.form.ServiceResponse
import com.llyod.task.R
import com.llyod.task.common.Utils
import com.llyod.task.common.Utils.rotateBitmap
import com.llyod.task.databinding.FragmentPersonalBinding
import com.llyod.task.viewmodel.SharedViewModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class PersonalFragment : Fragment() {


    private var qualificationType: Int = 0
    private var servieType: Int? = 1
    private lateinit var leftImageFile: File
    private var _binding: FragmentPersonalBinding? = null
    private val binding get() = _binding!!


    private val sharedViewModel: SharedViewModel by activityViewModels()
    var image_uri: Uri? = null
    private val calendar = Calendar.getInstance()
    var typeOfWorker = ""
    var vehicleType = ""
    var gender = ""


    private var galleryActivityResultLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(), ActivityResultCallback {
            if (it.resultCode === RESULT_OK) {
                image_uri = it.data?.data
                val inputImage = Utils.uriToBitmap(requireContext(),image_uri!!)
                val rotated = rotateBitmap(requireContext(), image_uri!!,inputImage!!)
                val compressed  = Utils.getResizedBitmap(rotated,200)
                leftImageFile = Utils.convertBitmapToFile(requireContext(),"gigworker", compressed)
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
            val compressed  = Utils.getResizedBitmap(rotated,200)
            leftImageFile = Utils.convertBitmapToFile(requireContext(),"gigworker", compressed)
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
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        sharedViewModel.personalDetailsModel?.let {
            it?.photo?.let {
                _binding?.imageView?.setImageURI(Uri.fromFile(it))
            }
            _binding!!.workerFullNameEditText.setText(it.name)
            _binding!!.dobEditText.setText(it.dob)
            _binding!!.aadharNumberEditText.setText(it.aadhar)
            _binding!!.mobileNumberEditText.setText(it.mobile)
            _binding!!.panNumberEditText.setText(it.pan)
            _binding!!.emailEditText.setText(it.email)
//            binding!!.genderRadioGroup.check(it1)
//            _binding?.workingTypeEditText?.setText(it.typeOfWork)
//            _binding?.vehicleTypeEditText?.setText(it.vehicleType)
            _binding?.workingTypeEditText?.setText(it.noOfHours)
            _binding?.fatherEditText?.setText(it.fatherName)
        }

        _binding?.imageView?.setOnClickListener {
            if (requireContext().checkSelfPermission(android.Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_DENIED ||
                requireContext().checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_DENIED
            ) {
                val permission = arrayOf<String>(
                    android.Manifest.permission.CAMERA,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
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
                com.llyod.task.R.id.genderradioButton1 -> {
                    val radio: RadioButton = radioGroup.findViewById(id)
                    gender = radio.text.toString()
                }
                com.llyod.task.R.id.genderradioButton2 -> {
                    val radio: RadioButton = radioGroup.findViewById(id)
                    gender = radio.text.toString()
                }
                com.llyod.task.R.id.genderradioButton3 -> {
                    val radio: RadioButton = radioGroup.findViewById(id)
                    gender = radio.text.toString()
                }

                else -> {
                }
            }
        }

        _binding!!.typeOfWorker.setOnCheckedChangeListener { radioGroup, id ->
            when (id) {
                com.llyod.task.R.id.radioButton1 -> {
                    _binding!!.vehicleTyperadioGroup.visibility = View.VISIBLE
                    _binding!!.workingTypeTextInputLayout.visibility = View.GONE
                    val radio: RadioButton = radioGroup.findViewById(id)
                    typeOfWorker = radio.text.toString()
                }
                com.llyod.task.R.id.radioButton2 -> {
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
                com.llyod.task.R.id.vehicelTyperadioButton1 -> {
                    val radio: RadioButton = radioGroup.findViewById(id)
                    vehicleType = "1"
                }

                com.llyod.task.R.id.vehicelTyperadioButton2 -> {
                    vehicleType = "2"

                }
                com.llyod.task.R.id.vehicelTyperadioButton3 -> {
                    vehicleType = "3"

                }

                else -> {
                }
            }
        }

        sharedViewModel.getQualifications()
        sharedViewModel.getServiceType()
        sharedViewModel.getRegisterUser()


        sharedViewModel.loading.observe(viewLifecycleOwner,::showLoader)
        sharedViewModel.qualificationLiveData.observe(viewLifecycleOwner,::qualificationData)
        sharedViewModel.servicesLiveData.observe(viewLifecycleOwner,::serviceTypeData)
        sharedViewModel.isRegisteredLiveData.observe(viewLifecycleOwner,::isRegisteredLiveData)
        _binding?.mobileNumberEditText?.setText(sharedViewModel.getUserMobileNumberFromPref())

    }

    private fun isRegisteredLiveData(isRegisteredUser: Boolean?) {
        if (isRegisteredUser == true){
            sharedViewModel.personalDetailsModel?.let {
                it?.photo?.let {
                    _binding?.imageView?.setImageURI(Uri.fromFile(it))
                }
                _binding!!.workerFullNameEditText.setText(it.name)
                _binding!!.dobEditText.setText(it.dob)
                _binding!!.aadharNumberEditText.setText(it.aadhar)
                _binding!!.mobileNumberEditText.setText(it.mobile)
                _binding!!.panNumberEditText.setText(it.pan)
                _binding!!.emailEditText.setText(it.email)
                _binding?.workingTypeEditText?.setText(it.typeOfWork)

                disableView(_binding!!.aadharNumberEditText)
                disableView(_binding!!.panNumberEditText)
                disableView(_binding!!.emailEditText)
                disableView(_binding!!.dobEditText)
                disableView(_binding!!.workerFullNameEditText)

                if(it.typeOfWork.equals("Driver", ignoreCase = true)) {
                    _binding?.typeOfWorker?.check(R.id.radioButton1);
                }else {
                    _binding?.typeOfWorker?.check(R.id.radioButton2);
                }

                if(it.gender.equals("Male", ignoreCase = true)) {
                    _binding?.genderRadioGroup?.check(R.id.genderradioButton1);
                }else {
                    _binding?.genderRadioGroup?.check(R.id.genderradioButton1);
                }
                if(it.vehicleType.equals("0", ignoreCase = true)) {
                    _binding?.vehicleTyperadioGroup?.check(R.id.vehicelTyperadioButton1);
                }else {
                    _binding?.vehicleTyperadioGroup?.check(R.id.vehicelTyperadioButton1);
                }
                _binding?.workingDaysEditText?.setText(it.noOfHours)
                _binding?.fatherEditText?.setText(it.fatherName)
            }
            Snackbar.make(requireView(), "Already Registered User", Snackbar.LENGTH_LONG)
                .setAction("CLOSE") { }
                .setActionTextColor(resources.getColor(android.R.color.holo_red_light))
                .show()
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
        val serviceType = _binding?.typeOfService?.selectedItem.toString()
        val  typeOfWork = typeOfWorker
        val vehicleType = vehicleType
        val  noOfHours = _binding?.workingDaysEditText?.text.toString().trim()
        val fatherName = _binding?.fatherEditText?.text.toString()

        if (name.isEmpty()){
            Toast.makeText(requireContext(), "Please enter name", Toast.LENGTH_SHORT).show()
            return false
        }

        if (gender.isEmpty()){
            Toast.makeText(requireContext(), "Please select gender", Toast.LENGTH_SHORT).show()
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
        if (serviceType.isEmpty()){
            Toast.makeText(requireContext(), "Please select service type", Toast.LENGTH_SHORT).show()
            return false
        }
        if (vehicleType.isEmpty()){
            Toast.makeText(requireContext(), "Please select vehicle type", Toast.LENGTH_SHORT).show()
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
            Toast.makeText(requireContext(), "Please enter pan number", Toast.LENGTH_SHORT).show()
            return false
        }

        if (noOfHours.isEmpty() || noOfHours.toInt() > 24 ){
            Toast.makeText(requireContext(), "Please enter working hours", Toast.LENGTH_SHORT).show()
            return false
        }
        if (::leftImageFile.isInitialized && name.isNotEmpty() && gender.isNotEmpty() && dob.isNotEmpty() && aadhar.isNotEmpty() && mobile.isNotEmpty() &&
            pan.isNotEmpty() && email.isNotEmpty()  && typeOfWork.isNotEmpty()  &&
            noOfHours.isNotEmpty() && fatherName.isNotEmpty()) {
            return true
        } else {
            Toast.makeText(requireContext(), "Please fill all the fields", Toast.LENGTH_SHORT).show()
            return false
        }


    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun serviceTypeData(response: ServiceResponse?) {
        val details = response?.details?.map { it.name }

        val adapter: ArrayAdapter<String> =
            ArrayAdapter<String>(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                details!!.toMutableList()
            )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        _binding?.typeOfService?.adapter = adapter
        _binding?.typeOfService?.prompt = "Select One"

        _binding!!.typeOfService.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view:View?, position: Int, id: Long) {
                val selectedItem = parent?.getItemAtPosition(position)
                servieType = response?.details?.filter { it.name == selectedItem }?.firstOrNull()?.id

                // Do something with the selected item
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do something when nothing is selected
            }}

    }


    private fun qualificationData(response: QualificationResponse?) {
        val details = response?.details?.map { it.name }

        val adapter: ArrayAdapter<String> =
            ArrayAdapter<String>(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                details!!.toMutableList()
            )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        _binding?.spinnerQualification?.adapter = adapter
        _binding?.spinnerQualification?.prompt = "Select One";

        _binding!!.spinnerQualification.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view:View?, position: Int, id: Long) {
                val selectedItem = parent?.getItemAtPosition(position)
                qualificationType = response?.details?.filter { it.name == selectedItem }?.firstOrNull()?.id!!
                // Do something with the selected item
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do something when nothing is selected
            }}



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
        val datePickerDialog = DatePickerDialog(
            requireContext(), {DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, monthOfYear, dayOfMonth)
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val formattedDate = dateFormat.format(selectedDate.time)
                _binding!!.dobEditText.setText(formattedDate)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
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
            serviceType = servieType.toString()
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