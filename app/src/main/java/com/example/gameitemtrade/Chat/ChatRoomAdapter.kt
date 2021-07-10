package com.example.gameitemtrade.Chat

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gameitemtrade.Data.User
import com.example.gameitemtrade.R
import com.example.gameitemtrade.Tasks.ChatDeleteTask
import com.example.gameitemtrade.Tasks.ItemDeleteTask
import com.example.gameitemtrade.Tasks.UserInfoTask
import com.squareup.picasso.Picasso
import org.json.JSONArray
import org.json.JSONObject
import java.util.*

class ChatRoomAdapter(val items: ArrayList<String>, val this_context: Context) :  RecyclerView.Adapter<ChatRoomAdapter.ViewHolder>() {

    //RecyclerView 초기화때 호출된다.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflatedView = LayoutInflater.from(parent.context)
            .inflate(R.layout.chatlist_recycler, parent, false)
        return ChatRoomAdapter.ViewHolder(inflatedView)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val str = items[position].split("_")
        lateinit var sender : String
        if(str[2].equals(User.userID)){ sender=str[3]
        }else{                  sender=str[2] }

        val loginTask = UserInfoTask()
        val result = loginTask.execute(sender).get()
        val user = JSONArray(result).getJSONObject(0)

        val listener = View.OnClickListener {it ->
            var intent = Intent(it.context, ChatActivity::class.java)
            intent.putExtra("chatroom",items[position])
            intent.putExtra("Other",result)
            it.context.startActivity(intent)
        }
        val longlistener = View.OnLongClickListener {it ->
            val builder = AlertDialog.Builder(this_context)
            builder.setTitle("채팅방 삭제")
            builder.setMessage("채팅방을 정말 삭제 하시겠습니까?")
            builder.setPositiveButton("예") { dialogInterface: DialogInterface, i: Int ->
                val task = ChatDeleteTask()
                task.execute(items[position]);
            }
            builder.setNegativeButton("아니오") { dialogInterface: DialogInterface, i: Int ->
            }

            builder.show()
            return@OnLongClickListener true
        }
        holder.bind(listener, longlistener, user)
    }

    //ViewHolder 단위 객체로 View의 데이터를 설정합니다
    class ViewHolder(private var v: View) : RecyclerView.ViewHolder(v){
        val chatlist_recycler_name: TextView = v.findViewById(R.id.chatlist_recycler_name)
        val chatlist_recycler_image: ImageView = v.findViewById(R.id.chatlist_recycler_image)
        fun bind(listener: View.OnClickListener, longlistener: View.OnLongClickListener, item: JSONObject){
            v.setOnClickListener(listener)
            v.setOnLongClickListener(longlistener)
            chatlist_recycler_name.setText(item.getString("nickName"))
            Picasso.get().load(item.getString("profilePicture")).error(R.drawable.nonephoto).into(chatlist_recycler_image);
        }
    }
}