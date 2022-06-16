package com.example.soramban.myapplication

import android.app.ActionBar
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import io.realm.Realm
import io.realm.kotlin.where

class ShowMenuActivity :AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_list)
        val realm = Realm.getDefaultInstance()
        val actionBar: androidx.appcompat.app.ActionBar? = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        val maxId = realm.where<MenuDBModel>().max("id")
        val minId = realm.where<MenuDBModel>().min("id")
        var menuList :String = ""

        for (id in (1..maxId!!.toInt())){
            var menu = realm.where<MenuDBModel>().equalTo("id", id).findFirst()
            menuList = menuList +"・" + menu!!.name + "\n"
        }

        var result_text = findViewById<TextView>(R.id.menuList)
        //result_text.text = menuList.toString()
        result_text.text = menuList
    }

    override fun onSupportNavigateUp(): Boolean {
        val intent = Intent(application, MainActivity::class.java)
        startActivity(intent)
        finish()
        return super.onSupportNavigateUp()
    }
}