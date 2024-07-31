package com.example.letterboxn.testplayground

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.Settings
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.example.letterboxn.common.base.BaseFragment
import com.example.letterboxn.databinding.FragmentCameraBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class CameraFragment : BaseFragment<FragmentCameraBinding>(FragmentCameraBinding::inflate) {

    private val cameraPermissionRequestCode = 100
    private val maxDeniedCount = 3

    private lateinit var takePictureLauncher: ActivityResultLauncher<Void?>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        takePictureLauncher =
            registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
                bitmap?.let {
                    binding.imageView13.setImageBitmap(it)
                }
            }
    }

    override fun onViewCreatedLight() {
        binding.button.setOnClickListener {
            handleCameraButtonClick()
        }
    }

    private fun handleCameraButtonClick() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            launchCamera()
        } else {
            requestCameraPermission()
        }
    }

    private fun launchCamera() {
        takePictureLauncher.launch(null)
    }

    private fun requestCameraPermission() {
        requestPermissions(arrayOf(Manifest.permission.CAMERA), cameraPermissionRequestCode)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == cameraPermissionRequestCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                launchCamera()
                resetDeniedCount()
            } else {
                incrementDeniedCount()
                if (getDeniedCount() >= maxDeniedCount) {
                    showPermissionSettingsDialog()
                }
            }
        }
    }

    private fun incrementDeniedCount() {
        val prefs = requireContext().getSharedPreferences("AppPrefs", 0)
        val deniedCount = prefs.getInt("denied_count", 0) + 1
        prefs.edit().putInt("denied_count", deniedCount).apply()
    }

    private fun getDeniedCount(): Int {
        val prefs = requireContext().getSharedPreferences("AppPrefs", 0)
        return prefs.getInt("denied_count", 0)
    }

    private fun resetDeniedCount() {
        requireContext().getSharedPreferences("AppPrefs", 0).edit().putInt("denied_count", 0)
            .apply()
    }

    private fun showPermissionSettingsDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Permission Required")
            .setMessage("You have denied camera permission several times. Please allow permission in the app settings to use this feature.")
            .setPositiveButton("Go to Settings") { _, _ ->
                openAppSettings()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun openAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = android.net.Uri.fromParts("package", requireContext().packageName, null)
        intent.data = uri
        startActivity(intent)
    }

    override fun observeChanges() {

    }
}