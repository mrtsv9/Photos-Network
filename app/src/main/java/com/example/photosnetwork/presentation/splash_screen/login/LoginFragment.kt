package com.example.photosnetwork.presentation.splash_screen.login

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.photosnetwork.R
import com.example.photosnetwork.databinding.FragmentLoginBinding
import com.example.photosnetwork.domain.model.auth.UserAuthInput
import com.example.photosnetwork.presentation.base.BaseFragment
import com.example.photosnetwork.presentation.main.MainActivity
import com.example.photosnetwork.util.Constants.TAG
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>() {

    override var hostActivity: Activity?
        get() = this.activity
        set(value) {}

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentLoginBinding
        get() = FragmentLoginBinding::inflate

    private val viewModel by viewModels<LoginViewModel>()

    override fun setup() {
        initObservers()
    }

    private fun initObservers() {
        binding.btnLogin.setOnClickListener {
            logInUser()
        }
        lifecycleScope.launchWhenStarted {
            viewModel.isErrorOccurred.collectLatest {
                toast(resources.getString(R.string.login_error_response))
            }
        }
        lifecycleScope.launchWhenStarted {
            viewModel.userAuthData.collectLatest {
                Intent(hostActivity, MainActivity::class.java).apply { startActivity(this) }
            }
        }
    }

    private fun logInUser() {
        val loginInput = binding.loginEtLogin.text?.trim().toString()
        val passwordInput = binding.loginEtPassword.text?.trim().toString()
        viewModel.logInUser(UserAuthInput(loginInput, passwordInput))
    }

}