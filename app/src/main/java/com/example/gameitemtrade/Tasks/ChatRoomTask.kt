package com.example.gameitemtrade.Tasks

import android.os.AsyncTask
import com.example.gameitemtrade.Data.User
import okhttp3.*
import org.json.JSONArray

public class ChatRoomTask : AsyncTask<String, Void, String>(){

    override fun doInBackground(vararg params: String?): String {
        val url = "http://192.168.55.69:65001/Make_ChattingRoom"
        val client = OkHttpClient()
        val requestBody : RequestBody = FormBody.Builder()
                .add("seller",params[0])
                .add("buyer",params[1])
                .add("productId",params[2])
                .build()

        var request: Request = Request.Builder()
                .url(url)
                .post(requestBody) // get()
                .build()

        var response: Response = client.newCall(request).execute()

        val answer = response.body()?.string().toString()
        System.out.println("Response Body is " +answer)

        return answer
    }
}