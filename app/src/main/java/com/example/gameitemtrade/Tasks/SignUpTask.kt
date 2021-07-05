package com.example.gameitemtrade.Tasks

import android.os.AsyncTask
import okhttp3.*

public class SignUpTask : AsyncTask<String, Void, String>(){

    //vararg var 초기화 이후에도 변경가능한 arg 가변길이
    override fun doInBackground(vararg params: String?): String {
        val url = "http://192.168.55.69:65001/"+params[2]
        val client = OkHttpClient()
        val requestBody : RequestBody = FormBody.Builder()
            .add("userID",params[0])
            .add("userPassword",params[1])
            .add("nickName",params[0])
            .add("profilePicture","http://192.168.55.69:65001/imgs/default_profile.jpg")
            .build()

        var request: Request = Request.Builder()
            .url(url)
            .post(requestBody) // get()
            .build()
        var response: Response = client.newCall(request).execute()

        return response.body()?.string().toString()
    }
}