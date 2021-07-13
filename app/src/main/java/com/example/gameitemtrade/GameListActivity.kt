package com.example.gameitemtrade

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gameitemtrade.Data.game_array

class GameListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_list)

        val recyclerview_itemlist = findViewById(R.id.recyclerview_gamelist) as RecyclerView

        val adapter =  GameListAdapter(game_array.values())
        recyclerview_itemlist.setLayoutManager(LinearLayoutManager(this));
        recyclerview_itemlist.setAdapter(adapter);
    }
}