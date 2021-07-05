package com.example.gameitemtrade

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_chat.*
import org.java_websocket.WebSocket
import org.java_websocket.client.WebSocketClient
import org.java_websocket.drafts.Draft_6455
import org.java_websocket.handshake.ServerHandshake
import java.net.URI
import java.net.URISyntaxException
import java.text.SimpleDateFormat
import java.util.*


class ChatActivity : AppCompatActivity() {

    internal lateinit var preferences: SharedPreferences
    private lateinit var chating_Text: EditText
    private lateinit var chat_Send_Button: Button
    private lateinit var userId: String

    var arrayList = arrayListOf<ChatModel>()
    lateinit var mAdapter:ChatAdapter
    lateinit var uri: URI
    lateinit var mWebSocketClient: WebSocketClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        preferences = getSharedPreferences("USERSIGN", Context.MODE_PRIVATE)
        val url = "ws://192.168.55.69:8090/connectDB/websocket"
        try { uri = URI(url)
            Log.d("test","websockethost clear!!!!!!!!!!!!!")
        }catch(e: URISyntaxException) {
            e.printStackTrace()
            return
        }

        val secondIntent = intent
        userId = secondIntent.getStringExtra("userId").toString()
        mAdapter = ChatAdapter(this, arrayList,userId)

        //어댑터 선언
        chat_recyclerview.adapter = mAdapter
        //레이아웃 매니저 선언
        val lm = LinearLayoutManager(this)
        chat_recyclerview.layoutManager = lm
        chat_recyclerview.setHasFixedSize(true)//아이템이 추가삭제될때 크기측면에서 오류 안나게 해줌

        chat_Send_Button = findViewById(R.id.chat_Send_Button)
        chating_Text = findViewById(R.id.chating_Text)

        try {
            mWebSocketClient = object : WebSocketClient(uri, Draft_6455()) {
                override fun onError(ex: Exception?) {
                    // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    Log.d("test","onError")
                    Log.d("test", ex?.stackTrace.toString())
                }
                
                override fun onClose(code: Int, reason: String?, remote: Boolean) {
                    // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    Log.d("test","onClose")
                    Log.d("test",reason!!)
                }

                override fun onOpen(serverHandshake: ServerHandshake) {
                    // Opened do some stuff
                    Log.d("test","onOpen")
                }
                override fun onMessage(s: String) { //서버에서 메세지를 받았을 때때
                    val chatMsg = s
                    val now = System.currentTimeMillis()
                    val date = Date(now)
                    val sdf = SimpleDateFormat("yyyy-MM-dd hh:mm")
                    val getTime = sdf.format(date)

                    Log.d("test","chat"+chatMsg)
                    val sendchatMsg = chatMsg.split(":")
                    val item = ChatModel(sendchatMsg[0],sendchatMsg[1], getTime)
                    mAdapter.addItem(item)
                    mAdapter.notifyDataSetChanged()
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
        //나중에 바꿔줄것 밑의 yyyy-MM-dd는 그냥 20xx년 xx월 xx일만 나오게 하는 식
        val sdf = SimpleDateFormat("yyyy-MM-dd hh:mm")

        val getTime = sdf.format(date)


        //example에는 원래는 이미지 url이 들어가야할 자리
        val item = ChatModel(userId,chating_Text.text.toString(), getTime)
        mAdapter.addItem(item)
        mAdapter.notifyDataSetChanged()
        mWebSocketClient.send(chating_Text.getText().toString())
        //채팅 입력창 초기화
        chating_Text.setText("")
    }
}