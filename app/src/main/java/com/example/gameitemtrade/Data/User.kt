package com.example.gameitemtrade.Data

import org.json.JSONObject
import kotlin.properties.Delegates

object User {
    lateinit var userID:String
    lateinit var userPassword:String
    lateinit var nickName:String
    lateinit var profilePicture:String
    var attitude by Delegates.notNull<Double>()

    fun getInstance (log_in_user : JSONObject) {
        userID = log_in_user.getString("userID")
        userPassword = log_in_user.getString("userPassword")
        nickName = log_in_user.getString("nickName")
        profilePicture = log_in_user.getString("profilePicture")
        attitude = log_in_user.getDouble("attitude")
    }

    fun profileUpdate (profilePicture : String, nickName : String) {
        this.nickName = nickName
        this.profilePicture = profilePicture
    }
}

