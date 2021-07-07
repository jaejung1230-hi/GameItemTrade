package com.example.gameitemtrade

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import com.example.gameitemtrade.Data.ItemInfomation
import com.example.gameitemtrade.Data.User
import com.example.gameitemtrade.Tasks.ChatRoomTask
import com.squareup.picasso.Picasso

class DetailItemInfoActivity : AppCompatActivity() {
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

        //val btn_postItem = findViewById(R.id.btn_postItem_Detail) as Button

        val secondIntent = intent
        val buyer = User.userID
        val item = secondIntent.getParcelableExtra<ItemInfomation>("item")
        Picasso.get().load(item?.fileName).error(R.drawable.nonephoto).into(iv_itemImage)
        Log.d("test", "로 : " + item?.fileName)
        detail_itemTitle.setText(item?.title)
        detail_itemPrice.setText(item?.price.toString())
        detail_itemKinds.setText(item?.kind)
        detail_gameTitle.setText(item?.gameTitle)
        detail_gameServer.setText(item?.gameServer)
        detail_itemExplain.setText(item?.explain)
        detail_seller.setText(item?.seller)
        val seller = item?.seller
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
    }
}