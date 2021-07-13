package com.example.gameitemtrade

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import com.example.gameitemtrade.Data.ItemInfomation
import com.example.gameitemtrade.Data.ReportObject
import com.example.gameitemtrade.Data.User
import com.squareup.picasso.Picasso

class DetailReportActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_report)

        val iv_itemImage_report_ditail = findViewById(R.id.iv_itemImage_report_ditail) as ImageView
        val report_target_ditail = findViewById(R.id.report_target_ditail) as TextView
        val report_suspect_ditail = findViewById(R.id.report_suspect_ditail) as TextView
        val report_itemTitle_ditail = findViewById(R.id.report_itemTitle_ditail) as TextView
        val report_price_ditail = findViewById(R.id.report_price_ditail) as TextView
        val report_game_server_kind_ditail = findViewById(R.id.report_game_server_kind_ditail) as TextView
        val report_reason_ditail = findViewById(R.id.report_reason_ditail) as TextView
        val report_explain_ditail = findViewById(R.id.report_explain_ditail) as TextView

        val secondIntent = intent
        val report = secondIntent.getParcelableExtra<ReportObject>("report")

        Log.d("aaatest",report?.target.toString())
        Log.d("aaatest",report?.suspect.toString())
        Log.d("aaatest",report?.reason.toString())
        Log.d("aaatest",(report?.reaport_explain.toString()))

        Picasso.get().load(report?.fileName).error(R.drawable.nonephoto).into(iv_itemImage_report_ditail)
        report_target_ditail.setText(report?.target)
        report_suspect_ditail.setText(report?.suspect)
        report_itemTitle_ditail.setText(report?.title)
        report_price_ditail.setText(report?.price.toString()+"원")
        report_game_server_kind_ditail.setText("${report?.gameTitle} / ${report?.gameServer} / ${report?.kind}")
        report_reason_ditail.setText(report?.reason)
        report_explain_ditail.setText(report?.reaport_explain)

    }
}