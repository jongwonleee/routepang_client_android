package com.itaewonproject.landingpage

import android.annotation.TargetApi
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.itaewonproject.customviews.ClearEditText
import com.itaewonproject.R
import com.itaewonproject.mainservice.MainActivity

class LoginActivity : AppCompatActivity() {

    lateinit var editID: ClearEditText
    lateinit var editPW: ClearEditText
    lateinit var buttonLogin: Button
    lateinit var buttonLoginKaKao: ConstraintLayout
    lateinit var buttonSignin: TextView
    lateinit var buttonFind:TextView
    lateinit var checkAutoLogin: CheckBox

    private lateinit var sharedPreferences:SharedPreferences


    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        editID = findViewById(R.id.edit_id) as ClearEditText
        editPW = findViewById(R.id.edit_password) as ClearEditText
        buttonLogin = findViewById(R.id.button_login) as Button
        buttonLoginKaKao = findViewById(R.id.button_login_kakao) as ConstraintLayout
        buttonSignin = findViewById(R.id.button_signin) as TextView
        buttonFind = findViewById(R.id.button_find) as TextView
        checkAutoLogin = findViewById(R.id.check_autologin) as CheckBox

        sharedPreferences = getSharedPreferences("autoLogin", MODE_PRIVATE)
        checkAutoLogin.isChecked = sharedPreferences.getBoolean("autoLoginCheck",false)

        buttonSignin.text= Html.fromHtml("<u>회원가입</u>",Html.FROM_HTML_MODE_LEGACY)
        buttonFind.text=Html.fromHtml("<u>ID, 비밀번호 찾기</u>",Html.FROM_HTML_MODE_LEGACY)
        buttonLogin.setOnClickListener({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        })

        buttonSignin.setOnClickListener({
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)

        })

        buttonFind.setOnClickListener({
            val intent = Intent(this, IdFindActivity::class.java)
            startActivity(intent)

        })

        checkAutoLogin.setOnCheckedChangeListener({ compoundButton: CompoundButton, isChecked: Boolean ->
            val editor = sharedPreferences.edit()
            editor.putBoolean("autoLoginCheck",isChecked)
            if(isChecked) {
                editor.putString("autoLoginId", editID.text.toString())
                editor.putString("autoLoginPW", editPW.text.toString())
            }
            editor.apply()
        })

    }

}
