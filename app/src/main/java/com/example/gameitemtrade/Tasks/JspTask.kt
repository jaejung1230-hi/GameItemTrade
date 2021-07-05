package com.example.gameitemtrade.Tasks

import android.os.AsyncTask
import com.example.gameitemtrade.Data.User
import okhttp3.*
import org.json.JSONArray


// 안드로이드에서 사용가능한 스레드 구현방법중에 하나
// UI는 메인스레드에서만 이용가능
// doInBackground 작업중 publishProgress( )메소드 이용 UI접근 가능

public class JspTask : AsyncTask<String, Void, String>(){

    //vararg var 초기화 이후에도 변경가능한 arg 가변길이
    override fun doInBackground(vararg params: String?): String {
        val url = "http://192.168.55.69:65001/Log_in"
        val client = OkHttpClient()
        val requestBody : RequestBody = FormBody.Builder()
                .add("userID",params[0])
                .add("userPassword",params[1])
                .build()

        var request: Request = Request.Builder()
                .url(url)
                .post(requestBody) // get()
                .build()
        var response: Response = client.newCall(request).execute()

        val user = response.body()?.string().toString()
        System.out.println("Response Body is " +user)

        if (!user.equals("[]")){
            System.out.println("Response Body is " +JSONArray(user).getJSONObject(0))
            User.getInstance(JSONArray(user).getJSONObject(0))
            return "Ok"
        }

        return "None"

        /*
        client.newCall(request).enqueue(object : Callback {//비동기 처리를 위해 Callback 구현
            override fun onFailure(call: Call, e: IOException) {  }
            override fun onResponse(call: Call, response: Response) {  }
        })*/


    }
}