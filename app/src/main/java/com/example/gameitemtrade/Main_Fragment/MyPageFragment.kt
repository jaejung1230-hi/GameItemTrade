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
import com.example.gameitemtrade.Chat.ChatRoomActivity
import com.example.gameitemtrade.Data.User
import com.example.gameitemtrade.MySelectItemActivity
import com.example.gameitemtrade.R
import com.example.gameitemtrade.Tasks.ChatRoomTask
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

        val btn_update_profile = v.findViewById(R.id.btn_update_profile) as Button
        val btn_my_chat = v.findViewById(R.id.btn_my_chat) as LinearLayout
        val btn_my_bookmark = v.findViewById(R.id.btn_my_bookmark) as LinearLayout
        val btn_my_item = v.findViewById(R.id.btn_my_item) as LinearLayout

        btn_update_profile.setOnClickListener {
            var intent = Intent(requireContext(), ChangeProfileActivity::class.java)
            startActivity(intent)
        }

        btn_my_chat.setOnClickListener {
            var intent = Intent(requireContext(), ChatRoomActivity::class.java)
            startActivity(intent)
        }
        btn_my_bookmark.setOnClickListener {
            var intent = Intent(requireContext(), MySelectItemActivity::class.java)
            intent.putExtra("function","Find_BookMark")
            startActivity(intent)
        }
        btn_my_item.setOnClickListener {
            var intent = Intent(requireContext(), MySelectItemActivity::class.java)
            intent.putExtra("function","Find_MyItem")
            startActivity(intent)
        }

        tv_loginusername.setText("???????????????! "+ User.userID+"???!")
        Picasso.get().load(User.profilePicture).error(R.drawable.nonephoto).into(img_profile);

        layout_profile.setOnClickListener {
            var intent = Intent(requireContext(), UserInfoActivity::class.java)
            intent.putExtra("select_user", User.userID)
            startActivity(intent)
        }

        //val crTask = ChatRoomTask()
        //val result = crTask.execute(userId,"Find_ChattingRoom").get()
/* ????????? ????????? ????????????
        val adapter = userId?.let { GameListAdapter(game_array.values(), it) }
        recyclerview_chatlist.setLayoutManager(LinearLayoutManager(requireContext()));
        recyclerview_chatlist.setAdapter(adapter);
*/
        return v
    }
}