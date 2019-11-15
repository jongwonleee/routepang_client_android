package com.itaewonproject.landingpage

import android.os.Bundle
import android.text.Editable
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.itaewonproject.customviews.ClearEditText
import com.itaewonproject.R
import com.itaewonproject.model.sender.Customer
import com.itaewonproject.rests.post.SignInConnector

class SignInActivity : AppCompatActivity() {

    lateinit var editID: ClearEditText
    lateinit var editPW: ClearEditText
    lateinit var editPWCheck: ClearEditText
    lateinit var editName: ClearEditText
    lateinit var buttonSignin: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        editID = findViewById(R.id.edit_id) as ClearEditText
        editPW = findViewById(R.id.edit_password) as ClearEditText
        editPWCheck = findViewById(R.id.edit_password_check) as ClearEditText
        editName = findViewById(R.id.edit_name) as ClearEditText
        buttonSignin = findViewById(R.id.button_link_share) as Button
        buttonSignin.setOnClickListener({
            val empty = Editable.Factory.getInstance().newEditable("")
            val id = editID.text.toString().trim()
            val pw = editPW.text.toString().trim()
            val check = editPWCheck.text.toString().trim()
            val name = editName.text.toString().trim()
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
            if(name.length<4){
                Toast.makeText(this,"이름을 다시 입력하세요.",Toast.LENGTH_LONG).show()
                editName.text=empty
                return@setOnClickListener
            }
            if(!pw.equals(check)){
                Toast.makeText(this,"비밀번호가 일치하지 않습니다.",Toast.LENGTH_LONG).show()
                editPW.text = empty
                editPWCheck.text = empty
                return@setOnClickListener
            }
            val customer = Customer()
            customer.account=id
            customer.reference=name
            customer.password=pw
            val ret = SignInConnector().post(customer)
            if(ret.responceCode==201){
                Toast.makeText(this,"환영합니다!",Toast.LENGTH_LONG).show()
                finish()
            }else
            {
                Toast.makeText(this,"회원가입에 실패했습니다. 다시 시도해주세요.",Toast.LENGTH_LONG).show()
                editName.text=empty
                editID.text=empty
                editPWCheck.text=empty
                editPW.text=empty
            }
        })
    }
}
