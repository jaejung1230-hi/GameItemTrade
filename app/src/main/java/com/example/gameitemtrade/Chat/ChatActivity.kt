package com.example.gameitemtrade.Chat

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gameitemtrade.Data.ItemInfomation
import com.example.gameitemtrade.Data.User
import com.example.gameitemtrade.PostReportActivity
import com.example.gameitemtrade.R
import com.example.gameitemtrade.Tasks.ChatTask
import com.example.gameitemtrade.Tasks.ItemDeleteTask
import com.example.gameitemtrade.Tasks.StateUpdateTask
import com.example.gameitemtrade.TempActivity
import kotlinx.android.synthetic.main.activity_chat.*
import org.java_websocket.WebSocket
import org.java_websocket.client.WebSocketClient
import org.java_websocket.drafts.Draft_6455
import org.java_websocket.handshake.ServerHandshake
import org.json.JSONArray
import java.net.URI
import java.net.URISyntaxException
import java.text.SimpleDateFormat
import java.util.*


class ChatActivity : AppCompatActivity() {

    internal lateinit var preferences: SharedPreferences
    private lateinit var chating_Text: EditText
    private lateinit var chat_Send_Button: Button
    lateinit var chatroom : String
    val userId = User.userID
    lateinit var sender : String

    var arrayList = arrayListOf<ChatModel>()
    lateinit var mAdapter: ChatAdapter
    lateinit var uri: URI
    lateinit var mWebSocketClient: WebSocketClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        preferences = getSharedPreferences("USERSIGN", Context.MODE_PRIVATE)
        val url = "ws://192.168.55.69:8090/connectDB/websocket"
        try { uri = URI(url)
        }catch(e: URISyntaxException) {
            e.printStackTrace()
            return
        }

        val secondIntent = intent
        chatroom = secondIntent.getStringExtra("chatroom").toString()

        val other = secondIntent.getStringExtra("Other").toString()
        val other_user = JSONArray(other).getJSONObject(0)

        val ChatTask = ChatTask()
        val result =ChatTask.execute(chatroom).get()
        if (!result.equals("[]")) {
            val chatArray = JSONArray(result)
            for (i in 0..(chatArray.length()-1)){
                val owner = chatArray.getJSONObject(i).getString("from_ID")
                lateinit var msgname :String
                if(owner.equals(User.userID)) {
                    msgname = User.userID
                }else{msgname = other_user.getString("nickName")}
                val chat = ChatModel(
                        other_user.getString("profilePicture"),
                        msgname,
                        chatArray.getJSONObject(i).getString("message"),
                        chatArray.getJSONObject(i).getString("date")
                )

                arrayList.add(chat)
            }
        }

        val str = chatroom!!.split("_")
        val productID = str[1]
        if(str[2].equals(userId)){ sender=str[3]
        }else{                  sender=str[2] }

        mAdapter = ChatAdapter(this, arrayList, userId)
        chat_recyclerview.adapter = mAdapter
        val lm = LinearLayoutManager(this)
        chat_recyclerview.layoutManager = lm
        chat_recyclerview.setHasFixedSize(true)

        chat_Send_Button = findViewById(R.id.chat_Send_Button)
        chating_Text = findViewById(R.id.chating_Text)

        try {
            mWebSocketClient = object : WebSocketClient(uri, Draft_6455()) {
                override fun onError(ex: Exception?) {
                    Log.d("test","onError")
                    Log.d("test", ex?.stackTrace.toString())
                }
                
                override fun onClose(code: Int, reason: String?, remote: Boolean) {
                    Log.d("test","onClose")
                    Log.d("test",reason!!)
                }

                override fun onOpen(serverHandshake: ServerHandshake) {
                    // Opened do some stuff
                    Log.d("test","onOpen")
                }
                override fun onMessage(chatMsg: String) { //서버에서 메세지를 받았을 때때
                    val msg = chatMsg.split("/")
                    Log.d("test","chat"+chatMsg)
                    if(msg[0].equals(chatroom)){
                        val item = ChatModel(
                                other_user.getString("profilePicture"),
                                other_user.getString("nickName"),
                                msg[3],
                                msg[4]
                        )
                        mAdapter.addItem(item)
                        mAdapter.notifyDataSetChanged()
                    }
                }
            }
        }catch(e: Exception) {
            e.printStackTrace()
        }

        mWebSocketClient.connect()

        chat_Send_Button.setOnClickListener {
            //아이템 추가 부분
            if(mWebSocketClient.getReadyState()== WebSocket.READYSTATE.OPEN){
                sendMessage()
            }
        }
    }

    fun sendMessage() {
        val now = System.currentTimeMillis()
        val date = Date(now)
        val sdf = SimpleDateFormat("yyyy-MM-dd_hh:mm")
        val getTime = sdf.format(date)

        val item = ChatModel(
            User.profilePicture,
            userId,
            chating_Text.text.toString(),
            getTime
        )
        mAdapter.addItem(item)
        mAdapter.notifyDataSetChanged()
        var message = chatroom+"/"+sender+"/"+userId+"/"+chating_Text.getText().toString()+"/"+getTime
        mWebSocketClient.send(message)
        //채팅 입력창 초기화
        chating_Text.setText("")
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.chat_menu, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.post_report -> {
                val str = chatroom!!.split("_")
                val productID = str[1]
                var intent = Intent(this, PostReportActivity::class.java)
                intent.putExtra("suspect",sender)
                intent.putExtra("productID",productID)
                startActivity(intent)
            }
            R.id.menu_sell -> {}
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        mWebSocketClient.close()
    }
}