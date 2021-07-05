package com.example.gameitemtrade.Tasks

import android.os.AsyncTask
import android.util.Log
import com.example.gameitemtrade.Data.User
import okhttp3.*
import java.io.*
import java.text.SimpleDateFormat
import java.util.*


public class UploadTask : AsyncTask<String, Void, String>(){

    override fun doInBackground(vararg params: String?): String? {

        val now = System.currentTimeMillis()
        val date = Date(now)
        val sdf = SimpleDateFormat("YYYY_MM_DD_hh_mm_ss")
        val getTime = sdf.format(date)

        val url = "http://192.168.55.69:65001/Item_Insert"
        val client = OkHttpClient()
        val filename = "${getTime}_${User.userID}.jpg"
        val requestBody : RequestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("image", filename, RequestBody.create(MultipartBody.FORM, File(params[0].toString())))
                .addFormDataPart("title",params[1])
                .addFormDataPart("price",params[2])
                .addFormDataPart("explain",params[3])
                .addFormDataPart("itemKind",params[4])
                .addFormDataPart("gameTitle",params[5])
                .addFormDataPart("gameServer",params[6])
                .addFormDataPart("seller", User.userID)
                .addFormDataPart("latitude",params[7])
                .addFormDataPart("longitude",params[8])
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
            return response.body()?.string().toString()
        } catch (e: Exception) {
            e.printStackTrace();

        }

        return null
    }
}