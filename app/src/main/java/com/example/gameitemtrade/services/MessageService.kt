package com.example.gameitemtrade.services

import android.R
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.widget.RemoteViews
import androidx.annotation.Nullable
import androidx.core.app.NotificationCompat
import com.example.gameitemtrade.Data.User
import com.example.gameitemtrade.MainActivity
import org.java_websocket.client.WebSocketClient
import org.java_websocket.drafts.Draft_6455
import org.java_websocket.handshake.ServerHandshake
import java.net.URI
import java.net.URISyntaxException


class MessageService : Service() {
    internal lateinit var preferences: SharedPreferences
    lateinit var uri: URI
    lateinit var mWebSocketClient: WebSocketClient

    override fun onCreate() {
        super.onCreate()

    }
    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        preferences = getSharedPreferences("USERSIGN", Context.MODE_PRIVATE)
        val url = "ws://192.168.55.69:8090/connectDB/websocket"
        try {
            uri = URI(url)
        } catch (e: URISyntaxException) {
            e.printStackTrace()
        }

        try {
            mWebSocketClient = object : WebSocketClient(uri, Draft_6455()) {
                override fun onError(ex: Exception?) {

                }

                override fun onClose(code: Int, reason: String?, remote: Boolean) {
                }

                override fun onOpen(serverHandshake: ServerHandshake) {
                    // Opened do some stuff
                }

                override fun onMessage(chatMsg: String) { //서버에서 메세지를 받았을 때때
                    val msg = chatMsg.split("/")
                    Log.d("test",chatMsg)
                    if(msg[1].equals(User.userID)){
                        Log.d("test","${msg[2]}님으로부터 새 메시지")

                        // 오래오 윗버젼일 때는 아래와 같이 채널을 만들어 Notification과 연결해야 한다.
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            val channel = NotificationChannel("channel", "play!!",
                                NotificationManager.IMPORTANCE_DEFAULT)

                            // Notification과 채널 연걸
                            val mNotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                            mNotificationManager.createNotificationChannel(channel)

                            // Notification 세팅
                            val notification: NotificationCompat.Builder = NotificationCompat.Builder(applicationContext, "channel")
                                .setContentTitle("${msg[2]}님으로부터 새 메시지\"")
                                .setContentText("앱을 열어서 확인해주세요!")

                            // id 값은 0보다 큰 양수가 들어가야 한다.
                            mNotificationManager.notify(1, notification.build())
                            // foreground에서 시작
                            startForeground(1, notification.build())
                        }
                    }
                }
            }
        }catch(e: Exception) {
            e.printStackTrace()
        }

        mWebSocketClient.connect()

        return START_STICKY
    }
}