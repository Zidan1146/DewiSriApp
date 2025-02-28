package com.metadigi.dewisriapp.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

object AsynchronousUtils {
    fun startInterval(intervalMillis: Long, action: () -> Unit): Job {
        return CoroutineScope(Dispatchers.Main).launch {
            while (isActive) {
                action()
                delay(intervalMillis)
            }
        }
    }
}
