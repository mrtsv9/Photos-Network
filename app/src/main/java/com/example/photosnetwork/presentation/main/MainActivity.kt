package com.example.photosnetwork.presentation.main

import android.Manifest
import android.app.Activity
import android.content.*
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.MenuItem
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.photosnetwork.R
import com.example.photosnetwork.data.local.dao.auth.UserDao
import com.example.photosnetwork.databinding.ActivityMainBinding
import com.example.photosnetwork.presentation.splash_screen.SplashActivity
import com.example.photosnetwork.util.Constants.CAMERA_REQUEST_CODE
import com.example.photosnetwork.util.Constants.TAG
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val PERMISSION_CODE = 1000
    private val IMAGE_CAPTURE_CODE = 1001

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var toggle: ActionBarDrawerToggle

    @Inject
    lateinit var dao: UserDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initNavigation()
        bindUsername()
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

    private fun bindUsername() {
        val headerView = binding.navView.getHeaderView(0)
        val userName: TextView = headerView.findViewById(R.id.header_username)

        lifecycleScope.launch {
            val user = dao.getUser()
            if (user == null) logoutUser()
            withContext(Dispatchers.Main) {
                userName.text = user?.login
            }
        }
    }

    private fun cameraAction() {
        binding.fabAddPhoto.setOnClickListener {
            openCamera()
        }
    }

    private fun openCamera() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "New Picture")
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera")

        //camera intent
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        // set filename
        val timeStamp = SimpleDateFormat("dd-MM-yyyy").format(Date())
//        vFilename = "FOTO_" + timeStamp + ".jpg"

        // set direcory folder
//        val file = File("/sdcard/niabsen/", vFilename);
//        val image_uri = FileProvider.getUriForFile(this,
//            this.getApplicationContext().getPackageName() + ".provider",
//            file);

//        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri)
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        //called when user presses ALLOW or DENY from Permission Request Popup
        when (requestCode) {
            PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED
                ) {
                    //permission from popup was granted
                    openCamera()
                } else {
                    //permission from popup was denied
//                    toast("Permission denied")
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            val bitmap = data?.extras?.get("data") as Bitmap
            Log.d(TAG, "onActivityResult: $bitmap")
            //File object of camera image
//            val file = File("/sdcard/niabsen/", vFilename);
//            longToast(file.toString())

            //Uri of camera image
//            val uri = FileProvider.getUriForFile(this,
//                this.getApplicationContext().getPackageName() + ".provider",
//                file);
//            myImageView.setImageURI(uri)
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

    private fun showLogoutDialog() {
        this.let {
            AlertDialog.Builder(it)
                .setMessage(resources.getString(R.string.ask_logout))
                .setPositiveButton(resources.getString(R.string.log_out)) { _, _ ->
                    logoutUser()
                }
                .setNegativeButton(resources.getString(R.string.close)) { dialog, _ ->
                    dialog.dismiss()
                }.show()
        }
    }
}