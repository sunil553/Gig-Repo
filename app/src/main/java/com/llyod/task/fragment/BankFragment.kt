package com.llyod.task.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.llyod.task.R
import com.llyod.task.databinding.FragmentBankBinding
import com.llyod.task.viewmodel.SharedViewModel

class BankFragment : Fragment() {

    private var _binding: FragmentBankBinding? = null
    private val binding get() = _binding!!

    private val sharedViewModel: SharedViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBankBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedViewModel.bankDetailsModel?.let {
            _binding?.editTextAccountNumber?.setText(it.account_no)
            _binding?.editTextIFSCCode?.setText(it.ifsc_code)
            _binding?.editTextBranch?.setText(it.branch)
            _binding?.editTextNomineeName?.setText(it.nominee_name)
            _binding?.editTextNomineeRelation?.setText(it.nominee_relation)
            _binding?.editTextNomineeAge?.setText(it.nominee_age)
            _binding?.editTextNomineeAccountNumber?.setText(it.nominee_account_no)
            _binding?.editTextNomineeIFSCCode?.setText(it.nominee_ifsc_code)
            _binding?.editTextNomineeBranch?.setText(it.nominee_branch)
        }
        _binding?.next?.setOnClickListener {
            if (validateBankAndNomineeDetails()){
                sharedViewModel.saveBankDetails(
                    _binding!!.editTextAccountNumber.text.toString(),
                    _binding!!.editTextIFSCCode.text.toString(),
                    _binding!!.editTextBranch.text.toString(),
                    _binding!!.editTextNomineeName.text.toString(),
                    _binding!!.editTextNomineeRelation.text.toString(),
                    _binding!!.editTextNomineeAge.text.toString(),
                    _binding!!.editTextNomineeAccountNumber.text.toString(),
                    _binding!!.editTextNomineeIFSCCode.text.toString(),
                    _binding!!.editTextNomineeBranch.text.toString()
                )
                navigateToDetails()
            }

        }
        _binding?.prev?.setOnClickListener {
            findNavController().navigateUp()
        }
        sharedViewModel.isRegisteredLiveData.observe(viewLifecycleOwner,::isRegisteredLiveData)

    }
    private fun isRegisteredLiveData(isRegisteredUser: Boolean?) {
        if (isRegisteredUser == true) {
            sharedViewModel.bankDetailsModel?.let {
                _binding?.editTextAccountNumber?.setText(it.account_no)
                _binding?.editTextIFSCCode?.setText(it.ifsc_code)
                _binding?.editTextBranch?.setText(it.branch)
                _binding?.editTextNomineeName?.setText(it.nominee_name)
                _binding?.editTextNomineeRelation?.setText(it.nominee_relation)
                _binding?.editTextNomineeAge?.setText(it.nominee_age)
                _binding?.editTextNomineeAccountNumber?.setText(it.nominee_account_no)
                _binding?.editTextNomineeIFSCCode?.setText(it.nominee_ifsc_code)
                _binding?.editTextNomineeBranch?.setText(it.nominee_branch)
            }
        }
        }

    private fun validateBankAndNomineeDetails(): Boolean {
        val accountNumber = _binding!!.editTextAccountNumber.text.toString()
        val ifscCode = _binding!!.editTextIFSCCode.text.toString()
        val branch = _binding!!.editTextBranch.text.toString()
        val nomineeName = _binding!!.editTextNomineeName.text.toString()
        val nomineeRelation = _binding!!.editTextNomineeRelation.text.toString()
        val nomineeAge = _binding!!.editTextNomineeAge.text.toString()
        val nomineeAccountNumber = _binding!!.editTextNomineeAccountNumber.text.toString()
        val nomineeIfscCode = _binding!!.editTextNomineeIFSCCode.text.toString()
        val nomineeBranch = _binding!!.editTextNomineeBranch.text.toString()

        if (accountNumber.isBlank() || ifscCode.isBlank() || branch.isBlank() || nomineeName.isBlank() || nomineeRelation.isBlank() || nomineeAge.isBlank()
            || nomineeAccountNumber.isBlank() || nomineeIfscCode.isBlank() || nomineeBranch.isBlank()) {
            Toast.makeText(requireContext(), "Please enter all fields", Toast.LENGTH_SHORT).show()
            return false
        }

        // Add more specific validations if needed
       /* if (!isValidAccountNumber(accountNumber)) {
            // Show error message for invalid account number
            return false
        }*/

      /*  if (!isValidIFSCCode(ifscCode)) {
            // Show error message for invalid IFSC code
            return false
        }*/

        if (!isValidAge(nomineeAge)) {
            // Show error message for invalid age
            Toast.makeText(requireContext(), "Please Enter Valid Age", Toast.LENGTH_SHORT).show()
            return false
        }

        // ... Add similar validations for other fields

        return true
    }

    private fun isValidAccountNumber(accountNumber: String): Boolean {
        // Implement your account number validation logic here
        // This might involve checking length, format, or other criteria
        return accountNumber.length in 10..20 // Example: Account number should be between 10 and 20 digits
    }

    private fun isValidIFSCCode(ifscCode: String): Boolean {
        // Implement your IFSC code validation logic here
        // You can use regular expressions or other methods to validate the format
        return ifscCode.matches(Regex("[A-Z]{4}0[A-Z0-9]{6}")) // Example: Basic IFSC code format check
    }

    private fun isValidAge(age: String): Boolean {
        // Implement your age validation logic here
        return try {
            val ageInt = age.toInt()
            ageInt in 1..150 // Example: Age should be between 1 and 150
        } catch (e: NumberFormatException) {
            false // Handle cases where age is not a valid number
        }
    }

    private fun navigateToDetails() {
        findNavController().navigate(R.id.action_bankFragment_to_companyFragment)
    }
}