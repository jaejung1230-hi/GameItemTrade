package com.example.gameitemtrade.Chat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gameitemtrade.Data.ItemInfomation
import com.example.gameitemtrade.Data.User
import com.example.gameitemtrade.GameListAdapter
import com.example.gameitemtrade.Main_Fragment.ItemListAdapter
import com.example.gameitemtrade.R
import com.example.gameitemtrade.Tasks.FindChatTask
import com.example.gameitemtrade.game_array
import org.json.JSONArray

class ChatRoomActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_room)

        val recyclerview_chatList = findViewById(R.id.recyclerview_chatList) as RecyclerView

        val chat_name = User.userID+"_chat"

        val findChatTask = FindChatTask()
        val list = findChatTask.execute(chat_name).get()

        if (!list.equals("[]")) {
            val listArray = JSONArray(list)
            val chatInfoList: ArrayList<String> = arrayListOf<String>()
            for (i in 0..(listArray.length()-1)){
                chatInfoList.add(listArray.getJSONObject(i).getString("chatRoom"))
            }
            val adapter = ChatRoomAdapter(chatInfoList,this)
            recyclerview_chatList.setLayoutManager(LinearLayoutManager(this))
            recyclerview_chatList.setAdapter(adapter)
        }
    }
}