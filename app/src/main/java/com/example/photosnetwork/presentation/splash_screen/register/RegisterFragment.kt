package com.example.photosnetwork.presentation.splash_screen.register

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.photosnetwork.R
import com.example.photosnetwork.databinding.FragmentRegisterBinding
import com.example.photosnetwork.domain.model.UserAuthInput
import com.example.photosnetwork.presentation.base.BaseFragment
import com.example.photosnetwork.presentation.main.MainActivity
import com.example.photosnetwork.presentation.splash_screen.SplashActivity
import com.example.photosnetwork.util.Constants
import com.example.photosnetwork.util.Constants.TAG
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.regex.Pattern

@AndroidEntryPoint
class RegisterFragment : BaseFragment<FragmentRegisterBinding>() {

    override var hostActivity: Activity?
        get() = this.activity
        set(value) {}

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentRegisterBinding
        get() = FragmentRegisterBinding::inflate

    private val viewModel by viewModels<RegisterViewModel>()

    private val loginPattern: Pattern = Pattern.compile("[a-z0-9_\\-.@]+")

    override fun setup() {
        initObservers()
    }

    private fun initObservers() {
        binding.btnSignUp.setOnClickListener {
            if (!validateLogin() || !validatePassword()) {
                Toast.makeText(requireContext(),
                    resources.getString(R.string.incorrect_input),
                    Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (!confirmPassword()) {
                Toast.makeText(requireContext(),
                    resources.getString(R.string.incorrect_password_repeat),
                    Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            registerUser()
        }
        lifecycleScope.launch {
            viewModel.isErrorOccurred.collectLatest {
                toast(resources.getString(R.string.register_error_response))
            }
        }
        lifecycleScope.launch {
            viewModel.userAuthData.collectLatest {
                Log.d(TAG, "initObservers: $it")
                Intent(hostActivity!!, MainActivity::class.java).apply { startActivity(this) }
            }
        }
    }

    private fun registerUser() {
        val loginInput = binding.registerEtLogin.text?.trim().toString()
        val passwordInput = binding.registerEtPassword.text?.trim().toString()
        viewModel.registerUser(UserAuthInput(loginInput, passwordInput))
    }

    private fun validateLogin(): Boolean {
        val loginInput = binding.registerEtLogin.text.toString().trim()
        val login = binding.registerEtLogin
        if (loginInput.isEmpty()) {
            login.error = resources.getString(R.string.field_cant_be_empty)
            return false
        } else if (loginInput.length > 32 || loginInput.length < 4) {
            login.error = resources.getString(R.string.login_length_error)
            return false
        } else if (!loginPattern.matcher(loginInput).matches()) {
            login.error = resources.getString(R.string.incorrect_login_format)
        }
        return true
    }

    private fun validatePassword(): Boolean {
        val passwordInput = binding.registerEtPassword.text.toString().trim()
        val password = binding.registerEtPassword
        return if (passwordInput.length > 500 || passwordInput.length < 8) {
            password.error = resources.getString(R.string.password_length_error)
            false
        } else true
    }

    private fun confirmPassword(): Boolean {
        return if (binding.registerEtPassword.text.toString() != binding.registerEtPasswordRepeat.text.toString()) {
            binding.registerEtPasswordRepeat.error =
                resources.getString(R.string.incorrect_password_repeat)
            false
        } else true
    }

}