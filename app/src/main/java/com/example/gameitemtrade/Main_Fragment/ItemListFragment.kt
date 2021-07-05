package com.example.gameitemtrade.Main_Fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gameitemtrade.*
import com.example.gameitemtrade.Data.ItemInfomation
import com.example.gameitemtrade.Tasks.GetItemTask
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.json.JSONArray

class ItemListFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v: View = inflater.inflate(R.layout.fragment_item_list, container, false)

        val spinner_kindSearch = v.findViewById(R.id.spinner_kindSearch) as Spinner
        val spinner_serverSearch = v.findViewById(R.id.spinner_serverSearch) as Spinner
        val btn_writeItem = v.findViewById(R.id.btn_writeItem) as FloatingActionButton
        val btn_itemSearch = v.findViewById(R.id.btn_itemSearch) as TextView
        val recyclerview_itemlist = v.findViewById(R.id.recyclerview_itemlist) as RecyclerView
        val edit_itemSearch = v.findViewById(R.id.edit_itemSearch) as EditText
        val tv_itemNull = v.findViewById(R.id.tv_itemNull) as TextView

        val gameTitle = arguments?.getString("gameTitle")

        spinner_kindSearch.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, arrayOf("게임머니", "장비 아이템", "캐시 아이템"))

        if (gameTitle.equals("메이플스토리")) {
            val clazz = maplestory_server_array.values()
            spinner_serverSearch.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, clazz)
        } else if (gameTitle.equals("로스트아크")) {
            val clazz = lostark_server_array.values()
            spinner_serverSearch.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, clazz)
        } else if (gameTitle.equals("던전앤파이터")) {
            val clazz = dungunandfighter_server_array.values()
            spinner_serverSearch.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, clazz)
        }

        val kindForSearch = spinner_kindSearch.selectedItem.toString()
        val serverForSearch = spinner_serverSearch.selectedItem.toString()
        val titleForsearch = edit_itemSearch.text.toString()

        val itemInfoList = getDBitemtoArrayList(gameTitle!!, kindForSearch, serverForSearch, titleForsearch)
        if (itemInfoList != null) {

            val adapter = ItemListAdapter(itemInfoList)
            recyclerview_itemlist.setLayoutManager(LinearLayoutManager(requireContext()))
            recyclerview_itemlist.setAdapter(adapter)
            recyclerview_itemlist.visibility = View.VISIBLE
            tv_itemNull.visibility = View.INVISIBLE
        } else {
            recyclerview_itemlist.visibility = View.INVISIBLE
            tv_itemNull.visibility = View.VISIBLE
        }

        btn_writeItem.setOnClickListener {
            var intent = Intent(requireContext(), AddItemActivity::class.java)
            startActivity(intent)
        }

        btn_itemSearch.setOnClickListener {
            val kindForSearch = spinner_kindSearch.selectedItem.toString()
            val serverForSearch = spinner_serverSearch.selectedItem.toString()
            val titleForsearch = edit_itemSearch.text.toString()

            val itemInfoList = getDBitemtoArrayList(gameTitle!!, kindForSearch, serverForSearch, titleForsearch)

            if (itemInfoList != null) {
                val adapter = ItemListAdapter(itemInfoList)
                recyclerview_itemlist.setLayoutManager(LinearLayoutManager(requireContext()))
                recyclerview_itemlist.setAdapter(adapter)
                recyclerview_itemlist.visibility = View.VISIBLE
                tv_itemNull.visibility = View.INVISIBLE
            } else {
                tv_itemNull.visibility = View.VISIBLE
                recyclerview_itemlist.visibility = View.INVISIBLE
            }

        }
        return v
    }
    fun getDBitemtoArrayList(gameTitle: String, kindForSearch: String, serverForSearch: String, titleForsearch: String): ArrayList<ItemInfomation>? {
        val tTask2 = GetItemTask()
        var result = tTask2.execute(gameTitle, kindForSearch, serverForSearch, titleForsearch,"Get_ItemList_Title").get()

        Log.d("test", "지금 검사중  : " + result)

        if (!result.equals("[]")) {
            val itemListArray = JSONArray(result)
            val itemInfoList: ArrayList<ItemInfomation> = arrayListOf<ItemInfomation>()
            for (i in 0..(itemListArray.length()-1)){
                itemInfoList.add(ItemInfomation(itemListArray.getJSONObject(i)))
            }
            return itemInfoList
        }
        return null
    }
}