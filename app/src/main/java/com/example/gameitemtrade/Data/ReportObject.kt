package com.example.gameitemtrade.Data

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parceler
import kotlinx.android.parcel.Parcelize
import org.json.JSONObject
import kotlin.properties.Delegates

@Parcelize
class ReportObject() : Parcelable {
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
    var state :String? = null
    lateinit var suspect : String
    lateinit var target: String
    lateinit var reason : String
    lateinit var reaport_explain : String

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
        this.state = item.getString("state")
        this.suspect = item.getString("suspect")
        this.target = item.getString("target")
        this.reason = item.getString("reason")
        this.reaport_explain = item.getString("reaport_explain")
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
            state = readString().toString()
            suspect = readString().toString()
            target = readString().toString()
            reason = readString().toString()
            reaport_explain = readString().toString()
            if(!latitude.equals("null") && !longitude.equals("null") ) {
                latitude = readString().toString()
                longitude = readString().toString()
            }
        }
    }
    private companion object : Parceler<ReportObject> {
        override fun ReportObject.write(parcel: Parcel, flags: Int) {
            parcel.writeString(productId)
            parcel.writeString(fileName)
            parcel.writeString(title)
            parcel.writeString(explain)
            parcel.writeInt(price)
            parcel.writeString(gameTitle)
            parcel.writeString(gameServer)
            parcel.writeString(kind)
            parcel.writeString(seller)
            parcel.writeString(state)
            parcel.writeString(suspect)
            parcel.writeString(target)
            parcel.writeString(reason)
            parcel.writeString(reaport_explain)
            if(!latitude.equals("null") && !longitude.equals("null")) {
                parcel.writeString(latitude!!)
                parcel.writeString(longitude!!)
            }

        }

        override fun create(parcel: Parcel): ReportObject {
            return ReportObject(parcel)
        }
    }
}