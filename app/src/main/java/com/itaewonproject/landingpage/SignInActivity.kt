package com.itaewonproject.landingpage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.itaewonproject.R

class SignInActivity : AppCompatActivity() {

    lateinit var editID:EditText
    lateinit var editPW:EditText
    lateinit var editPWCheck:EditText
    lateinit var editName:EditText
    lateinit var buttonSignin:Button
    lateinit var buttonSigninKakao:Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        editID = findViewById(R.id.edit_id) as EditText
        editPW = findViewById(R.id.edit_password) as EditText
        editPWCheck = findViewById(R.id.edit_password_check) as EditText
        editName = findViewById(R.id.edit_name) as EditText
        buttonSignin = findViewById(R.id.button_create) as Button
        buttonSigninKakao = findViewById(R.id.button_create_kakao) as Button
        buttonSignin.setOnClickListener({
            finish()
        })
    }
}
