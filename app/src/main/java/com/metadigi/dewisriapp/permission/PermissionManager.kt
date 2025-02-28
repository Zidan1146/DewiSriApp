package com.metadigi.dewisriapp.permission

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

object PermissionManager {
    private fun hasPermission(context: Context, permission: String): Boolean {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
    }

    fun hasAllPermissions(context: Context): Boolean {
        val isCameraPermissionGranted = hasPermission(context, Manifest.permission.CAMERA)
        val isLocationPermissionGranted =
            hasPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) ||
                    hasPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)

        if (!isCameraPermissionGranted) {
            return false
        }

        if(!isLocationPermissionGranted) {
            return false
        }
        return true
    }
}