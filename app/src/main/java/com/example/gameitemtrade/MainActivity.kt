package com.example.gameitemtrade

import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.Signature
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Base64
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.gameitemtrade.Tasks.LoginTask
import com.example.gameitemtrade.services.MessageService
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class MainActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val newIdBtn = findViewById(R.id.newIdBtn) as TextView
        val btn_Login = findViewById(R.id.btn_Login) as TextView
        val edit_loginId = findViewById(R.id.edit_loginId) as EditText
        val edit_loginPassword = findViewById(R.id.edit_loginPassword) as EditText
        val TempB = findViewById(R.id.TempB) as Button
        getHashKey()

        newIdBtn.setOnClickListener {
            var intent = Intent(this, SigninActivity::class.java)
            startActivity(intent)
        }

        TempB.setOnClickListener {
            var intent = Intent(this, TempActivity::class.java)
            startActivity(intent)
        }

        btn_Login.setOnClickListener {
            val loginId = edit_loginId.text.toString()
            val loginPassword = edit_loginPassword.text.toString()
            if (loginId.equals("")){
                Toast.makeText(this,"ID를 입력해주세요!!", Toast.LENGTH_SHORT).show()
            }
            else if (loginPassword.equals("")){
                Toast.makeText(this,"비밀번호를 입력해주세요!!",Toast.LENGTH_SHORT).show()
            }
            else{
                val loginTask = LoginTask()
                val result = loginTask.execute(loginId, loginPassword).get()

                if(result.equals("None")){
                    Toast.makeText(this,"ID 또는 비밀번호를 확인해주세요!!",Toast.LENGTH_SHORT).show()
                }else{
                    //val sintent = Intent(this, MessageService::class.java)

                    //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                    //    startForegroundService(sintent);
                    //else startService(sintent);

                    var intent = Intent(this, GameListActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }

    fun getHashKey(){
        var packageInfo : PackageInfo = PackageInfo()
        try {
            packageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
        } catch (e: PackageManager.NameNotFoundException){
            e.printStackTrace()
        }

        for (signature: Signature in packageInfo.signatures){
            try{
                var md: MessageDigest = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                Log.e("KEY_HASH", Base64.encodeToString(md.digest(), Base64.DEFAULT))
            } catch(e: NoSuchAlgorithmException){
                Log.e("KEY_HASH", "Unable to get MessageDigest. signature = " + signature, e)
            }
        }
    }
}