package com.example.gameitemtrade.Main_Fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gameitemtrade.ChangeProfileActivity
import com.example.gameitemtrade.Data.User
import com.example.gameitemtrade.R
import com.example.gameitemtrade.UserInfoActivity
import com.squareup.picasso.Picasso

class MyPageFragment : Fragment() {

    lateinit var tv_loginusername : TextView
    lateinit var img_profile : ImageView

    override fun onResume() {
        super.onResume()
        tv_loginusername.setText(User.nickName)
        Picasso.get().load(User.profilePicture).error(R.drawable.nonephoto).into(img_profile);
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val v: View = inflater.inflate(R.layout.fragment_my_page, container, false)
        tv_loginusername = v.findViewById(R.id.tv_loginusername) as TextView
        img_profile = v.findViewById(R.id.img_profile) as ImageView
        val layout_profile = v.findViewById(R.id.layout_profile) as LinearLayout

        val recyclerview_chatlist = v.findViewById(R.id.recyclerview_chatlist) as RecyclerView
        val btn_temp = v.findViewById(R.id.btn_temp) as Button

        btn_temp.setOnClickListener {
            var intent = Intent(requireContext(), ChangeProfileActivity::class.java)
            startActivity(intent)
        }

        tv_loginusername.setText("환영합니다! "+ User.userID+"님!")
        Picasso.get().load(User.profilePicture).error(R.drawable.nonephoto).into(img_profile);

        layout_profile.setOnClickListener {
            var intent = Intent(requireContext(), UserInfoActivity::class.java)
            intent.putExtra("select_user", User.userID)
            startActivity(intent)
        }

        //val crTask = ChatRoomTask()
        //val result = crTask.execute(userId,"Find_ChattingRoom").get()
/* 나중에 리스트 필요하면
        val adapter = userId?.let { GameListAdapter(game_array.values(), it) }
        recyclerview_chatlist.setLayoutManager(LinearLayoutManager(requireContext()));
        recyclerview_chatlist.setAdapter(adapter);
*/
        return v
    }
}