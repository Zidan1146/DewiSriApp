package com.metadigi.dewisriapp.utils

object NetworkUtils {
    fun isInternetAvailable():Boolean {
        return try {
            val process = Runtime.getRuntime().exec("ping -c 1 google.com")
            val exitCode = process.waitFor()
            exitCode == 0
        } catch (e: Exception) {
            false
        }
    }
}
