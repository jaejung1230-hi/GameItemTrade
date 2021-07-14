package com.example.gameitemtrade.Tasks

import android.os.AsyncTask
import android.util.Log
import com.example.gameitemtrade.Data.User
import okhttp3.*


public class SelectItemTask : AsyncTask<String, Void, String>(){

    override fun doInBackground(vararg params: String?): String {
        lateinit var url : String
        val requestBody = FormBody.Builder()
            .add("name", params[0])
            .build()
        if(params[1].equals("Find_BookMark")){
            url = "http://192.168.55.69:65001/Find_BookMark"
        } else if(params[1].equals("Find_MyItem")){
            url = "http://192.168.55.69:65001/Find_MyItem"
        }

        val client = OkHttpClient()

        var request: Request = Request.Builder()
                .url(url)
                .post(requestBody) // get()
                .build()
        var response: Response = client.newCall(request).execute()

        return response.body()?.string().toString()
    }
}