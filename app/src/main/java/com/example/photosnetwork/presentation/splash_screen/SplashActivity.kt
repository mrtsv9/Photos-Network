package com.example.photosnetwork.presentation.splash_screen

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.example.photosnetwork.R
import com.example.photosnetwork.data.local.dao.AppDao
import com.example.photosnetwork.databinding.ActivitySplashBinding
import com.example.photosnetwork.presentation.main.MainActivity
import com.example.photosnetwork.presentation.splash_screen.adapter.ViewPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    private var _binding: ActivitySplashBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var dao: AppDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_SplashScreen)
        _binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycleScope.launch {
            val user = dao.getUser()
            if (user != null) Intent(applicationContext,
                MainActivity::class.java).apply { startActivity(this) }
        }

        val adapter = ViewPagerAdapter(supportFragmentManager, lifecycle)
        binding.viewPager.adapter = adapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = resources.getString(R.string.login)
                1 -> tab.text = resources.getString(R.string.register)
            }
        }.attach()

    }

}