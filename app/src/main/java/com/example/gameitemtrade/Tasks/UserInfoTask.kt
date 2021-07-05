package com.example.gameitemtrade.Tasks

import android.os.AsyncTask
import com.example.gameitemtrade.Data.User
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject

public class UserInfoTask : AsyncTask<String, Void, String>(){

    override fun doInBackground(vararg params: String?): String? {
        val url = "http://192.168.55.69:65001/find_user"
        val client = OkHttpClient()
        val requestBody : RequestBody = FormBody.Builder()
                .add("userID",params[0])
                .build()

        var request: Request = Request.Builder()
                .url(url)
                .post(requestBody) // get()
                .build()
        var response: Response = client.newCall(request).execute()

        val user = response.body()?.string().toString()
        System.out.println("Response Body is " +user)

        return user
    }
}