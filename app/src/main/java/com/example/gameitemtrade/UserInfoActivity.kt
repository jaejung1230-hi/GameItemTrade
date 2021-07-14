package com.example.gameitemtrade

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gameitemtrade.Data.ItemInfomation
import com.example.gameitemtrade.Data.ReportObject
import com.example.gameitemtrade.Main_Fragment.ItemListAdapter
import com.example.gameitemtrade.Main_Fragment.ReportListAdapter
import com.example.gameitemtrade.Tasks.GetReportTask
import com.example.gameitemtrade.Tasks.SelectItemTask
import com.example.gameitemtrade.Tasks.UserInfoTask
import com.squareup.picasso.Picasso
import org.json.JSONArray

class UserInfoActivity : AppCompatActivity() {
    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_info)

        val secondIntent = intent
        val select_user = secondIntent.getStringExtra("select_user").toString()

        val iv_selectUser_pic = findViewById(R.id.iv_selectUser_pic) as ImageView
        val tv_selectUser_name = findViewById(R.id.tv_selectUser_name) as TextView
        val tv_selectUser_point = findViewById(R.id.tv_selectUser_point) as TextView
        val btn_users_item = findViewById(R.id.btn_users_item) as TextView
        val btn_users_report = findViewById(R.id.btn_users_report) as TextView
        val recyclerview_users = findViewById(R.id.recyclerview_users) as RecyclerView

        val loginTask = UserInfoTask()
        val result = loginTask.execute(select_user).get()

        val User = JSONArray(result).getJSONObject(0)

        Picasso.get().load(User.getString("profilePicture")).error(R.drawable.nonephoto).into(iv_selectUser_pic);
        tv_selectUser_name.text = User.getString("nickName")
        tv_selectUser_point.text = User.getInt("attitude").toString() + "점의 유저입니다!!"

        btn_users_item.setOnClickListener {
            btn_users_item.setBackgroundResource(R.drawable.custom_login_button)
            btn_users_item.setTextColor(R.color.main_color)
            btn_users_report.setBackgroundResource(R.drawable.custom_solid_buttom)
            btn_users_report.setTextColor(R.color.white)
            val task = SelectItemTask()
            var result = task.execute(User.getString("userID"),"Find_MyItem").get()

            val itemListArray = JSONArray(result)
            val itemInfoList: ArrayList<ItemInfomation> = arrayListOf<ItemInfomation>()
            if (!result.equals("[]")) {
                for (i in 0..(itemListArray.length() - 1)) {
                    itemInfoList.add(ItemInfomation(itemListArray.getJSONObject(i)))
                }
            }
            val adapter = ItemListAdapter(itemInfoList)
            recyclerview_users.setLayoutManager(LinearLayoutManager(this))
            recyclerview_users.setAdapter(adapter)

        }

        btn_users_report.setOnClickListener {
            btn_users_report.setBackgroundResource(R.drawable.custom_login_button)
            btn_users_report.setTextColor(R.color.main_color)
            btn_users_item.setBackgroundResource(R.drawable.custom_solid_buttom)
            btn_users_item.setTextColor(R.color.white)

            val task = GetReportTask()
            var result = task.execute(User.getString("userID"),"Get_report_user").get()

            val reportListArray = JSONArray(result)
            val reportList: ArrayList<ReportObject> = arrayListOf<ReportObject>()
            if (!result.equals("[]")) {
                for (i in 0..(reportListArray.length() - 1)) {
                    reportList.add(ReportObject(reportListArray.getJSONObject(i)))
                }
            }
            val adapter = ReportListAdapter(reportList)
            recyclerview_users.setLayoutManager(LinearLayoutManager(this))
            recyclerview_users.setAdapter(adapter)
        }
    }
}