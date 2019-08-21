package com.itaewonproject.B_Landing

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.itaewonproject.A.LocationSelectActivity
import com.itaewonproject.B_Mypage.MypageActivity
import android.widget.Toast
import com.itaewonproject.A.RealService
import android.os.PowerManager
import android.net.Uri
import com.itaewonproject.R
import android.provider.Settings.canDrawOverlays
import android.provider.Settings.ACTION_MANAGE_OVERLAY_PERMISSION
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.os.Build
import android.provider.Settings
import androidx.annotation.RequiresApi


class LandingPageActivity : AppCompatActivity() {

    lateinit var buttonSearch:Button
    lateinit var buttonMypage:Button
    lateinit var serviceIntent: Intent


    @SuppressLint("NewApi")
    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing_page)
        buttonSearch = findViewById(R.id.but_search)
        buttonMypage = findViewById(R.id.but_mypage)
        buttonMypage.setOnClickListener({
            var intent = Intent(this, MypageActivity::class.java)
            startActivity(intent)
        })
        buttonSearch.setOnClickListener({
            var intent = Intent(this, LocationSelectActivity::class.java)
            startActivity(intent)
        })

        val pm = applicationContext.getSystemService(Context.POWER_SERVICE) as PowerManager
        var isWhiteListing = false
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            isWhiteListing = pm.isIgnoringBatteryOptimizations(applicationContext.packageName)
        }
        if (!isWhiteListing) {
            val intent = Intent()
            intent.action = android.provider.Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
            intent.data = Uri.parse("package:" + applicationContext.packageName)
            startActivity(intent)
        }
        checkDrawOverlayPermission()
        if (RealService.serviceIntent == null) {
            serviceIntent = Intent(this, RealService::class.java)
            startService(serviceIntent)
        } else {
            serviceIntent = RealService.serviceIntent!!//getInstance().getApplication();
            Toast.makeText(applicationContext, "already", Toast.LENGTH_LONG).show()
        }

    }

    val REQUEST_CODE = 10101

    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(Build.VERSION_CODES.M)
    fun checkDrawOverlayPermission() {

        // Checks if app already has permission to draw overlays
        if (!Settings.canDrawOverlays(this)) {

            // If not, form up an Intent to launch the permission request
            val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName"))

            // Launch Intent, with the supplied request code
            startActivityForResult(intent, REQUEST_CODE)
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("MissingSuperCall", "NewApi")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE) {
            if (Settings.canDrawOverlays(this)) {

            } else {

                Toast.makeText(this, "Sorry. Can't draw overlays without permission...", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
