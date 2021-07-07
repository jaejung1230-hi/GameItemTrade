package com.example.gameitemtrade

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.gameitemtrade.Tasks.SignUpTask

class SigninActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)

        var flag : Boolean = false

        val btn_IdPost = findViewById(R.id.btn_IdPost) as Button
        val btn_Idcheck = findViewById(R.id.btn_Idcheck) as Button
        val edit_newId = findViewById(R.id.edit_newId) as EditText
        val edit_newPw = findViewById(R.id.edit_newPw) as EditText
        val edit_newPwCheck = findViewById(R.id.edit_newPwCheck) as EditText

        edit_newId.setOnClickListener { flag = false; btn_Idcheck.setEnabled(true)}

        btn_IdPost.setOnClickListener {
            val newId = edit_newId.text.toString()
            val newPw = edit_newPw.text.toString()
            val newPwCheck = edit_newPwCheck.text.toString()
            Log.d("test",newId)
            if( newPw.equals(newPwCheck)){
                if(flag){
                    try {
                        val testTask = SignUpTask()
                        val aaa = testTask.execute(newId, newPw,"ID_write").get()
                    } catch (e: Exception) { e.printStackTrace() }
                }
                else{Toast.makeText(this,"ID 중복 체크 해주세요",Toast.LENGTH_SHORT).show()}
            }else{
                Toast.makeText(this,"비밀번호를 재확인 해주세요!!",Toast.LENGTH_SHORT).show()
            }
        }

        btn_Idcheck.setOnClickListener {
            val newId = edit_newId.text.toString()
            Log.d("test",newId)
            try {
                val testTask = SignUpTask()
                val usercheck  = testTask.execute(newId, "null","ID_check").get()
                Log.d("test",usercheck)
                if(usercheck.equals("Ok")){
                    Toast.makeText(this,"생성 가능한 아이디 입니다.",Toast.LENGTH_SHORT).show()
                    flag = true
                    btn_Idcheck.setEnabled(false)
                }else{Toast.makeText(this,"이미 존재하는 아이디 입니다.",Toast.LENGTH_SHORT).show()
                    flag = false
                }
                } catch (e: Exception) {
                e.printStackTrace()
            }

            }
    }
}