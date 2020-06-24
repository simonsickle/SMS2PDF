package com.simonsickle.sms2pdf.fragment

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.simonsickle.sms2pdf.R

class PermissionRequestFragment : Fragment(R.layout.fragment_get_permission) {
    private val PERMISSION_REQUEST_CODE = 1

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_SMS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(arrayOf(Manifest.permission.READ_SMS), PERMISSION_REQUEST_CODE)
        } else {
            findNavController().navigate(PermissionRequestFragmentDirections.goToList())
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        grantResults.forEach {
            if (it != PackageManager.PERMISSION_GRANTED) {
                Log.e("PermissionResult", "Didn't get permissions")
                return
            }
        }
        findNavController().navigate(PermissionRequestFragmentDirections.goToList())
    }
}