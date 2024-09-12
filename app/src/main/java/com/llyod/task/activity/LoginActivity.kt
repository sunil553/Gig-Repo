package com.llyod.task.activity

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.llyod.task.databinding.FragmentMobileNumberBinding
import com.llyod.task.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private var _binding: FragmentMobileNumberBinding? = null
    private val viewModel: LoginViewModel by viewModels()




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = FragmentMobileNumberBinding.inflate(layoutInflater)
        val view = _binding!!.root
        setContentView(view)
        _binding!!.lifecycleOwner = this
        _binding!!.viewModel = viewModel

        if (viewModel.getAccessToken())  {
            Intent(this, GigWorkerActivity::class.java).apply {
                startActivity(this)
                finish()
            }
        } else {
            viewModel.loginLiveData.observe(this,::onLoginSuccess)
        }

        viewModel.errorMessages.observe(this,::onErrorMessage)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_DENIED ||
                checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_DENIED) {
                val permission = arrayOf<String>(
                    android.Manifest.permission.CAMERA,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
                requestPermissions(permission, 112)
            }
        }

    }

    private fun onErrorMessage(errorMessage: String?) {
        errorMessage?.let {
            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
        }
    }

    private fun onLoginSuccess(b: Boolean?) {
        Intent(this, GigWorkerActivity::class.java).apply {
            startActivity(this)
            finish()
        }

    }

    companion object {
        private const val REFRESH_TOKEN = "REFRESH_TOKEN"
        private const val ACCESS_TOKEN = "ACCESS_TOKEN"
    }


}