package com.itaewonproject.landingpage

import android.annotation.TargetApi
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.itaewonproject.R
import com.itaewonproject.mypage.MypageActivity

class LoginActivity : AppCompatActivity() {

    lateinit var editID: EditText
    lateinit var editPW: EditText
    lateinit var buttonLogin: Button
    lateinit var buttonLoginKaKao: ConstraintLayout
    lateinit var buttonSignin: TextView
    lateinit var buttonFind:TextView
    lateinit var checkAutoLogin: CheckBox


    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        editID = findViewById(R.id.edit_id) as EditText
        editPW = findViewById(R.id.edit_password) as EditText
        buttonLogin = findViewById(R.id.button_login) as Button
        buttonLoginKaKao = findViewById(R.id.button_login_kakao) as ConstraintLayout
        buttonSignin = findViewById(R.id.button_signin) as TextView
        buttonFind = findViewById(R.id.button_find) as TextView
        buttonSignin.text= Html.fromHtml("<u>회원가입</u>")
        buttonFind.text=Html.fromHtml("<u>ID, 비밀번호 찾기</u>")
        buttonLogin.setOnClickListener({
            var intent = Intent(this, MypageActivity::class.java)
            startActivity(intent)
            finish()
        })

        buttonSignin.setOnClickListener({
            var intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        })

    }

}
