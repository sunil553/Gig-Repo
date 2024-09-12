package com.llyod.task.fragment

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.llyod.domain.model.form.CompanyReponse
import com.llyod.domain.model.form.DistrictsForStatesResponse
import com.llyod.domain.model.form.ServiceResponse
import com.llyod.domain.model.form.StatesModelResponse
import com.llyod.task.R
import com.llyod.task.databinding.FragmentAddressBinding
import com.llyod.task.viewmodel.SharedViewModel

class AddressFragment : Fragment() {

    private var _binding: FragmentAddressBinding? = null
    private val binding get() = _binding!!

    private val sharedViewModel: SharedViewModel by activityViewModels()

    private var  selectedDistrict = ""
    private var  selectedTempDistrict = ""

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

        // Restore the state of the fragment here
        sharedViewModel.addressDetailsModel?.let {
            _binding?.editTextPermanentHouseNo?.setText(it.house_no)
            _binding?.editTextPermanentStreetName?.setText(it.street_name)
            binding.editTextPermanentColonyArea.setText(it.colony_area)
            binding.editTextPermanentLandmark.setText(it.landmark)
            binding.spinnerDistrict.setSelection(1)
            binding.editTextPermanentPinCode.setText(it.house_no)
            binding.editTextPresentHouseNo.setText(it.temp_house_no)
            binding.editTextPresentStreetName.setText(it.temp_street_name)
            binding.editTextPresentColonyArea.setText(it.temp_colony_area)
            binding.editTextPresentLandmark.setText(it.temp_landmark)
            binding.spinnerTempDistrict.setSelection(1)
            binding.editTextPresentPinCode.setText(it.temp_pincode)
        }


        _binding?.next?.setOnClickListener {
            if (validateAddressFields()) {
                sharedViewModel.saveAddressDetails(
                    binding.editTextPermanentHouseNo.text.toString(),
                    binding.editTextPermanentStreetName.text.toString(),
                    binding.editTextPermanentColonyArea.text.toString(),
                    binding.editTextPermanentLandmark.text.toString(),
                    binding.editTextPermanentPinCode.text.toString(),
                    state = "36",
                    district = selectedDistrict,
                    binding.editTextPresentHouseNo.text.toString(),
                    binding.editTextPresentStreetName.text.toString(),
                    binding.editTextPresentColonyArea.text.toString(),
                    binding.editTextPresentLandmark.text.toString(),
                    binding.editTextPresentPinCode.text.toString(),
                    temp_state =  "36",
                    temp_district = selectedTempDistrict,
                    isCheckBoxChecked()
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
                binding.spinnerTempDistrict.setSelection(permanentDistrict)
            } else {
                binding.editTextPresentHouseNo.setText("")
                binding.editTextPresentStreetName.setText("")
                binding.editTextPresentColonyArea.setText("")
                binding.editTextPresentLandmark.setText("")
                binding.editTextPresentPinCode.setText("")
                binding.spinnerTempDistrict.setSelection(0)
            }

        }
        _binding?.prev?.setOnClickListener {
            findNavController().navigateUp()
        }

        sharedViewModel.getDistrictsByStateId("36")
        sharedViewModel.districtLiveData.observe(viewLifecycleOwner,::districtsTypeData)
        sharedViewModel.districtLiveData.observe(viewLifecycleOwner,::districtsTempTypeData)
        sharedViewModel.isRegisteredLiveData.observe(viewLifecycleOwner,::isRegisteredLiveData)

    }

    private fun isRegisteredLiveData(isRegisteredUser: Boolean?) {
        if (isRegisteredUser == true) {
            sharedViewModel.addressDetailsModel?.let {
                _binding?.editTextPermanentHouseNo?.setText(it.house_no)
                _binding?.editTextPermanentStreetName?.setText(it.street_name)
                binding.editTextPermanentColonyArea.setText(it.colony_area)
                binding.editTextPermanentLandmark.setText(it.landmark)
                binding.spinnerDistrict.setSelection(1)
                binding.editTextPermanentPinCode.setText(it.house_no)
                binding.editTextPresentHouseNo.setText(it.temp_house_no)
                binding.editTextPresentStreetName.setText(it.temp_street_name)
                binding.editTextPresentColonyArea.setText(it.temp_colony_area)
                binding.editTextPresentLandmark.setText(it.temp_landmark)
                binding.spinnerTempDistrict.setSelection(1)
                binding.editTextPresentPinCode.setText(it.temp_pincode)
            }
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

        if (permanentHouseNo.isBlank() || permanentStreetName.isBlank() || permanentColonyArea.isBlank() || permanentLandmark.isBlank() || permanentPinCode.isBlank()|| presentHouseNo.isBlank() || presentStreetName.isBlank() || presentColonyArea.isBlank() || presentLandmark.isBlank() || presentPinCode.isBlank()) {
            // Show error message or highlight empty fields
            Toast.makeText(requireContext(), "Please fill all the fields", Toast.LENGTH_SHORT).show()
            return false
        }

      /*  if (!isValidPinCode(permanentPinCode) || !isValidPinCode(presentPinCode)) {
            // Show error message for invalid pin codes
            return false
        }*/
        // Add more specific validations for other fields if needed
        return true
    }

  /*  private fun isValidPinCode(pinCode: String): Boolean {
        return pinCode.matches(Regex("\\d{6}"))
    }*/

    private fun isCheckBoxChecked(): String {
       return if (binding.checkBoxSameAddress.isChecked) "1" else  "0"
    }

    private fun districtsTempTypeData(response: DistrictsForStatesResponse?) {
        val details = response?.details?.map { it.name }

        val adapter: ArrayAdapter<String> =
            ArrayAdapter<String>(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                details!!.toMutableList()
            )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        _binding?.spinnerTempDistrict?.adapter = adapter

        _binding!!.spinnerTempDistrict.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view:View?, position: Int, id: Long) {
                val selectedItem = parent?.getItemAtPosition(position)
                val filter = response?.details?.firstOrNull { it -> it.name == selectedItem }
                selectedDistrict = filter?.code.toString()
                // Do something with the selected item
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do something when nothing is selected
            }}
    }

    private fun districtsTypeData(response: DistrictsForStatesResponse?) {
        val details = response?.details?.map { it.name }

        val adapter: ArrayAdapter<String> =
            ArrayAdapter<String>(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                details!!.toMutableList()
            )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        _binding?.spinnerDistrict?.adapter = adapter

        _binding!!.spinnerDistrict.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view:View?, position: Int, id: Long) {
                val selectedItem = parent?.getItemAtPosition(position)
                val filter = response?.details?.firstOrNull { it -> it.name == selectedItem }
                selectedTempDistrict = filter?.code.toString()
                // Do something with the selected item
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do something when nothing is selected
            }}

    }



    private fun navigateToDetails() {
        findNavController().navigate(R.id.action_addressFragment_to_bankFragment)
    }



}