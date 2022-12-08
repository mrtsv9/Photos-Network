package com.example.photosnetwork.presentation.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.photosnetwork.R
import com.example.photosnetwork.data.local.dao.AppDao
import com.example.photosnetwork.databinding.ActivityMainBinding
import com.example.photosnetwork.presentation.splash_screen.SplashActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var toggle: ActionBarDrawerToggle

    @Inject
    lateinit var dao: AppDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initNavigation()
        bindUsername()
    }

    private fun initNavigation() {
        toggle = ActionBarDrawerToggle(this, binding.drawer, R.string.open, R.string.close)
        binding.drawer.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.container) as NavHostFragment
        val navController: NavController = navHostFragment.navController
        binding.navView.setupWithNavController(navController)
    }

    private fun bindUsername() {
        val headerView = binding.navView.getHeaderView(0)
        val userName: TextView = headerView.findViewById(R.id.header_username)

        lifecycleScope.launch {
            val user = dao.getUser()
            withContext(Dispatchers.Main) {
                userName.text = user?.login
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) return true
        return super.onOptionsItemSelected(item)
    }

    private fun logoutUser() {
        lifecycleScope.launch {
            dao.deleteUser()
            Intent(applicationContext, SplashActivity::class.java).apply { startActivity(this) }
        }
    }
}