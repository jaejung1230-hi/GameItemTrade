package com.example.gameitemtrade

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.gameitemtrade.Data.dungunandfighter_server_array
import com.example.gameitemtrade.Data.game_array
import com.example.gameitemtrade.Data.lostark_server_array
import com.example.gameitemtrade.Data.maplestory_server_array
import com.example.gameitemtrade.Tasks.UploadTask
import com.google.android.gms.location.*

class AddItemActivity : AppCompatActivity() {
    private val GET_GALLERY_IMAGE = 200
    private val GET_LOCATION = 3000
    private var iv_itemImage : ImageView? = null
    private var img_path : String? = null
    private var imageName : String? = null
    val PERMISSION_ID = 42
    lateinit var mFusedLocationClient: FusedLocationProviderClient
    var latitude : Double? = null
    var longitude : Double? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_item)
        val secondIntent = intent
        val userId = secondIntent.getStringExtra("userId")

        iv_itemImage = findViewById(R.id.iv_itemImage) as ImageView
        val edit_itemTitle = findViewById(R.id.edit_itemTitle) as EditText
        val edit_itemPrice = findViewById(R.id.edit_itemPrice) as EditText
        val spinner_itemKinds = findViewById(R.id.spinner_itemKinds) as Spinner
        val spinner_gameTitle = findViewById(R.id.spinner_gameTitle) as Spinner
        val spinner_gameServer = findViewById(R.id.spinner_gameServer) as Spinner
        val edit_itemExplain = findViewById(R.id.edit_itemExplain) as EditText
        val btn_postItem = findViewById(R.id.btn_postItem) as Button
        val btn_location = findViewById(R.id.btn_location) as Button

        btn_postItem.setOnClickListener {
            val testTask = UploadTask()
            val result = testTask.execute(
                    img_path,
                    edit_itemTitle.text.toString(),
                    edit_itemPrice.text.toString(),
                    edit_itemExplain.text.toString(),
                    spinner_itemKinds.selectedItem.toString(),
                    spinner_gameTitle.selectedItem.toString(),
                    spinner_gameServer.selectedItem.toString(),
                   latitude.toString(),longitude.toString()).get()
            if(result.equals("Clear")){Toast.makeText(this, "등록성공!!", Toast.LENGTH_SHORT).show(); finish()}
        }
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        btn_location.setOnClickListener {
            val intent = Intent(this,ChoicePlaceActivity::class.java)
            startActivityForResult(intent,GET_LOCATION)

        }

        spinner_itemKinds.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, arrayOf("게임머니","장비 아이템","캐시 아이템"))
        spinner_gameTitle.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, game_array.values())
        val context = this

        spinner_gameTitle.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                Log.d("test", game_array.values()[position].games)
                val selectedgame = game_array.values()[position].games
                //val clazz  = Class.forName(selectedgame)
                // 여기를 단축시켜야 하는데 string으로 클래스를 만드려면 어떻게 해야 할까...
                if(selectedgame.equals("maplestory_server_array")){
                   val clazz = maplestory_server_array.values()
                    spinner_gameServer.adapter = ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, clazz)
                }else if(selectedgame.equals("lostark_server_array")){
                    val clazz = lostark_server_array.values()
                    spinner_gameServer.adapter = ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, clazz)
                }else if(selectedgame.equals("dungunandfighter_server_array")){
                    val clazz = dungunandfighter_server_array.values()
                    spinner_gameServer.adapter = ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, clazz)
                }
            }
        }

        iv_itemImage?.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
            startActivityForResult(intent, GET_GALLERY_IMAGE)
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GET_GALLERY_IMAGE && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            img_path = data.getData()?.let { getImagePathToUri(it) }
            val image_bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
            iv_itemImage?.setImageBitmap(image_bitmap)
        }

        if(requestCode == GET_LOCATION){
            latitude = data?.getDoubleExtra("latitude",0.0)
            longitude = data?.getDoubleExtra("longitude",0.0)
            if(latitude  == 0.0 && longitude  == 0.0){
                latitude = null
                longitude = null
            }
            Log.d("test", "위치 수신 : " + latitude.toString());
            Log.d("test", "위치 수신 : " + longitude.toString());
        }
    }

    fun getImagePathToUri(data : Uri):String {

        var proj: Array<String> = arrayOf(MediaStore.Images.Media.DATA)
        val cursor: Cursor = managedQuery(data, proj, null, null, null);
        val column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();

        //이미지의 경로 값
        val imgPath:String = cursor.getString(column_index);
        Log.d("test", imgPath);

        //이미지의 이름 값
        val imgName :String = imgPath.substring(imgPath.lastIndexOf("/") + 1);
        Log.d("test", "이미지 이름 : " + imgName);
        this.imageName = imgName;

        return imgPath;
    }//end of getImagePathToUri()
}
