package com.itaewonproject.B_Landing

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PowerManager
import android.provider.Settings
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.itaewonproject.A.RealService
import com.itaewonproject.B_Mypage.MypageActivity
import com.itaewonproject.R

class LoginActivity : AppCompatActivity() {

    lateinit var editID:EditText
    lateinit var editPW:EditText
    lateinit var buttonLogin:Button
    lateinit var buttonLoginKaKao:Button
    lateinit var buttonSignin:Button
    lateinit var checkAutoLogin:CheckBox
    lateinit var clipboard:ClipboardManager

    lateinit var serviceIntent: Intent

    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        editID = findViewById(R.id.edit_id) as EditText
        editPW=findViewById(R.id.edit_password) as EditText
        buttonLogin = findViewById(R.id.button_login) as Button
        buttonLoginKaKao =findViewById(R.id.button_create_kakao) as Button
        buttonSignin = findViewById(R.id.button_signin) as Button
        buttonLogin.setOnClickListener({
            var intent = Intent(this,MypageActivity::class.java)
            startActivity(intent)
            finish()
        })

        buttonSignin.setOnClickListener({
            var intent = Intent(this,SignInActivity::class.java)
            startActivity(intent)
        })


       getPermissions()
    }

    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(Build.VERSION_CODES.M)
    private fun getPermissions(){
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
