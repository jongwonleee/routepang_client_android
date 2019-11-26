package com.itaewonproject.setting

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.google.gson.Gson
import com.itaewonproject.R
import com.itaewonproject.Routepang
import com.itaewonproject.landingpage.LoginActivity

class MyInfoActivity : AppCompatActivity() {

    private lateinit var buttonBack:ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_info)
        buttonBack = findViewById<ImageView>(R.id.button_back)
        buttonBack.setOnClickListener { finish()}
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.settings, SettingsFragment())
            .commit()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        finish()
    }

    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.my_info_preferences, rootKey)
        }

        override fun onPreferenceTreeClick(preference: Preference?): Boolean {
            if(preference!=null) {
                when(preference.key){
                    "secure_quit" ->{
                        val intent = Intent(context,QuitActivity::class.java)
                        startActivity(intent)
                    }
                    "service_qna"->{

                    }
                    "service_privateData"->{

                    }
                    "service_openSource"->{

                    }
                    "secure_logout"->{
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
