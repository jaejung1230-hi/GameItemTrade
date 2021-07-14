package com.example.gameitemtrade.Tasks

import android.os.AsyncTask
import android.util.Log
import okhttp3.*


public class GetReportTask : AsyncTask<String, Void, String>(){

    override fun doInBackground(vararg params: String?): String {
        val lastNum = params.size-1
        lateinit var url : String
        lateinit var requestBody : RequestBody
        if(params[lastNum].equals("Get_report_Detailed")){
            url = "http://192.168.55.69:65001/Get_report_Detailed"
            requestBody  = FormBody.Builder()
                .add("GameTitle",params[0])
                .add("ItemKind",params[1])
                .add("GameServer",params[2])
                .add("reportReason",params[3])
                .add("suspect",params[4])
                .build()
        }
        if(params[lastNum].equals("Get_report_user")){
            url = "http://192.168.55.69:65001/Get_report_user"
            requestBody  = FormBody.Builder()
                    .add("userID",params[0])
                    .build()
        }

        val client = OkHttpClient()

        var request: Request = Request.Builder()
                .url(url)
                .post(requestBody)
                .build()
        var response: Response = client.newCall(request).execute()

        return response.body()?.string().toString()
    }
}