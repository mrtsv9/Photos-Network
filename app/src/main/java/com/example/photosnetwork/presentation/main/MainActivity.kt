package com.example.photosnetwork.presentation.main

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.*
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.provider.MediaStore
import android.provider.Settings
import android.util.Base64
import android.util.Log
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
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
import com.example.photosnetwork.data.local.dao.auth.UserDao
import com.example.photosnetwork.databinding.ActivityMainBinding
import com.example.photosnetwork.domain.model.image.PostImageItem
import com.example.photosnetwork.presentation.splash_screen.SplashActivity
import com.example.photosnetwork.util.Constants.CAMERA_REQUEST_CODE
import com.example.photosnetwork.util.Constants.TAG
import com.google.android.gms.location.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var toggle: ActionBarDrawerToggle

    val PERMISSION_ID = 42
    lateinit var mFusedLocationClient: FusedLocationProviderClient
    var currentLocation: Location? = null

    @Inject
    lateinit var dao: UserDao

    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)


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

    private fun initObservers() {
        val headerView = binding.navView.getHeaderView(0)
        val userName: TextView = headerView.findViewById(R.id.header_username)

        lifecycleScope.launch {
            val user = dao.getUser()
            if (user == null) logoutUser()
            withContext(Dispatchers.Main) {
                userName.text = user?.login
            }
        }

        lifecycleScope.launch {
            viewModel.photos.collectLatest {
                Log.d(TAG, "initObservers: $it")
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
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, CAMERA_REQUEST_CODE)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                CAMERA_REQUEST_CODE -> {
                    val bitmap = data?.extras?.get("data") as Bitmap
                    postImage(bitmap)
                }
            }
        }
    }

    private fun postImage(bitmap: Bitmap) {
        AlertDialog.Builder(this).setMessage(R.string.request_post_image)
            .setPositiveButton(R.string.yes) { _, _ ->
                val encodedImage = encodeImage(bitmap)
//                getLastLocation()
                val location = currentLocation
                val postImageItem = encodedImage?.let {
                    PostImageItem(it,
                        Date().time / 1000,
                        location?.latitude ?: 40.0,
                        location?.longitude ?: 40.0)
                }
                postImageItem?.let { viewModel.postImage(it) }
            }.setNegativeButton(resources.getString(R.string.close)) { dialog, _ ->
                dialog.dismiss()
            }.show()
    }

//    @SuppressLint("MissingPermission")
//    private fun getLastLocation() {
//        if (checkPermissions()) {
//            if (isLocationEnabled()) {
//                mFusedLocationClient.lastLocation.addOnCompleteListener(this) { task ->
//                    val location: Location? = task.result
//                    if (location == null) {
//                        requestNewLocationData()
//                    } else {
//                        currentLocation = location
//                    }
//                }
//            } else {
//                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show()
//                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
//                startActivity(intent)
//            }
//        } else {
//            requestPermissions()
//        }
//    }
//
//    @SuppressLint("MissingPermission")
//    private fun requestNewLocationData() {
//        val mLocationRequest = LocationRequest()
//        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
//        mLocationRequest.interval = 0
//        mLocationRequest.fastestInterval = 0
//        mLocationRequest.numUpdates = 1
//
//        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
//        mFusedLocationClient.requestLocationUpdates(
//            mLocationRequest, mLocationCallback,
//            Looper.myLooper()
//        )
//    }
//
//    private val mLocationCallback = object : LocationCallback() {
//        override fun onLocationResult(locationResult: LocationResult) {
//            val lastLocation: Location? = locationResult.lastLocation
//            if (lastLocation != null) {
//                currentLocation = lastLocation
//            }
//        }
//    }
//
//    private fun isLocationEnabled(): Boolean {
//        val locationManager: LocationManager =
//            getSystemService(Context.LOCATION_SERVICE) as LocationManager
//        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
//            LocationManager.NETWORK_PROVIDER
//        )
//    }
//
//    private fun checkPermissions(): Boolean {
//        if (ActivityCompat.checkSelfPermission(
//                this,
//                Manifest.permission.ACCESS_COARSE_LOCATION
//            ) == PackageManager.PERMISSION_GRANTED &&
//            ActivityCompat.checkSelfPermission(
//                this,
//                Manifest.permission.ACCESS_FINE_LOCATION
//            ) == PackageManager.PERMISSION_GRANTED
//        ) {
//            return true
//        }
//        return false
//    }
//
//    private fun requestPermissions() {
//        ActivityCompat.requestPermissions(
//            this,
//            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION,
//                Manifest.permission.ACCESS_FINE_LOCATION),
//            PERMISSION_ID
//        )
//    }
//
//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<String>,
//        grantResults: IntArray,
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        if (requestCode == PERMISSION_ID) {
//            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
//                getLastLocation()
//            }
//        }
//    }

    private fun encodeImage(bitmap: Bitmap): String? {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos)
        val bytes = baos.toByteArray()
        return Base64.encodeToString(bytes, Base64.DEFAULT)
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