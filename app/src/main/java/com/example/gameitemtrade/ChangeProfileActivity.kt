package com.example.gameitemtrade

import android.app.Activity
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.example.gameitemtrade.Data.User
import com.example.gameitemtrade.Tasks.UpdateProfileTask
import com.example.gameitemtrade.Tasks.UploadTask
import com.squareup.picasso.Picasso

class ChangeProfileActivity : AppCompatActivity() {
    lateinit var tv_for_change_profile : TextView
    lateinit var img_for_change_profile : ImageView
    lateinit var btn_for_change_profile : Button
    private val GET_GALLERY_IMAGE = 200
    private var img_path : String? = null
    private var imageName : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_profile)
        tv_for_change_profile = findViewById(R.id.tv_for_change_profile) as EditText
        img_for_change_profile = findViewById(R.id.img_for_change_profile) as ImageView
        btn_for_change_profile = findViewById(R.id.btn_for_change_profile) as Button
        tv_for_change_profile.setText(User.nickName)
        Picasso.get().load(User.profilePicture).error(R.drawable.nonephoto).into(img_for_change_profile);

        img_for_change_profile?.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
            startActivityForResult(intent, GET_GALLERY_IMAGE)
        }
        btn_for_change_profile.setOnClickListener {
            val testTask = UpdateProfileTask()
            val result = testTask.execute(img_path, tv_for_change_profile.text.toString()).get()
            Log.d("test", result)
            if(result.equals("Clear")){
                Log.d("test", "싱글톤 수정")
                if(img_path.isNullOrEmpty()){
                    User.profileUpdate(User.profilePicture, tv_for_change_profile.text.toString())
                }else{
                    User.profileUpdate(img_path!!, tv_for_change_profile.text.toString())
                }
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GET_GALLERY_IMAGE && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            img_path = data.getData()?.let { getImagePathToUri(it) }
            val image_bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
            img_for_change_profile?.setImageBitmap(image_bitmap)
        }
    }

    fun getImagePathToUri(data : Uri):String {

        var proj: Array<String> = arrayOf(MediaStore.Images.Media.DATA)
        val cursor: Cursor = managedQuery(data, proj, null, null, null);
        val column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();

        val imgPath:String = cursor.getString(column_index);
        Log.d("test", imgPath);

        val imgName :String = imgPath.substring(imgPath.lastIndexOf("/") + 1);
        Log.d("test", "이미지 이름 : " + imgName);
        this.imageName = imgName;

        return imgPath;
    }
}