package com.example.gameitemtrade

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.example.gameitemtrade.Tasks.JspTask
import com.example.gameitemtrade.Tasks.UserInfoTask
import com.squareup.picasso.Picasso
import org.json.JSONArray

class UserInfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_info)

        val secondIntent = intent
        val select_user = secondIntent.getStringExtra("select_user").toString()

        val iv_selectUser_pic = findViewById(R.id.iv_selectUser_pic) as ImageView
        val tv_selectUser_name = findViewById(R.id.tv_selectUser_name) as TextView
        val tv_selectUser_point = findViewById(R.id.tv_selectUser_point) as TextView
        val loginTask = UserInfoTask()
        val result = loginTask.execute(select_user).get()

        val User = JSONArray(result).getJSONObject(0)

        Picasso.get().load(User.getString("profilePicture")).error(R.drawable.nonephoto).into(iv_selectUser_pic);
        tv_selectUser_name.text = User.getString("nickName")
        tv_selectUser_point.text = User.getInt("attitude").toString() + "점의 유저입니다!!"

    }
}