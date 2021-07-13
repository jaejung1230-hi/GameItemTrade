package com.example.gameitemtrade.Main_Fragment

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.gameitemtrade.DetailItemInfoActivity
import com.example.gameitemtrade.Data.ItemInfomation
import com.example.gameitemtrade.Data.ReportObject
import com.example.gameitemtrade.DetailReportActivity
import com.example.gameitemtrade.R
import com.squareup.picasso.Picasso

class ReportListAdapter(private val items: ArrayList<ReportObject>) : RecyclerView.Adapter<ReportListAdapter.ViewHolder>() {

    //RecyclerView 초기화때 호출된다.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflatedView = LayoutInflater.from(parent.context)
            .inflate(R.layout.reportlist_recycler, parent, false)
        return ViewHolder(
            inflatedView
        )
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val listener = View.OnClickListener {it ->
            var intent = Intent(it.context, DetailReportActivity::class.java)
            intent.putExtra("report",items[position])
            it.context.startActivity(intent)
        }
        holder.bind(listener, items[position])
    }

    //ViewHolder 단위 객체로 View의 데이터를 설정합니다
    class ViewHolder(private var v: View) : RecyclerView.ViewHolder(v){
        val reportlist_recycler_name: TextView = v.findViewById(R.id.reportlist_recycler_name)
        val reportlist_recycler_suspect: TextView = v.findViewById(R.id.reportlist_recycler_suspect)
        val reportlist_recycler_explain: TextView = v.findViewById(R.id.reportlist_recycler_explain)

        fun bind(listener: View.OnClickListener, item : ReportObject){
            v.setOnClickListener(listener)
            reportlist_recycler_name.setText(item.reason)
            reportlist_recycler_suspect.setText(item.suspect)
            reportlist_recycler_explain.setText("${item.gameTitle} / ${item.gameServer} / ${item.kind}")

        }
    }
}

