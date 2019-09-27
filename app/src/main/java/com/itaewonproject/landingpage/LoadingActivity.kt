package com.itaewonproject.landingpage

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PowerManager
import android.provider.Settings
import android.text.Html
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.itaewonproject.R
import com.itaewonproject.linkshare.ClipboardListener

class LoadingActivity : AppCompatActivity() {

    private lateinit var slogan:TextView
    lateinit var clipboard: ClipboardManager

    lateinit var serviceIntent: Intent
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)
        slogan = findViewById(R.id.text_slogan) as TextView
        slogan.paintFlags=slogan.paintFlags or Paint.FAKE_BOLD_TEXT_FLAG
        slogan.text= Html.fromHtml("원하는 정보만 <b>루팡!</b><br>나만의 루트가 <b>팡팡!</b>")

        getPermissions()
    }

    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(Build.VERSION_CODES.M)
    private fun getPermissions() {
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
        if (ClipboardListener.serviceIntent == null) {
            serviceIntent = Intent(this, ClipboardListener::class.java)
            startService(serviceIntent)
        } else {
            serviceIntent = ClipboardListener.serviceIntent!! // getInstance().getApplication();
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
        }else
        {
            val intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("MissingSuperCall", "NewApi")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE) {
            if (Settings.canDrawOverlays(this)) {
                val intent = Intent(this,LoginActivity::class.java)
                startActivity(intent)
                finish()
            } else {

                Toast.makeText(this, "이 권한 없이 서비스 실행이 불가합니다", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
