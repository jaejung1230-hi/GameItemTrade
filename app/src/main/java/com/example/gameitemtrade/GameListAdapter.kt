package com.example.gameitemtrade

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.gameitemtrade.Data.game_array
import com.example.gameitemtrade.Main_Fragment.PageActivity
import com.example.gameitemtrade.Tasks.UserInfoTask

class GameListAdapter(private val items: Array<game_array>) : RecyclerView.Adapter<GameListAdapter.ViewHolder>() {

        //RecyclerView 초기화때 호출된다.
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val inflatedView = LayoutInflater.from(parent.context)
                .inflate(R.layout.gamelist_recycler, parent, false)
            return GameListAdapter.ViewHolder(inflatedView)
        }

        override fun getItemCount(): Int = items.size

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val listener = View.OnClickListener { it ->
                Toast.makeText(it.context, items[position].name, Toast.LENGTH_SHORT).show()
                var intent = Intent(it.context, PageActivity::class.java)
                intent.putExtra("gameTitle",items[position].name)
                it.context.startActivity(intent)
            }
            holder.bind(listener, items[position])
        }

        //ViewHolder 단위 객체로 View의 데이터를 설정합니다
        class ViewHolder(private var v: View) : RecyclerView.ViewHolder(v){
            val gamelist_recycler_title: TextView = v.findViewById(R.id.gamelist_recycler_title)
            fun bind(listener: View.OnClickListener, item: game_array){
                val userInfoTask = UserInfoTask()
                v.setOnClickListener(listener)
                gamelist_recycler_title.setText(item.name)
            }
        }
}