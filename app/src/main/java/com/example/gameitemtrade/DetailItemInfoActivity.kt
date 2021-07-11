package com.example.gameitemtrade

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.gameitemtrade.Data.ItemInfomation
import com.example.gameitemtrade.Data.User
import com.example.gameitemtrade.Tasks.BookMarkTask
import com.example.gameitemtrade.Tasks.ChatRoomTask
import com.example.gameitemtrade.Tasks.ItemDeleteTask
import com.example.gameitemtrade.Tasks.StateUpdateTask
import com.google.android.material.internal.ContextUtils.getActivity
import com.squareup.picasso.Picasso


class DetailItemInfoActivity : AppCompatActivity() {
    lateinit var product_ID : String
    lateinit var seller : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_item_info)

        val iv_itemImage = findViewById(R.id.iv_itemImage_Detail) as ImageView
        val detail_itemTitle = findViewById(R.id.detail_itemTitle) as TextView
        val detail_itemPrice = findViewById(R.id.detail_itemPrice) as TextView
        val detail_itemKinds = findViewById(R.id.detail_itemKinds) as TextView
        val detail_gameTitle = findViewById(R.id.detail_gameTitle) as TextView
        val detail_gameServer = findViewById(R.id.detail_gameServer) as TextView
        val detail_itemExplain = findViewById(R.id.detail_itemExplain) as TextView
        val detail_seller = findViewById(R.id.detail_seller) as TextView
        val btn_makechat = findViewById(R.id.btn_makechat) as TextView
        val btn_makebookmark = findViewById(R.id.btn_makebookmark) as TextView
        val detail_item_reserved:TextView = findViewById(R.id.detail_item_reserved)
        val detail_item_sold_out:TextView = findViewById(R.id.detail_item_sold_out)

        //val btn_postItem = findViewById(R.id.btn_postItem_Detail) as Button

        val secondIntent = intent
        val buyer = User.userID
        val item = secondIntent.getParcelableExtra<ItemInfomation>("item")
        Picasso.get().load(item?.fileName).error(R.drawable.nonephoto).into(iv_itemImage)
        product_ID = item?.productId.toString()
        detail_itemTitle.setText(item?.title)
        detail_itemPrice.setText(item?.price.toString())
        detail_itemKinds.setText(item?.kind)
        detail_gameTitle.setText(item?.gameTitle)
        detail_gameServer.setText(item?.gameServer)
        detail_itemExplain.setText(item?.explain)
        detail_seller.setText(item?.seller)
        seller = item?.seller.toString()

        when(item?.state){
            "1" -> {detail_item_reserved.setVisibility(View.VISIBLE)}
            "2" -> {detail_item_sold_out.setVisibility(View.VISIBLE)}
        }

        if(buyer.equals(seller)){
            btn_makechat.setEnabled(false);
            btn_makechat.setBackgroundResource(R.drawable.custom_unable_button)
        }

        btn_makechat.setOnClickListener {
            if (buyer.equals(seller)){
                Toast.makeText(this,"자신의 아이템은 구매할 수 없어요!",Toast.LENGTH_SHORT).show()
            }else{
                val chatRoomTask = ChatRoomTask()
                val result = chatRoomTask.execute(seller, buyer, item?.productId).get()
                if(result.equals("Clear")){
                    Toast.makeText(this, "채팅방이 생성이 완료됐습니다.",Toast.LENGTH_SHORT).show()
                }else{Toast.makeText(this, "채팅방이 이미 생성되었습니다.",Toast.LENGTH_SHORT).show()}
            }
        }
        btn_makebookmark.setOnClickListener{
            if (buyer.equals(seller)){
                Toast.makeText(this,"자신의 아이템은 찜 할 수 없어요!",Toast.LENGTH_SHORT).show()
            }else{
                val bookMarkTask = BookMarkTask()
                val result = bookMarkTask.execute(item?.productId).get()
                if(result.equals("Clear")){
                    Toast.makeText(this, "아이템을 찜 했습니다.",Toast.LENGTH_SHORT).show()
                }else{Toast.makeText(this, "이미 찜한 아이템입니다.",Toast.LENGTH_SHORT).show()}
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if(User.userID.equals(seller)){
            val inflater = menuInflater
            inflater.inflate(R.menu.item_detail_info_menu, menu)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.menu_delete_item -> {
                val task = ItemDeleteTask(); task.execute(product_ID); finish();}
            R.id.menu_sell -> {
                val task = StateUpdateTask(); task.execute(product_ID,"0"); }
            R.id.menu_reserved -> {
                val task = StateUpdateTask(); task.execute(product_ID,"1"); }
            R.id.menu_sold_out -> {
                val task = StateUpdateTask(); task.execute(product_ID,"2"); }
        }
        return super.onOptionsItemSelected(item)
    }
}