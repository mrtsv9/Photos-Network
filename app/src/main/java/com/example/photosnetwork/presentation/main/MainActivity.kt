package com.example.photosnetwork.presentation.main

import android.annotation.SuppressLint
import android.content.*
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Base64
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.photosnetwork.R
import com.example.photosnetwork.databinding.ActivityMainBinding
import com.example.photosnetwork.domain.model.image.PostImageItem
import com.example.photosnetwork.presentation.splash_screen.SplashActivity
import com.google.android.gms.location.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.util.*


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var toggle: ActionBarDrawerToggle

    private var currentLocation: Location? = null
    lateinit var fusedLocationClient: FusedLocationProviderClient

    private val viewModel by viewModels<MainViewModel>()

    private val takePhotoResult =
        registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
            if (bitmap != null) {
                postImage(bitmap)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        getLocation()
        initNavigation()
        initObservers()
        cameraAction()
    }

    private fun initNavigation() {
        toggle = ActionBarDrawerToggle(this, binding.drawer, R.string.open, R.string.close)
        binding.drawer.addDrawerListener(toggle)
        setSupportActionBar(binding.toolbar)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.btnLogout.setOnClickListener {
            showLogoutDialog()
        }

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.container) as NavHostFragment
        val navController: NavController = navHostFragment.navController
        binding.navView.setupWithNavController(navController)
    }

    fun hideFab() {
        binding.fabAddPhoto.visibility = View.GONE
    }

    fun showFab() {
        binding.fabAddPhoto.visibility = View.VISIBLE
    }

    private fun initObservers() {
        val headerView = binding.navView.getHeaderView(0)
        val userName: TextView = headerView.findViewById(R.id.header_username)

        lifecycleScope.launch {
            viewModel.user.collectLatest { user ->
                if (user == null) logoutUser()
                withContext(Dispatchers.Main) {
                    userName.text = user?.login
                }
            }
        }
        viewModel.getUser()

        lifecycleScope.launch {
            viewModel.photoResponseMessage.collectLatest {
                withContext(Dispatchers.Main) {
                    if (it.isFailure) {
                        Toast.makeText(
                            applicationContext,
                            it.exceptionOrNull().toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            applicationContext,
                            resources.getString(R.string.photo_added),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    private fun cameraAction() {
        binding.fabAddPhoto.setOnClickListener {
            try {
                camera()
            } catch (e: Exception) {
                showRotationalDialogForPermission()
            }
        }
    }

    private fun camera() {
        takePhotoResult.launch()
    }

    private fun showRotationalDialogForPermission() {
        this.let {
            AlertDialog.Builder(it)
                .setMessage(resources.getString(R.string.no_required_permissions))
                .setPositiveButton(resources.getString(R.string.open_settings)) { _, _ ->
                    try {
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        val uri = Uri.fromParts("package", packageName, null)
                        intent.data = uri
                        startActivity(intent)
                    } catch (e: ActivityNotFoundException) {
                        e.printStackTrace()
                    }
                }.setNegativeButton(resources.getString(R.string.cancel)) { dialog, _ ->
                    dialog.dismiss()
                }.show()
        }
    }

    private fun postImage(bitmap: Bitmap) {
        AlertDialog.Builder(this).setMessage(R.string.request_post_image)
            .setPositiveButton(R.string.yes) { _, _ ->
                val encodedImage = encodeImage(bitmap)
                val location = currentLocation
                val postImageItem = encodedImage?.let {
                    PostImageItem(
                        it,
                        Date().time / 1000,
                        location?.latitude ?: 40.0,
                        location?.longitude ?: 40.0
                    )
                }
                postImageItem?.let { viewModel.postImage(it) }
            }.setNegativeButton(resources.getString(R.string.close)) { dialog, _ ->
                dialog.dismiss()
            }.show()
    }

    @SuppressLint("MissingPermission") // It is actually not missing
    private fun getLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                100
            )
            return
        }
        val location = fusedLocationClient.lastLocation
        location.addOnSuccessListener {
            if (it != null) {
                currentLocation = it
            }
        }
    }

    private fun encodeImage(bitmap: Bitmap): String? {
        return try {
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos)
            val bytes = baos.toByteArray()
            Base64.encodeToString(bytes, Base64.DEFAULT)
        } catch (e: java.lang.RuntimeException) {
            null
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) return true
        return super.onOptionsItemSelected(item)
    }

    private fun logoutUser() {
        lifecycleScope.launch {
            viewModel.deleteUser()
            Intent(this@MainActivity, SplashActivity::class.java).apply { startActivity(this) }
        }
    }

    private fun showLogoutDialog() {
        this.let {
            AlertDialog.Builder(it).setMessage(resources.getString(R.string.ask_logout))
                .setPositiveButton(resources.getString(R.string.log_out)) { _, _ ->
                    logoutUser()
                }.setNegativeButton(resources.getString(R.string.close)) { dialog, _ ->
                    dialog.dismiss()
                }.show()
        }
    }
}