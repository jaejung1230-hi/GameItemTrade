package com.example.gameitemtrade.Tasks

import android.os.AsyncTask
import android.util.Log
import okhttp3.*
import java.io.File


class TempTask : AsyncTask<String, Void, String>() {

    override fun doInBackground(vararg params: String?): String? {
        val url = "http://192.168.55.69:65001/test"
        val client = OkHttpClient()

        val requestBody : RequestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("image", "origianl_photo.jpg", RequestBody.create(MultipartBody.FORM, File(params[1].toString())))
            .addFormDataPart("name","wowjd")
            .addFormDataPart("password","wowjd963")
            .build()

        var request: Request = Request.Builder()
            .url(url)
            .post(requestBody) // get()
            .build()

        var response: Response = client.newCall(request).execute()
        try {
            Log.d("request : ", request.toString())
            Log.d("Response : ", response.toString())
            //response.body()?.string()?.trim()?.let { Log.d("Response : ", it) }
            return response.body()?.string()?.trim();
        } catch (e: Exception) {
            e.printStackTrace();

        }

        return null
    }


}