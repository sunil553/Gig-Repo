package com.llyod.task.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.llyod.domain.model.form.Detail
import com.llyod.domain.model.form.DistrictsForStatesResponse
import com.llyod.domain.model.form.StatesModelResponse
import com.llyod.task.R
import com.llyod.task.databinding.FragmentAddressBinding
import com.llyod.task.viewmodel.SharedViewModel

class AddressFragment : Fragment() {

    private var _binding: FragmentAddressBinding? = null
    private val binding get() = _binding!!

    private val sharedViewModel: SharedViewModel by activityViewModels()

    private var  selectedState = "-1"
    private var  spinnerTempState = "-1"
    private var  selectedDistrict = "-1"
    private var  selectedTempDistrict = "-1"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddressBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedViewModel.errorMessages.observe(viewLifecycleOwner,::onErrorMessage)

        // Restore the state of the fragment here
        sharedViewModel.addressDetailsModel?.let {
            _binding?.editTextPermanentHouseNo?.setText(it.house_no)
            _binding?.editTextPermanentStreetName?.setText(it.street_name)
            binding.editTextPermanentColonyArea.setText(it.colony_area)
            binding.editTextPermanentLandmark.setText(it.landmark)
            selectSpinnerItemByValue(binding.spinnerState,it.state)
            selectSpinnerItemByValue(binding.spinnerDistrict,it.district)
            binding.editTextPermanentPinCode.setText(it.pincode)
            binding.editTextPresentHouseNo.setText(it.temp_house_no)
            binding.editTextPresentStreetName.setText(it.temp_street_name)
            binding.editTextPresentColonyArea.setText(it.temp_colony_area)
            binding.editTextPresentLandmark.setText(it.temp_landmark)
            selectSpinnerItemByValue(binding.spinnerTempState,it.temp_state)
            selectSpinnerItemByValue(binding.spinnerTempDistrict,it.temp_district)
            binding.editTextPresentPinCode.setText(it.temp_pincode)
        }



        _binding?.next?.setOnClickListener {
            if (validateAddressFields()) {
                var isCheckeds = isCheckBoxChecked()
                if (selectedState != spinnerTempState ) {
                    isCheckeds = "0"
                }
                sharedViewModel.saveAddressDetails(
                    binding.editTextPermanentHouseNo.text.toString(),
                    binding.editTextPermanentStreetName.text.toString(),
                    binding.editTextPermanentColonyArea.text.toString(),
                    binding.editTextPermanentLandmark.text.toString(),
                    binding.editTextPermanentPinCode.text.toString(),
                    state = selectedState,
                    district = selectedDistrict,
                    binding.editTextPresentHouseNo.text.toString(),
                    binding.editTextPresentStreetName.text.toString(),
                    binding.editTextPresentColonyArea.text.toString(),
                    binding.editTextPresentLandmark.text.toString(),
                    binding.editTextPresentPinCode.text.toString(),
                    temp_state =  spinnerTempState,
                    temp_district = selectedTempDistrict,
                    isCheckeds
                )
                navigateToDetails()
            }

        }
        _binding?.checkBoxSameAddress?.setOnCheckedChangeListener {
                _, isChecked ->

            val permanentHouseNo =  binding.editTextPermanentHouseNo.text.toString()
            val permanentStreetName =   binding.editTextPermanentStreetName.text.toString()
            val permanentColonyArea =   binding.editTextPermanentColonyArea.text.toString()
            val permanentLandMark =   binding.editTextPermanentLandmark.text.toString()
            val permanentPincode =   binding.editTextPermanentPinCode.text.toString()
            val permanentDistrict = binding.spinnerDistrict.selectedItemPosition
            if (isChecked) {
                binding.editTextPresentHouseNo.setText(permanentHouseNo)
                binding.editTextPresentStreetName.setText(permanentStreetName)
                binding.editTextPresentColonyArea.setText(permanentColonyArea)
                binding.editTextPresentLandmark.setText(permanentLandMark)
                binding.editTextPresentPinCode.setText(permanentPincode)
                if (isSelectedStateTG()){
                    try {
                        binding.spinnerTempState.setSelection(1)
                        binding.spinnerTempDistrict.setSelection(permanentDistrict)
                    }catch (exception : Exception) {

                    }

                }
                enableDisableView( binding.editTextPresentHouseNo, false)
                enableDisableView( binding.editTextPresentStreetName, false)
                enableDisableView( binding.editTextPresentColonyArea, false)
                enableDisableView( binding.editTextPresentLandmark, false)
                enableDisableView( binding.editTextPresentPinCode, false)
            } else {
                binding.editTextPresentHouseNo.setText("")
                binding.editTextPresentStreetName.setText("")
                binding.editTextPresentColonyArea.setText("")
                binding.editTextPresentLandmark.setText("")
                binding.editTextPresentPinCode.setText("")
                binding.spinnerTempDistrict.setSelection(0)
                binding.spinnerTempState.setSelection(0)
                enableDisableView( binding.editTextPresentHouseNo, true)
                enableDisableView( binding.editTextPresentStreetName, true)
                enableDisableView( binding.editTextPresentColonyArea, true)
                enableDisableView( binding.editTextPresentLandmark, true)
                enableDisableView( binding.editTextPresentPinCode, true)

            }

        }
        _binding?.prev?.setOnClickListener {
            findNavController().navigateUp()
        }

