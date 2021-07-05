package com.example.gameitemtrade

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.net.URL
import java.util.ArrayList

class ChatAdapter(val context: Context, val arrayList: ArrayList<ChatModel>, val myId:String) :  RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    internal lateinit var preferences: SharedPreferences

    fun addItem(item: ChatModel) {//아이템 추가
        if (arrayList != null) {
            arrayList.add(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View
        //getItemViewType 에서 뷰타입 1을 리턴받았다면 내채팅레이아웃을 받은 Holder를 리턴
        if(viewType == 1){
            view = LayoutInflater.from(context).inflate(R.layout.ownchat_recycler, parent, false)
            return Holder(view)
        }
        //getItemViewType 에서 뷰타입 2을 리턴받았다면 상대채팅레이아웃을 받은 Holder2를 리턴
        else{
            view = LayoutInflater.from(context).inflate(R.layout.otherchat_recycler, parent, false)
            return Holder2(view)
        }
    }

    override fun getItemCount(): Int {
        return arrayList.size

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        //onCreateViewHolder에서 리턴받은 뷰홀더가 Holder라면 내채팅, item_my_chat의 뷰들을 초기화 해줌
        if (holder is Holder) {
            (holder as Holder).chat_Text?.setText(arrayList.get(position).script)
            (holder as Holder).chat_Time?.setText(arrayList.get(position).date_time)
        }
        //onCreateViewHolder에서 리턴받은 뷰홀더가 Holder2라면 상대의 채팅, item_your_chat의 뷰들을 초기화 해줌
        else if(holder is Holder2) {
            (holder as Holder2).chat_Other_Name?.setText(arrayList.get(position).name)
            (holder as Holder2).chat_Text?.setText(arrayList.get(position).script)
            (holder as Holder2).chat_Time?.setText(arrayList.get(position).date_time)
        }
    }

    //내가친 채팅 뷰홀더
    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //친구목록 모델의 변수들 정의하는부분
        val chat_Text = itemView?.findViewById(R.id.chat_Text) as TextView
        val chat_Time = itemView?.findViewById(R.id.chat_Time) as TextView
    }

    //상대가친 채팅 뷰홀더
    inner class Holder2(itemView: View) : RecyclerView.ViewHolder(itemView) {

        //친구목록 모델의 변수들 정의하는부분
        val chat_Other_Name = itemView?.findViewById(R.id.chat_Other_Name) as TextView
        val chat_Text = itemView?.findViewById(R.id.chat_Text) as TextView
        val chat_Time = itemView?.findViewById(R.id.chat_Time) as TextView


    }

    override fun getItemViewType(position: Int): Int {//여기서 뷰타입을 1, 2로 바꿔서 지정해줘야 내채팅 너채팅을 바꾸면서 쌓을 수 있음
        //preferences = context.getSharedPreferences("USERSIGN", Context.MODE_PRIVATE)
        //내 아이디와 arraylist의 name이 같다면 내꺼 아니면 상대꺼
        return if (arrayList.get(position).name == myId) { 1 } else { 2 }
    }




}