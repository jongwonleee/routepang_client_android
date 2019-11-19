package com.itaewonproject.setting

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.itaewonproject.R

class SettingActivity : AppCompatActivity(){

    private lateinit var buttonBack:ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        buttonBack = findViewById<ImageView>(R.id.button_back)
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
                when(preference.key){
                    "account_myInfo" ->{
                        val intent = Intent(context,MyInfoActivity::class.java)
                        startActivity(intent)
                    }
                    "service_qna"->{

                    }
                    "service_privateData"->{

                    }
                    "service_openSource"->{

                    }
                }
            }

            return super.onPreferenceTreeClick(preference)
        }
    }
}