        sharedViewModel.getStates()
        sharedViewModel.statesLiveData.observe(viewLifecycleOwner,::selectedState)
        sharedViewModel.statesLiveData.observe(viewLifecycleOwner,::selectedTempState)
        sharedViewModel.getDistrictsByTempStateId()
        sharedViewModel.districtLiveData.observe(viewLifecycleOwner,::districtsTypeData)
        sharedViewModel.districtTempLiveData.observe(viewLifecycleOwner,::districtsTempTypeData)
        sharedViewModel.isRegisteredLiveData.observe(viewLifecycleOwner,::isRegisteredLiveData)

        _binding?.progressLayout?.bar1?.setBackgroundColor(requireContext().resources.getColor(R.color.purple_indicator))
        _binding?.progressLayout?.bar2?.setBackgroundColor(requireContext().resources.getColor(R.color.purple_indicator))
        _binding?.progressLayout?.bar3?.setBackgroundColor(requireContext().resources.getColor(R.color.disable_indicator))
        _binding?.progressLayout?.bar4?.setBackgroundColor(requireContext().resources.getColor(R.color.disable_indicator))
        _binding?.progressLayout?.bar5?.setBackgroundColor(requireContext().resources.getColor(R.color.disable_indicator))

        _binding?.progressLayout?.framelayout1?.background  = ResourcesCompat.getDrawable(resources, R.drawable.circle_purple, null)
        _binding?.progressLayout?.framelayout2?.background =  ResourcesCompat.getDrawable(resources, R.drawable.circle_purple, null)
        _binding?.progressLayout?.framelayout3?.background =  ResourcesCompat.getDrawable(resources, R.drawable.circle_grey, null)
        _binding?.progressLayout?.framelayout4?.background =  ResourcesCompat.getDrawable(resources, R.drawable.circle_grey, null)

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

    private fun isSelectedStateTG(): Boolean {
          return  selectedState == "36"
    }

    private fun onErrorMessage(errorMessage: String?) {
        errorMessage?.let {
            Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
        }
    }
    fun enableDisableView(view: EditText, isChecked: Boolean){
        view.isClickable = isChecked
        view.isFocusable = isChecked
        view.isFocusableInTouchMode = isChecked
    }

