package com.example.gameitemtrade.Tasks

import android.os.AsyncTask
import com.example.gameitemtrade.Data.User
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response

public class StateUpdateTask : AsyncTask<String, Void, String>() {

    override fun doInBackground(vararg params: String?): String {
        val url = "http://192.168.55.69:65001/State_Update"
        val requestBody = FormBody.Builder()
                .add("productId", params[0])
                .add("state", params[1])
                .build()

        val client = OkHttpClient()

        var request: Request = Request.Builder()
                .url(url)
                .post(requestBody) // get()
                .build()
        var response: Response = client.newCall(request).execute()

        return response.body()?.string().toString()
    }
}