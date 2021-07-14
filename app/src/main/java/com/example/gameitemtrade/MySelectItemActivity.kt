package com.example.gameitemtrade

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gameitemtrade.Data.ItemInfomation
import com.example.gameitemtrade.Data.User
import com.example.gameitemtrade.Main_Fragment.ItemListAdapter
import com.example.gameitemtrade.Tasks.GetItemTask
import com.example.gameitemtrade.Tasks.SelectItemTask
import org.json.JSONArray

class MySelectItemActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_select_item)
        val recyclerview_select_itemlist = findViewById(R.id.recyclerview_select_itemlist) as RecyclerView

        val secondIntent = intent
        val function = secondIntent.getStringExtra("function").toString()

        val task = SelectItemTask()
        var result = task.execute(User.userID,function).get()
        val itemListArray = JSONArray(result)
        val itemInfoList: ArrayList<ItemInfomation> = arrayListOf<ItemInfomation>()
        if (!result.equals("[]")) {
            for (i in 0..(itemListArray.length() - 1)) {
                itemInfoList.add(ItemInfomation(itemListArray.getJSONObject(i)))
            }
        }
        val adapter = ItemListAdapter(itemInfoList)
        recyclerview_select_itemlist.setLayoutManager(LinearLayoutManager(this))
        recyclerview_select_itemlist.setAdapter(adapter)
    }
}