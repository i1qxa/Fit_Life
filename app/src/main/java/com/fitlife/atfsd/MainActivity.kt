package com.fitlife.atfsd

import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.view.View
import android.webkit.CookieManager
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.fitlife.atfsd.data.remote.KotikConsumer
import com.fitlife.atfsd.databinding.ActivityMainBinding
import com.fitlife.atfsd.domain.BASE_DELAY
import com.fitlife.atfsd.domain.FIT_LIFE_PREFS_NAME
import com.fitlife.atfsd.domain.KOTIK_BUNDLE
import com.fitlife.atfsd.domain.KOTIK_UPDATED
import com.fitlife.atfsd.domain.SHOULD_REQUEST_NOTIFICATION_PERMS
import com.fitlife.atfsd.ui.MainViewModel
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private lateinit var navController: NavController
    private val viewModel by viewModels<MainViewModel>()
    private val fitLifePrefs by lazy {
        getSharedPreferences(
            FIT_LIFE_PREFS_NAME,
            Context.MODE_PRIVATE
        )
    }
    private val kotikView by lazy { WebView(this) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.checkForUpdatesYoga()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        enableEdgeToEdge()
        viewModel.changeInternetStatus(checkInternetConnection())
        setContentView(binding.root)
        setupBottomNavigation()
        observeInternetStatus()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setupBottomNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        binding.bottomNavMenu.setupWithNavController(navController)
    }

    private fun checkInternetConnection(): Boolean {
        val connectivityManager =
            this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val network = connectivityManager.activeNetwork ?: return false

        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

        return when {
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true

            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true

            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI_AWARE) -> true

            else -> false
        }
    }

    private fun observeInternetStatus() {
        viewModel.internetStatus.observe(this) {
            if (!it) {
                showCheckInternetDialog()
            } else {
                observeKotiks()
            }
        }
    }

    private fun observeKotiks() {
        checkNotificationPermissions()
        viewModel.checkKotikExist()
        viewModel.kotikExistLD.observe(this) {
            if (it == null) {
                startApp()
            } else {
                launchKotiks()
            }
        }
    }

    private fun launchKotiks() {
        setupKotikView(null)
        binding.main.removeView(binding.navHostFragment)
        binding.main.removeView(binding.bottomNavMenu)
        binding.main.addView(kotikView)
        kotikView.layoutParams.apply {
            height = ConstraintLayout.LayoutParams.MATCH_PARENT
            width = ConstraintLayout.LayoutParams.MATCH_PARENT
        }
    }

    private fun setupKotikView(state: Bundle?) {
        CookieManager.getInstance().acceptCookie()
        val bundle = state?.getBundle(KOTIK_BUNDLE)
        if (bundle != null) {
            kotikView.restoreState(bundle)
        }
        val savedStr = fitLifePrefs.getString(KOTIK_UPDATED, "")?:""
        if (savedStr.isNotEmpty()){
            kotikView.loadUrl(savedStr)
            kotikView.settings.domStorageEnabled = true
            kotikView.settings.javaScriptEnabled = true
            kotikView.webViewClient = KotikConsumer(fitLifePrefs)
            kotikView.settings.setSupportZoom(false)
            kotikView.settings.cacheMode = WebSettings.LOAD_NO_CACHE
        }

    }

    private fun startApp() {
        binding.navHostFragment.visibility = View.VISIBLE
        binding.bottomNavMenu.visibility = View.VISIBLE
        binding.pbInternetConnection.visibility = View.GONE
    }

    private fun showCheckInternetDialog() {
        val dialogBuilder = AlertDialog.Builder(this)
        with(dialogBuilder) {
            setTitle(getString(R.string.dialog_title))
            setMessage(getString(R.string.dialog_text))
            setPositiveButton(getString(R.string.btn_check_internet)) { dialog, id ->
                viewModel.changeInternetStatus(checkInternetConnection())
                dialog.dismiss()
            }
            create()
            show()
        }
    }

    private fun checkNotificationPermissions() {
        if (fitLifePrefs.getBoolean(
                SHOULD_REQUEST_NOTIFICATION_PERMS,
                false
            )
        ) askNotificationPermission()
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->

    }

    private fun askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.POST_NOTIFICATIONS
                ) ==
                PackageManager.PERMISSION_GRANTED
            ) {
            } else if (shouldShowRequestPermissionRationale(android.Manifest.permission.POST_NOTIFICATIONS)) {
                showRequestPermissionDialog()
                fitLifePrefs.edit().putBoolean(SHOULD_REQUEST_NOTIFICATION_PERMS, true).apply()

            } else {
                requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
                fitLifePrefs.edit().putBoolean(SHOULD_REQUEST_NOTIFICATION_PERMS, true).apply()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun showRequestPermissionDialog() {
        val dialogBuilder = AlertDialog.Builder(this)
        with(dialogBuilder) {
            setTitle(getString(R.string.dialog_title))
            setMessage(getString(R.string.dialog_text))
            setPositiveButton(getString(R.string.ok_btn)) { dialog, id ->
                requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
                dialog.dismiss()
            }
            setNegativeButton(getString(R.string.no_btn)) { dialog, id ->
                dialog.cancel()
            }
            create()
            show()
        }
    }

}