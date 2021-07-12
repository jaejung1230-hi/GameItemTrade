package com.example.gameitemtrade.Tasks

import android.os.AsyncTask
import android.util.Log
import okhttp3.*


public class GetItemTask : AsyncTask<String, Void, String>(){

    override fun doInBackground(vararg params: String?): String {
        val lastNum = params.size-1
        lateinit var url : String
        lateinit var requestBody : RequestBody
        if(params[lastNum].equals("Get_ItemList_Title")){
            url = "http://192.168.55.69:65001/Get_ItemList_Title"
            requestBody  = FormBody.Builder()
                    .add("GameTitle",params[0])
                    .add("ItemKind",params[1])
                    .add("GameServer",params[2])
                    .add("ItemTitle",params[3])
                    .build()
        } else if(params[lastNum].equals("Get_ItemList_Location")){
            url = "http://192.168.55.69:65001/Get_ItemList_Location"
            requestBody  = FormBody.Builder()
                    .add("latitude",params[0])
                    .add("longitude",params[1])
                    .add("distance",params[2])
                    .build()
        } else if(params[lastNum].equals("Get_ItemList_ID")){
            url = "http://192.168.55.69:65001/Get_ItemList_ID"
            requestBody  = FormBody.Builder()
                .add("productID",params[0])
                .build()
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