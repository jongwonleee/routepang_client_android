package com.itaewonproject.landingpage

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.Html
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.gson.Gson
import com.itaewonproject.JsonParser
import com.itaewonproject.customviews.ClearEditText
import com.itaewonproject.R
import com.itaewonproject.Routepang
import com.itaewonproject.mainservice.MainActivity
import com.itaewonproject.model.sender.Customer
import com.itaewonproject.rests.IS_OFFLINE
import com.itaewonproject.rests.authorization
import com.itaewonproject.rests.get.GetCustomerConnector
import com.itaewonproject.rests.post.LogInConnector

class LoginActivity : AppCompatActivity() {

    lateinit var editID: ClearEditText
    lateinit var editPW: ClearEditText
    lateinit var buttonLogin: Button
    lateinit var buttonLoginKaKao: ConstraintLayout
    lateinit var buttonSignin: TextView
    lateinit var buttonFind:TextView
    lateinit var checkAutoLogin: CheckBox

    private lateinit var sharedPreferences:SharedPreferences


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
            val empty = Editable.Factory.getInstance().newEditable("")
            val id = editID.text.toString().trim()
            val pw = editPW.text.toString().trim()
            if(id.length<6){
                Toast.makeText(this,"ID를 다시 입력하세요.",Toast.LENGTH_LONG).show()
                editID.text=empty
                return@setOnClickListener
            }
            if(pw.length<8){
                Toast.makeText(this,"비밀번호를 다시 입력하세요.",Toast.LENGTH_LONG).show()
                editPW.text=empty
                return@setOnClickListener
            }
            val customer = Customer()
            customer.account=id
            customer.password=pw
            val ret = LogInConnector().post(customer)
            if(ret.responceCode ==200)
            {
                (application as Routepang).token = ret.body!!
                authorization = ret.body!!
                (application as Routepang).customer = JsonParser().objectJsonParsing(GetCustomerConnector().get(id).body!!,Customer::class.java)!!
                if(checkAutoLogin.isChecked){
                    val editor = sharedPreferences.edit()
                    editor.putString("loginToken", ret.body)
                    editor.putString("autoLoginCustomer", Gson().toJson((application as Routepang).customer))
                    editor.apply()
                }
                if (intent.getStringExtra(Intent.EXTRA_TEXT) != null) {
                    finish()
                }else
                {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.translate_in_from_right,R.anim.translate_out_to_left)
                    finish()
                }

            }else
            {
                Toast.makeText(this,"로그인에 실패했습니다. 다시 시도해주세요.",Toast.LENGTH_LONG).show()
                editID.text=empty
                editPW.text=empty
                if(IS_OFFLINE) {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.translate_in_from_right,R.anim.translate_out_to_left)
                    finish()
                }
            }

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
            editor.apply()
        })

    }

}
