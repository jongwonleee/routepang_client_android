package com.itaewonproject.setting

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.itaewonproject.R

class MyInfoActivity : AppCompatActivity() {

    private lateinit var buttonBack:ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        buttonBack = findViewById(R.id.button_back) as ImageView
        buttonBack.setOnClickListener({ finish()})
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.settings, SettingsFragment())
            .commit()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.my_info_preferences, rootKey)
        }

        override fun onPreferenceTreeClick(preference: Preference?): Boolean {
            if(preference!=null) {
                when(preference!!.key){
                    "secure_quit" ->{
                        val intent = Intent(context,QuitActivity::class.java)
                        startActivity(intent)
                        activity?.overridePendingTransition(R.anim.translate_in_from_left,0)
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
