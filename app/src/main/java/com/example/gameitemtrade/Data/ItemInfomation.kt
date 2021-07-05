package com.example.gameitemtrade.Data

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parceler
import kotlinx.android.parcel.Parcelize
import org.json.JSONObject
import kotlin.properties.Delegates

@Parcelize
class ItemInfomation() : Parcelable {
    lateinit var productId : String
    lateinit var fileName : String
    lateinit var title : String
    lateinit var explain : String
    var price by Delegates.notNull<Int>()
    lateinit var gameTitle : String
    lateinit var gameServer: String
    lateinit var kind : String
    lateinit var seller : String
    var latitude :String? =null
    var longitude :String? = null

    constructor(item : JSONObject) : this() {
        this.productId = item.getString("productID")
        this.fileName = item.getString("imgurl")
        this.title = item.getString("title")
        this.explain = item.getString("explain")
        this.price = item.getInt("price")
        this.gameTitle = item.getString("game")
        this.gameServer = item.getString("server")
        this.kind = item.getString("itemkind")
        this.seller = item.getString("seller")
        if(!latitude.equals("null") && !longitude.equals("null") ) {
            this.latitude = item.getString("latitude")
            this.longitude = item.getString("longitude")
                        }
    }

    constructor(parcel: Parcel): this() {
        parcel.run {
            productId = readString().toString()
            fileName = readString().toString()
            title = readString().toString()
            explain = readString().toString()
            price = readInt()
            gameTitle = readString().toString()
            gameServer = readString().toString()
            kind = readString().toString()
            seller = readString().toString()
            if(!latitude.equals("null") && !longitude.equals("null") ) {
                latitude = readString().toString()
                longitude = readString().toString()
            }
        }
    }
    private companion object : Parceler<ItemInfomation> {
        override fun ItemInfomation.write(parcel: Parcel, flags: Int) {
            parcel.writeString(productId)
            parcel.writeString(fileName)
            parcel.writeString(title)
            parcel.writeString(explain)
            parcel.writeInt(price)
            parcel.writeString(gameTitle)
            parcel.writeString(gameServer)
            parcel.writeString(kind)
            parcel.writeString(seller)
            if(!latitude.equals("null") && !longitude.equals("null")) {
                parcel.writeString(latitude!!)
                parcel.writeString(longitude!!)
            }

        }

        override fun create(parcel: Parcel): ItemInfomation {
            return ItemInfomation(parcel)
        }
    }
}