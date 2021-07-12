package com.example.gameitemtrade

import android.app.AlertDialog
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import com.example.gameitemtrade.Data.ItemInfomation
import com.example.gameitemtrade.Data.User
import com.example.gameitemtrade.Tasks.*
import com.squareup.picasso.Picasso
import org.json.JSONArray

class PostReportActivity : AppCompatActivity() {
    lateinit var iv_itemImage_report : ImageView
    lateinit var report_explain : EditText
    lateinit var report_reason : Spinner
    lateinit var report_itemTitle : TextView
    lateinit var report_price : TextView
    lateinit var report_gametitle : TextView
    lateinit var report_gameserver : TextView
    lateinit var report_suspect : TextView
    lateinit var reportitem : ItemInfomation
    lateinit var productID : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_report)
        iv_itemImage_report = findViewById(R.id.iv_itemImage_report) as ImageView
        report_explain = findViewById(R.id.report_explain) as EditText
        report_reason = findViewById(R.id.report_reason) as Spinner
        report_itemTitle = findViewById(R.id.report_itemTitle) as TextView
        report_price = findViewById(R.id.report_price) as TextView
        report_gametitle = findViewById(R.id.report_gametitle) as TextView
        report_gameserver = findViewById(R.id.report_gameserver) as TextView
        report_suspect = findViewById(R.id.report_suspect) as TextView

        val secondIntent = intent
        val suspect = secondIntent.getStringExtra("suspect").toString()
        productID = secondIntent.getStringExtra("productID").toString()

        val task = GetItemTask()
        val result = task.execute(productID,"Get_ItemList_ID").get()

        if (!result.equals("[]")) {
            reportitem = ItemInfomation(JSONArray(result).getJSONObject(0))

            Picasso.get().load(reportitem?.fileName).error(R.drawable.nonephoto).into(iv_itemImage_report)
            report_itemTitle.setText(reportitem?.title)
            report_price.setText("${reportitem?.price}원")
            report_gametitle.setText(reportitem?.gameTitle)
            report_gameserver.setText(reportitem?.gameServer)
            report_suspect.setText(suspect)
            report_reason.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, report_array.values())

        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.report_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(report_explain.text.length <= 20){
            Toast.makeText(this,"최소 20글자 이상의 내용을 기입해주세요", Toast.LENGTH_SHORT).show()
        }else{
            when(item.itemId) {
                R.id.post_report_end -> {
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle("정말 신고하시겠습니까?")
                    builder.setMessage("불확실한 신고는 제제의 대상이 될 수 있습니다.")
                    builder.setPositiveButton("예") { dialogInterface: DialogInterface, i: Int ->
                        val task = ReportTask()
                        val result = task.execute(productID, report_suspect.text.toString(),
                            report_reason.selectedItem.toString(), report_explain.text.toString()).get()
                        if(result.equals("Clear")){finish()
                        }else{
                            Toast.makeText(this,"같은 물품에 대한 반복 신고는 허용되지 않습니다.", Toast.LENGTH_SHORT).show()
                        }

                    }
                    builder.setNegativeButton("아니오") { dialogInterface: DialogInterface, i: Int ->
                    }

                    builder.show()
                }
            }
        }

        return super.onOptionsItemSelected(item)
    }
}