package com.itaewonproject.setting

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.itaewonproject.R
import com.itaewonproject.landingpage.LoginActivity

class SettingActivity : AppCompatActivity(){

    private lateinit var buttonBack:ImageView
    lateinit var con:Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        buttonBack = findViewById<ImageView>(R.id.button_back)
        con = this
        buttonBack.setOnClickListener { finish()}
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.settings, SettingsFragment())
            .commit()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.setting_preferences, rootKey)
        }

        override fun onPreferenceTreeClick(preference: Preference?): Boolean {
            if(preference!=null) {
                Log.i("!@",preference.key)

                when(preference.key){
                    "account_myInfo" ->{
                        Log.i("!@","info clicked")
                        val intent = Intent(activity,MyInfoActivity::class.java)
                        startActivity(intent)
                    }
                    "service_qna"->{

                    }
                    "service_privateData"->{

                    }
                    "service_openSource"->{

                        val sharedPreferences = activity!!.getSharedPreferences("autoLogin", MODE_PRIVATE)
                        val editor = sharedPreferences.edit()
                        editor.putBoolean("autoLoginCheck",false)
                        editor.putString("loginToken", null)
                        editor.putString("autoLoginCustomer",null)
                        editor.apply()
                        val intent = Intent(context, LoginActivity::class.java)
                        startActivity(intent)
                        activity!!.finish()
                    }
                }
            }

            return super.onPreferenceTreeClick(preference)
        }
    }
}
