package com.metadigi.dewisriapp.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.core.app.ActivityCompat
import kotlin.system.exitProcess

object AppUtils {
    fun exitApp(activity: Activity) {
        ActivityCompat.finishAffinity(activity)
        exitProcess(0)
    }
}