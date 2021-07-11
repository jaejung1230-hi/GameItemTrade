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
import com.example.gameitemtrade.R
import com.squareup.picasso.Picasso


class ItemListAdapter(private val items: ArrayList<ItemInfomation>) : RecyclerView.Adapter<ItemListAdapter.ViewHolder>() {

    //RecyclerView 초기화때 호출된다.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflatedView = LayoutInflater.from(parent.context)
            .inflate(R.layout.itemlist_recycler, parent, false)
        return ViewHolder(
            inflatedView
        )
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val listener = View.OnClickListener {it ->
            Toast.makeText(it.context, "기능 정상 수행", Toast.LENGTH_SHORT).show()
            var intent = Intent(it.context, DetailItemInfoActivity::class.java)
            intent.putExtra("item",items[position])
            it.context.startActivity(intent)
        }
        holder.bind(listener, items[position])
    }

    //ViewHolder 단위 객체로 View의 데이터를 설정합니다
    class ViewHolder(private var v: View) : RecyclerView.ViewHolder(v){
        val itemlist_recycler_name: TextView = v.findViewById(R.id.itemlist_recycler_name)
        val itemlist_recycler_price: TextView = v.findViewById(R.id.itemlist_recycler_price)
        val itemlist_recycler_image: ImageView = v.findViewById(R.id.itemlist_recycler_image)
        val itemlist_recycler_reserved:TextView = v.findViewById(R.id.itemlist_recycler_reserved)
        val itemlist_recycler_sold_out:TextView = v.findViewById(R.id.itemlist_recycler_sold_out)
        fun bind(listener: View.OnClickListener, item : ItemInfomation){
            v.setOnClickListener(listener)
            itemlist_recycler_name.setText(item.title)
            itemlist_recycler_price.setText(item.price.toString())
            Picasso.get().load(item.fileName).error(R.drawable.nonephoto).into(itemlist_recycler_image)
            when(item.state){
                "1" -> {itemlist_recycler_reserved.setVisibility(View.VISIBLE)}
                "2" -> {itemlist_recycler_sold_out.setVisibility(View.VISIBLE)}
            }
        }
    }
}