    private fun selectedTempState(response: StatesModelResponse?) {
        val details  = response?.details?.filter { it.code == "36" }?.map { it.name }
        val tempStateDetails = details

        val toMutableList = tempStateDetails!!.toMutableList()
        toMutableList.add(0,"Select State")

        val adapter: ArrayAdapter<String> =
            ArrayAdapter<String>(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                toMutableList!!.toMutableList()
            )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        _binding?.spinnerTempState?.adapter = adapter

        _binding!!.spinnerTempState.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view:View?, position: Int, id: Long) {
                val selectedItem = parent?.getItemAtPosition(position)
                if (selectedItem?.equals("Select State") == true) {
                    spinnerTempState = "-1"
                } else{
                    val filter = response?.details?.firstOrNull { it -> it.name == selectedItem }
                    spinnerTempState = filter?.code.toString()
                    sharedViewModel.getDistrictsByTempStateId()
                }
                // Do something with the selected item
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do something when nothing is selected
            }}

        sharedViewModel.addressDetailsModel?.let { address ->
            val addressDetails = response.details.filter { it.name == address.temp_state }
            if (addressDetails.isNotEmpty()) {
                address.temp_state.let { it1 ->
                    selectSpinnerItemByValueByListTempState(binding.spinnerTempState,
                        it1,addressDetails
                    )
                }
            } else {
                val addressDetails1 = response.details.filter { it.id.toString() == address.temp_state }
                address.temp_state?.let { it1 ->
                    selectSpinnerItemByValueByListTempState(binding.spinnerTempState,
                        it1,addressDetails1
                    )
                }
            }

        } ?: run {
            districtsTempTypeData(null)
        }
    }

    private fun selectedState(response: StatesModelResponse?) {

        val details = response?.details?.map { it.name }

        val allStates = details!!.toMutableList()
        allStates.add(0,"Select State")

        val adapter: ArrayAdapter<String> =
            ArrayAdapter<String>(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                allStates!!.toMutableList()
            )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        _binding?.spinnerState?.adapter = adapter

        _binding!!.spinnerState.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view:View?, position: Int, id: Long) {
                val selectedItem = parent?.getItemAtPosition(position)
                if (selectedItem?.equals("Select State") == true) {
                    selectedState = "-1"
                } else{
                    val filter = response?.details?.firstOrNull { it -> it.name == selectedItem }
                    selectedState = filter?.code.toString()
                    sharedViewModel.getDistrictsByStateId(filter?.code.toString())
                }

                // Do something with the selected item
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do something when nothing is selected
            }}

        sharedViewModel.addressDetailsModel?.let { addressDetailModel ->
           val stateDetail =  response?.details?.filter { it.name == addressDetailModel.state }
            if (stateDetail?.isNotEmpty() == true) {
                selectSpinnerItemByValueByList(binding.spinnerState,stateDetail?.firstOrNull()?.code,response?.details!!)
            } else{
                val stateDetail =  response?.details?.filter { it.code == addressDetailModel.state }
                selectSpinnerItemByValueByList(binding.spinnerState,stateDetail?.firstOrNull()?.code,response?.details!!)
            }

        } ?: kotlin.run {
            districtsTypeData(null)
        }

    }

    private fun selectSpinnerItemByValueByListTempState(spnr: Spinner, value: String?, details: List<Detail>) {
        try {
            val adapter = spnr.adapter
            for (position in 0 until adapter.count) {
                    spnr.setSelection(1)
                    return
            }
        }catch (exception : Exception){

        }

    }
    private fun selectSpinnerItemByValueByList(spnr: Spinner, value: String?, details: List<Detail>) {
        try {
            val adapter = spnr.adapter
            val details =  details.filter { it.code == value }
            for (position in 0 until adapter.count) {
                if (adapter.getItem(position) == details.firstOrNull()?.name) {
                    spnr.setSelection(position)
                    return
                }
            }
        }catch (exception : Exception){

        }

    }
    private fun isRegisteredLiveData(isRegisteredUser: Boolean?) {
        if (isRegisteredUser == true) {
            sharedViewModel.addressDetailsModel?.let {
                _binding?.progressLayout?.bar1?.setBackgroundColor(requireContext().resources.getColor(R.color.purple_indicator))
                _binding?.progressLayout?.bar2?.setBackgroundColor(requireContext().resources.getColor(R.color.purple_indicator))
                _binding?.progressLayout?.bar3?.setBackgroundColor(requireContext().resources.getColor(R.color.purple_indicator))
                _binding?.progressLayout?.bar4?.setBackgroundColor(requireContext().resources.getColor(R.color.purple_indicator))
                _binding?.progressLayout?.bar5?.setBackgroundColor(requireContext().resources.getColor (R.color.purple_indicator))

                _binding?.progressLayout?.framelayout1?.background  = ResourcesCompat.getDrawable(resources, R.drawable.circle_purple, null)
                _binding?.progressLayout?.framelayout2?.background =  ResourcesCompat.getDrawable(resources, R.drawable.circle_purple, null)
                _binding?.progressLayout?.framelayout3?.background =  ResourcesCompat.getDrawable(resources, R.drawable.circle_purple, null)
                _binding?.progressLayout?.framelayout4?.background =  ResourcesCompat.getDrawable(resources, R.drawable.circle_purple, null)

                _binding?.editTextPermanentHouseNo?.setText(it.house_no)
                _binding?.editTextPermanentStreetName?.setText(it.street_name)
                binding.editTextPermanentColonyArea.setText(it.colony_area)
                binding.editTextPermanentLandmark.setText(it.landmark)
                selectSpinnerItemByValue( binding.spinnerState,it.state)
                selectSpinnerItemByValue( binding.spinnerDistrict,it.temp_district)
                binding.editTextPermanentPinCode.setText(it.pincode)
                binding.editTextPresentHouseNo.setText(it.temp_house_no)
                binding.editTextPresentStreetName.setText(it.temp_street_name)
                binding.editTextPresentColonyArea.setText(it.temp_colony_area)
                binding.editTextPresentLandmark.setText(it.temp_landmark)
                selectSpinnerItemByValueState( binding.spinnerTempState,it.temp_state)
                selectSpinnerItemByValue( binding.spinnerTempDistrict,it.temp_district)
                binding.editTextPresentPinCode.setText(it.temp_pincode)
            }
        }
    }
    private fun selectSpinnerItemByValue(spnr: Spinner, value: String) {
        try {
            val adapter = spnr.adapter
            for (position in 0 until adapter.count) {
                if (adapter.getItem(position) == value) {
                    spnr.setSelection(position)
                    return
                }
            }
        }catch (exception : Exception) {

        }

    }
    private fun selectSpinnerItemByValueState(spnr: Spinner, value: String) {
        try {
            val adapter = spnr.adapter
            for (position in 0 until adapter.count) {
                    spnr.setSelection(1)
                    return
            }
        }catch (exception : Exception) {

        }

    }


    private fun validateAddressFields(): Boolean {
        val permanentHouseNo = binding.editTextPermanentHouseNo.text.toString()
        val permanentStreetName = binding.editTextPermanentStreetName.text.toString()
        val permanentColonyArea = binding.editTextPermanentColonyArea.text.toString()
        val permanentLandmark = binding.editTextPermanentLandmark.text.toString()
        val permanentPinCode = binding.editTextPermanentPinCode.text.toString()
        val presentHouseNo = binding.editTextPresentHouseNo.text.toString()
        val presentStreetName = binding.editTextPresentStreetName.text.toString()
        val presentColonyArea = binding.editTextPresentColonyArea.text.toString()
        val presentLandmark = binding.editTextPresentLandmark.text.toString()
        val presentPinCode = binding.editTextPresentPinCode.text.toString()

//        val selectedState = binding.spinnerState.selectedItem.toString()
//        val selectedDistrict = binding.spinnerDistrict.selectedItem.toString()
//        val selectedTempState = binding.spinnerTempState.selectedItem.toString()
//        val selectedTempDistrict = binding.spinnerTempDistrict.selectedItem.toString()




        if (permanentHouseNo.isBlank()) {
            Toast.makeText(requireContext(), "Please enter house number", Toast.LENGTH_SHORT).show()
            return false
        }

        if (permanentStreetName.isBlank()) {
            Toast.makeText(requireContext(), "Please enter street name", Toast.LENGTH_SHORT).show()
            return false
        }


        if (permanentColonyArea.isBlank()) {
            Toast.makeText(requireContext(), "Please enter permanent colony/area", Toast.LENGTH_SHORT).show()
            return false
        }

        if (permanentLandmark.isBlank()) {
            Toast.makeText(requireContext(), "Please enter permanent landmark", Toast.LENGTH_SHORT).show()
            return false
        }
        if (selectedState == "-1") {
            Toast.makeText(requireContext(), "Please select a state", Toast.LENGTH_SHORT).show()
            return false
        }

        if (selectedDistrict == "-1") {
            Toast.makeText(requireContext(), "Please select a district", Toast.LENGTH_SHORT).show()
            return false
        }


        if (permanentPinCode.isBlank()) {
            Toast.makeText(requireContext(), "Please enter permanent pin code", Toast.LENGTH_SHORT).show()
            return false
        }

        if (presentHouseNo.isBlank()) {
            Toast.makeText(requireContext(), "Please enter present house number", Toast.LENGTH_SHORT).show()
            return false
        }

        if (presentStreetName.isBlank()) {
            Toast.makeText(requireContext(), "Please enter present street name", Toast.LENGTH_SHORT).show()
            return false
        }

        if (presentColonyArea.isBlank()) {
            Toast.makeText(requireContext(), "Please enter present colony/area", Toast.LENGTH_SHORT).show()
            return false
        }

        if (presentLandmark.isBlank()) {
            Toast.makeText(requireContext(), "Please enter present landmark", Toast.LENGTH_SHORT).show()
            return false
        }
        if (spinnerTempState == "-1") {
            Toast.makeText(requireContext(), "Please select a temporary state", Toast.LENGTH_SHORT).show()
            return false
        }

        if (selectedTempDistrict == "-1") {
            Toast.makeText(requireContext(), "Please select a temporary district", Toast.LENGTH_SHORT).show()
            return false
        }
        if (presentPinCode.isBlank()) {
            Toast.makeText(requireContext(), "Please enter present pin code", Toast.LENGTH_SHORT).show()
            return false
        }

        if (!isValidPinCode(permanentPinCode) || !isValidPinCode(presentPinCode)) {
              // Show error message for invalid pin codes
            Toast.makeText(requireContext(), "Please Enter valid postal code", Toast.LENGTH_SHORT).show()
            return false
          }
        // Add more specific validations for other fields if needed
        return true
    }

     private fun isValidPinCode(pinCode: String): Boolean {
          return pinCode.matches(Regex("\\d{6}"))
      }

    private fun isCheckBoxChecked(): String {
        return if (binding.checkBoxSameAddress.isChecked) "1" else  "0"
    }

    private fun districtsTempTypeData(response: DistrictsForStatesResponse?) {
        val details = response?.details?.map { it.name }

        val toMutableList = details?.toMutableList() ?: mutableListOf()
        toMutableList.add(0,"Select District")

        val adapter: ArrayAdapter<String> =
            ArrayAdapter<String>(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                toMutableList!!.toMutableList()
            )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        _binding?.spinnerTempDistrict?.adapter = adapter


        _binding!!.spinnerTempDistrict.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view:View?, position: Int, id: Long) {
                val selectedItem = parent?.getItemAtPosition(position)
                if (selectedItem?.equals("Select District") == true) {
                    selectedTempDistrict = "-1"
                } else {
                    val filter = response?.details?.firstOrNull { it -> it.name == selectedItem }
                    selectedTempDistrict = filter?.code.toString()
                }
                // Do something with the selected item
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do something when nothing is selected
            }}
        sharedViewModel.addressDetailsModel?.let { addressDetailModel ->
            response?.details?.let {
                val districtDetails =  response?.details?.filter { it.name == addressDetailModel.temp_district }
                if (districtDetails?.isNotEmpty() == true) {
                    selectSpinnerItemByValueByList(binding.spinnerTempDistrict,districtDetails?.firstOrNull()?.code,response?.details!!)
                } else {
                    val districtDetails =  response?.details?.filter { it.code == addressDetailModel.temp_district }
                    selectSpinnerItemByValueByList(binding.spinnerTempDistrict,districtDetails?.firstOrNull()?.code,response?.details!!)
                }
            }

        }

    }

    private fun districtsTypeData(response: DistrictsForStatesResponse?) {
        val details = response?.details?.map { it.name }

        val toMutableList = details?.toMutableList() ?: mutableListOf()
        toMutableList.add(0,"Select District")

        val adapter: ArrayAdapter<String> =
            ArrayAdapter<String>(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                toMutableList.toMutableList()
            )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        _binding?.spinnerDistrict?.adapter = adapter

        _binding!!.spinnerDistrict.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view:View?, position: Int, id: Long) {
                val selectedItem = parent?.getItemAtPosition(position)
                if (selectedItem?.equals("Select District") == true) {
                    selectedDistrict = "-1"
                } else {
                    val filter = response?.details?.firstOrNull { it -> it.name == selectedItem }
                    selectedDistrict = filter?.code.toString()
                }

                // Do something with the selected item
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do something when nothing is selected
            }}

        sharedViewModel.addressDetailsModel?.let { addressDetailModel ->
            val stateDetail =  response?.details?.filter { it.name == addressDetailModel.district }
            if (stateDetail?.isNotEmpty() == true) {
                selectSpinnerItemByValueByList(binding.spinnerDistrict,stateDetail?.firstOrNull()?.code,response?.details!!)
            } else {
                val stateDetail1 =  response?.details?.filter { it.code == addressDetailModel.district }
                selectSpinnerItemByValueByList(binding.spinnerDistrict,stateDetail1?.firstOrNull()?.code,response?.details!!)
            }

        }

    }



    private fun navigateToDetails() {
        findNavController().navigate(R.id.action_addressFragment_to_bankFragment)
    }



}