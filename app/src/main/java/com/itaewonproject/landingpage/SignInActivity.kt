package com.itaewonproject.landingpage

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.itaewonproject.customviews.ClearEditText
import com.itaewonproject.R

class SignInActivity : AppCompatActivity() {

    lateinit var editID: ClearEditText
    lateinit var editPW: ClearEditText
    lateinit var editPWCheck: ClearEditText
    lateinit var editName: ClearEditText
    lateinit var buttonSignin: Button
    lateinit var buttonSigninKakao: ConstraintLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        editID = findViewById(R.id.edit_id) as ClearEditText
        editPW = findViewById(R.id.edit_password) as ClearEditText
        editPWCheck = findViewById(R.id.edit_password_check) as ClearEditText
        editName = findViewById(R.id.edit_name) as ClearEditText
        buttonSignin = findViewById(R.id.button_create) as Button
        buttonSigninKakao = findViewById(R.id.button_login_kakao) as ConstraintLayout
        buttonSignin.setOnClickListener({
            finish()
        })
    }
}
