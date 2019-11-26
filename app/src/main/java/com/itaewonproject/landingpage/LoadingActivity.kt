package com.itaewonproject.landingpage

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PowerManager
import android.provider.Settings
import android.text.Html
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.itaewonproject.JsonParser
import com.itaewonproject.R
import com.itaewonproject.Routepang
import com.itaewonproject.linkshare.ClipboardListener
import com.itaewonproject.mainservice.MainActivity
import com.itaewonproject.model.sender.Customer
import com.itaewonproject.rests.authorization
import java.lang.Exception

class LoadingActivity : AppCompatActivity() {

    private lateinit var slogan:TextView
    lateinit var clipboard: ClipboardManager
    private lateinit var sharedPreferences: SharedPreferences

    private var isAutoLogin = false

    private lateinit var serviceIntent: Intent
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)
        //getHashKey(this)
        slogan = findViewById<TextView>(R.id.text_slogan)
        sharedPreferences = getSharedPreferences("autoLogin", Context.MODE_PRIVATE)
        slogan.text= Html.fromHtml("원하는 정보만 <b>루팡!</b><br>나만의 루트가 <b>팡팡!</b>",Html.FROM_HTML_MODE_LEGACY)

        isAutoLogin = sharedPreferences.getBoolean("autoLoginCheck",false)

        if(isAutoLogin){
            try{
                val token = sharedPreferences.getString("loginToken","")
                val customer = JsonParser().objectJsonParsing(sharedPreferences.getString("autoLoginCustomer","{}")!!,Customer::class.java)
                authorization = token!!
                (application as Routepang).token = token
                (application as Routepang).customer = customer!!
                Log.i("autoLoginMode","auto Login as ${customer.customerId}")
                Log.i("autoLoginMode","auto Login as $token")
                if(token=="") throw NullPointerException()
                if (customer.account=="") throw java.lang.NullPointerException()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.translate_in_from_right,R.anim.translate_out_to_left)
                finish()
            }catch (e:Exception){
                val editor = sharedPreferences.edit()
                editor.putString("loginToken", "")
                editor.putString("autoLoginCustomer", "")
                editor.putBoolean("autoLoginCheck",false)
                editor.apply()
                Toast.makeText(this,"자동 로그인에 실패했습니다. 다시 로그인해주세요.",Toast.LENGTH_SHORT).show()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.translate_in_from_right,R.anim.translate_out_to_left)
                finish()
            }

        }

        getPermissions()
    }

    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(Build.VERSION_CODES.M)
    private fun getPermissions() {
        val pm = applicationContext.getSystemService(Context.POWER_SERVICE) as PowerManager
        var isWhiteListing = false
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            isWhiteListing = pm.isIgnoringBatteryOptimizations(applicationContext.packageName)
        }
        if (!isWhiteListing) {
            val intent = Intent()
            intent.action = Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
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

    private val REQUEST_CODE = 10101

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
            if(!isAutoLogin){
                val intent = Intent(this,LoginActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.translate_in_from_right,R.anim.translate_out_to_left)
                finish()
            }

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
