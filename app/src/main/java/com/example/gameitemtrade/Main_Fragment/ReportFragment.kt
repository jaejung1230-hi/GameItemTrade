package com.example.gameitemtrade.Main_Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gameitemtrade.Data.*
import com.example.gameitemtrade.R
import com.example.gameitemtrade.Tasks.GetReportTask
import org.json.JSONArray

class ReportFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v: View = inflater.inflate(R.layout.fragment_report, container, false)

        val spinner_report_kindSearch = v.findViewById(R.id.spinner_report_kindSearch) as Spinner
        val spinner_report_serverSearch = v.findViewById(R.id.spinner_report_serverSearch) as Spinner
        val spinner_report_reasonSearch = v.findViewById(R.id.spinner_report_reasonSearch) as Spinner
        val btn_reportSearch = v.findViewById(R.id.btn_reportSearch) as TextView
        val edit_reportSearch = v.findViewById(R.id.edit_reportSearch) as EditText
        val recyclerview_reportlist = v.findViewById(R.id.recyclerview_reportlist) as RecyclerView
        val tv_reportNull = v.findViewById(R.id.tv_reportNull) as TextView

        val gameTitle = arguments?.getString("gameTitle")

        spinner_report_kindSearch.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, arrayOf("게임머니", "장비 아이템", "캐시 아이템"))
        spinner_report_reasonSearch.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, report_array.values())

        if (gameTitle.equals("메이플스토리")) {
            val clazz = maplestory_server_array.values()
            spinner_report_serverSearch.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, clazz)
        } else if (gameTitle.equals("로스트아크")) {
            val clazz = lostark_server_array.values()
            spinner_report_serverSearch.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, clazz)
        } else if (gameTitle.equals("던전앤파이터")) {
            val clazz = dungunandfighter_server_array.values()
            spinner_report_serverSearch.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, clazz)
        }

        val kindForSearch = spinner_report_kindSearch.selectedItem.toString()
        val serverForSearch = spinner_report_serverSearch.selectedItem.toString()
        val reasonForsearch = spinner_report_reasonSearch.selectedItem.toString()
        val userForsearch = edit_reportSearch.text.toString()

        val reportList = getDBitemtoArrayList(gameTitle!!, kindForSearch, serverForSearch, reasonForsearch, userForsearch)

        if (reportList != null) {
            val adapter = ReportListAdapter(reportList)
            recyclerview_reportlist.setLayoutManager(LinearLayoutManager(requireContext()))
            recyclerview_reportlist.setAdapter(adapter)
            recyclerview_reportlist.visibility = View.VISIBLE
            tv_reportNull.visibility = View.INVISIBLE
        } else {
            recyclerview_reportlist.visibility = View.INVISIBLE
            tv_reportNull.visibility = View.VISIBLE
        }

        btn_reportSearch.setOnClickListener {
            val kindForSearch = spinner_report_kindSearch.selectedItem.toString()
            val serverForSearch = spinner_report_serverSearch.selectedItem.toString()
            val reasonForsearch = spinner_report_reasonSearch.selectedItem.toString()
            val userForsearch = edit_reportSearch.text.toString()

            val reportList = getDBitemtoArrayList(gameTitle!!, kindForSearch, serverForSearch, reasonForsearch, userForsearch)

            if (reportList != null) {
                val adapter = ReportListAdapter(reportList)
                recyclerview_reportlist.setLayoutManager(LinearLayoutManager(requireContext()))
                recyclerview_reportlist.setAdapter(adapter)
                recyclerview_reportlist.visibility = View.VISIBLE
                tv_reportNull.visibility = View.INVISIBLE
            } else {
                recyclerview_reportlist.visibility = View.INVISIBLE
                tv_reportNull.visibility = View.VISIBLE
            }
        }
        return v
    }

    fun getDBitemtoArrayList(gameTitle: String, kindForSearch: String, serverForSearch: String, reasonForsearch: String, userForsearch:String): ArrayList<ReportObject>? {
        val task = GetReportTask()
        var result = task.execute(gameTitle, kindForSearch, serverForSearch, reasonForsearch, userForsearch,"Get_report_Detailed").get()

        if (!result.equals("[]")) {
            val reportListArray = JSONArray(result)
            val itemInfoList: ArrayList<ReportObject> = arrayListOf<ReportObject>()
            for (i in 0..(reportListArray.length()-1)){
                itemInfoList.add(ReportObject(reportListArray.getJSONObject(i)))
            }
            return itemInfoList
        }
        return null
    }

}