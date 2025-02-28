package com.metadigi.dewisriapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.GeolocationPermissions
import android.webkit.PermissionRequest
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.metadigi.dewisriapp.permission.PermissionManager
import com.metadigi.dewisriapp.utils.AppUtils
import com.metadigi.dewisriapp.utils.AsynchronousUtils
import com.metadigi.dewisriapp.utils.NetworkUtils
import com.metadigi.dewisriapp.webview.WebViewManager

class MainActivity : AppCompatActivity() {
    private lateinit var webView:WebView
    private var alertDialog: AlertDialog? = null
    private var isPermissionRequestLaunched = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        webView = findViewById(R.id.mainWebView)
        configureBackPressHandling()
        monitorInternetAndLoadPage()
        setupWebView()
        setupChromeClient()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupWebView() {
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        webView.settings.userAgentString = "DewiSriApp"
    }

    private fun setupChromeClient() {
        webView.webChromeClient = object : WebChromeClient() {
            override fun onPermissionRequest(request: PermissionRequest) {
                if(PermissionManager.hasAllPermissions(this@MainActivity)) {
                    runOnUiThread {
                        request.grant(request.resources)
                    }
                }
            }

            override fun onGeolocationPermissionsShowPrompt(
                origin: String?,
                callback: GeolocationPermissions.Callback?
            ) {
                callback?.invoke(origin, true, false)
            }
        }
    }

    private fun showPermissionDialogIfNeeded() {
        if(!PermissionManager.hasAllPermissions(this) && NetworkUtils.isInternetAvailable()) {
            showPermissionExplanationDialog()
        }
    }

    private fun monitorInternetAndLoadPage() {
        AsynchronousUtils.startInterval(1000L) {
            if((alertDialog == null || alertDialog?.isShowing == false) && !isPermissionRequestLaunched){
                showPermissionDialogIfNeeded()
            }
            WebViewManager.checkAndLoadPage(webView)
        }
    }

    private fun configureBackPressHandling() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if(webView.canGoBack()) {
                    webView.goBack()
                } else {
                    finish()
                }
            }
        })
    }

    private val requestPermissionsLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val locationGranted = permissions[android.Manifest.permission.ACCESS_FINE_LOCATION] ?: false
            val cameraGranted = permissions[android.Manifest.permission.CAMERA] ?: false

            isPermissionRequestLaunched = false
            if (locationGranted && cameraGranted) {
                Toast.makeText(this, "All permissions granted!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Some permissions denied!", Toast.LENGTH_SHORT).show()
            }
        }

    private fun showPermissionExplanationDialog() {
        alertDialog = AlertDialog.Builder(this)
            .setTitle("Permissions Required")
            .setMessage(
                "Location & Camera access is required for gameplay. You can grant permissions in Settings."
            )
            .setPositiveButton("OK") { _, _ ->
                isPermissionRequestLaunched = true
                requestPermissionsLauncher.launch(
                    arrayOf(
                        android.Manifest.permission.CAMERA,
                        android.Manifest.permission.ACCESS_FINE_LOCATION
                    )
                )
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
                Toast.makeText(this, "Permissions are required for this app to function.", Toast.LENGTH_SHORT).show()
                AppUtils.exitApp(this)
            }
            .show()
    }
}