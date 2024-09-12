package com.llyod.task.fragment

import android.app.DatePickerDialog
import android.content.Intent
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
import com.llyod.domain.model.form.CompanyReponse
import com.llyod.task.R
import com.llyod.task.activity.GigWorkerActivity
import com.llyod.task.databinding.FragmentCompanyBinding
import com.llyod.task.viewmodel.SharedViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class CompanyFragment : Fragment() {

    private var _binding: FragmentCompanyBinding? = null
    private val binding get() = _binding!!

    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val calendar = Calendar.getInstance()
    var primaryCompany = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCompanyBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        _binding?.next?.setOnClickListener {
            if (validateWorkerDetails()) {
                sharedViewModel.saveCompanyDetails(
                    _binding!!.editTextDateOfJoining.text.toString(),
                    primaryCompany.toString(),
                    _binding!!.editTextWorkerId.text.toString(),
                    _binding!!.editTextOfficeAddress.text.toString()
                )
                sharedViewModel.registerUser()
            }
//            navigateToDetails()
        }
        _binding?.prev?.setOnClickListener {
            findNavController().navigateUp()
        }

        sharedViewModel.getCompanyTypes()
        sharedViewModel.companyLiveData.observe(viewLifecycleOwner,::getCompanyDetails)

        sharedViewModel.loading.observe(viewLifecycleOwner,::showLoader)
        sharedViewModel.registerSuccessLiveData.observe(viewLifecycleOwner,::onRegistrationSuccess)

        _binding?.editTextDateOfJoining?.setOnClickListener {
            showDatePicker()
        }
        sharedViewModel.isRegisteredLiveData.observe(viewLifecycleOwner,::isRegisteredLiveData)

    }

    private fun showDatePicker() {

        // Create a DatePickerDialog
        val datePickerDialog = DatePickerDialog(
            requireContext(), {DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                // Create a new Calendar instance to hold the selected date
                val selectedDate = Calendar.getInstance()
                // Set the selected date using the values received from the DatePicker dialog
                selectedDate.set(year, monthOfYear, dayOfMonth)
                // Create a SimpleDateFormat to format the date as "dd/MM/yyyy"
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                // Format the selected date into a string
                val formattedDate = dateFormat.format(selectedDate.time)
                // Update the TextView to display the selected date with the "Selected Date: " prefix
                _binding!!.editTextDateOfJoining.setText(formattedDate)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
        datePickerDialog.show()
    }
    private fun isRegisteredLiveData(isRegisteredUser: Boolean?) {
        if (isRegisteredUser == true) {
            sharedViewModel.companyModel?.let {
                _binding?.editTextOfficeAddress?.setText(it.office_address)
                _binding?.editTextDateOfJoining?.setText(it.doj)
                _binding?.editTextWorkerId?.setText(it.primary_working_id)
                _binding?.spinnerCompanyName?.setSelection(2)
            }
        }
    }
    private fun onRegistrationSuccess(s: String?) {
        if (s == "Success"){
            GlobalScope.launch(Dispatchers.Main) {
                showCustomToast()
                delay(3500)
                activity?.finish()
                val intent = Intent(
                    activity,
                    GigWorkerActivity::class.java
                )
                startActivity(intent)

            }
        } else {

        }


    }
    private fun showCustomToast() {
        val inflater = layoutInflater
        val layout: View = inflater.inflate(
            R.layout.toast,
            requireActivity().findViewById(com.llyod.task.R.id.toast_layout_root)
        )
        val toast = Toast(requireContext())
        toast.setView(layout)
        toast.show()
    }

    private fun validateWorkerDetails(): Boolean {
        val dateOfJoining = _binding!!.editTextDateOfJoining.text.toString()
        val primaryCompany = primaryCompany.toString() // Assuming primaryCompany is already a String
        val workerId = _binding!!.editTextWorkerId.text.toString()
        val officeAddress = _binding!!.editTextOfficeAddress.text.toString()

        if (dateOfJoining.isBlank() || workerId.isBlank() || officeAddress.isBlank()) {
            // Show error message or highlight empty fields
            Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return false
        }

        /*if (!isValidDate(dateOfJoining)) {
            // Show error message for invalid date format
            return false
        }

        if (!isValidWorkerId(workerId)) {
            // Show error message for invalid worker ID format
            return false
        }*/

        // You can add more specific validations for officeAddress if needed

        return true
    }

    private fun isValidDate(date: String): Boolean {
        // Implement your date validation logic here// You can use regular expressions or a date parsing library
        // Example using a simple date format (YYYY-MM-DD)
        return date.matches(Regex("\\d{4}-\\d{2}-\\d{2}"))
    }

    private fun isValidWorkerId(workerId: String): Boolean {
        // Implement your worker ID validation logic here
        // This might involve checking length, allowed characters, or a specific pattern
        // Example: Worker ID should be alphanumeric and at least 6 characters long
        return workerId.matches(Regex("[a-zA-Z0-9]{6,}"))
    }
    private fun showLoader(b: Boolean?) {
        if (b == true) _binding?.loading?.visibility = View.VISIBLE else _binding?.loading?.visibility = View.GONE
    }

    private fun getCompanyDetails(response: CompanyReponse?) {
        val details = response?.details?.map { it.name }

        val adapter: ArrayAdapter<String> =
            ArrayAdapter<String>(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                details!!.toMutableList()
            )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        _binding?.spinnerCompanyName?.adapter = adapter

        _binding!!.spinnerCompanyName.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view:View?, position: Int, id: Long) {
                val selectedItem = parent?.getItemAtPosition(position)
                primaryCompany = response?.details?.filter { it.name == selectedItem }?.firstOrNull()?.id!!

                // Do something with the selected item
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do something when nothing is selected
            }}


    }

    private fun navigateToDetails() {
//        findNavController().navigate(R.id.action_bankFragment_to_companyFragment)
    }
}