package com.example.gameitemtrade.Main_Fragment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.example.gameitemtrade.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class PageActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener{
    lateinit var gameTitle:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_page)
        val navigation = findViewById(R.id.navigation) as BottomNavigationView
        navigation.setOnNavigationItemSelectedListener(this)

        val secondIntent = intent
        gameTitle = secondIntent.getStringExtra("gameTitle").toString()
    }

    override fun onNavigationItemSelected(p0: MenuItem): Boolean {

        when(p0.itemId){
            R.id.action_list ->{
                var itemListFragment = ItemListFragment()
                var bundle = Bundle()
                bundle.putString("gameTitle",gameTitle)
                itemListFragment.arguments = bundle
                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.frame,itemListFragment)
                transaction.commit()
                return true }

            R.id.action_map ->{
                var mapFragment = MapFragment()
                var bundle = Bundle()
                bundle.putString("gameTitle",gameTitle)
                mapFragment.arguments = bundle
                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.frame,mapFragment)
                transaction.commit()
                return true }


            R.id.action_mypage ->{
                var myPageFragment = MyPageFragment()
                var bundle = Bundle()
                myPageFragment.arguments = bundle
                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.frame,myPageFragment)
                transaction.commit()
                return true }

                /*
            R.id.action_report ->{
                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.frame,MainFragment())
                transaction.commit()
                return true }
             */
        }
        return false
    }
}