package com.example.gameitemtrade.Tasks

import android.os.AsyncTask
import android.util.Log
import com.example.gameitemtrade.Data.User
import okhttp3.*
import java.io.*
import java.text.SimpleDateFormat
import java.util.*


public class UpdateProfileTask : AsyncTask<String, Void, String>(){

    override fun doInBackground(vararg params: String?): String? {

        val now = System.currentTimeMillis()
        val date = Date(now)
        val sdf = SimpleDateFormat("YYYY_MM_DD_hh_mm_ss")
        val getTime = sdf.format(date)

        val url = "http://192.168.55.69:65001/Profile_Update"
        val client = OkHttpClient()
        val filename = "${getTime}_${User.userID}.jpg"
        lateinit var requestBody : RequestBody
        Log.d("test",params[0].isNullOrEmpty().toString())
        if (params[0].isNullOrEmpty()){
            requestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("profile_nickname",params[1])
                .addFormDataPart("profile_userID",User.userID)
                .build()
        }else{
            requestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("profile_image", filename, RequestBody.create(MultipartBody.FORM, File(params[0].toString())))
                .addFormDataPart("profile_nickname",params[1])
                .addFormDataPart("profile_userID",User.userID)
                .build()
        }

        var request: Request = Request.Builder()
            .url(url)
            .post(requestBody) // get()
            .build()

        var response: Response = client.newCall(request).execute()
        try {
            Log.d("request : ", request.toString())
            Log.d("Response : ", response.toString())
            //response.body()?.string()?.trim()?.let { Log.d("Response : ", it) }
            return response.body()?.string().toString()
        } catch (e: Exception) {
            e.printStackTrace();
        }

        return null
    }
}