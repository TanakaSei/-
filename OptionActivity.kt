package com.example.soramban.myapplication

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle

import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.Toast
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity
import io.realm.Realm
import io.realm.kotlin.where
import kotlin.properties.Delegates


class OptionActivity:AppCompatActivity() {
    var num = 2
    var duplicationFlg = true
    val btnList = mutableListOf<Int>(R.id.a,R.id.b,R.id.c,R.id.d,R.id.e,R.id.f,R.id.g,R.id.h,R.id.i,R.id.j,R.id.k,R.id.l,R.id.m,R.id.n)
    var btnNumList = mutableListOf<ToggleButton>()

    override fun onCreate(savedInstanceState: Bundle?) {
        val dataStore: SharedPreferences = getSharedPreferences("DataStore", Context.MODE_PRIVATE)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_option)

        val menuNum = dataStore.getInt("NUMBER", 2)
        var booleanFlg = dataStore.getBoolean("FLG", false)
        var recordFlg:Boolean?
        num = menuNum
        duplicationFlg = booleanFlg

        val realm = Realm.getDefaultInstance()
        val actionBar: androidx.appcompat.app.ActionBar? = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)

        //seekBarの制御
        val seekBar = findViewById<SeekBar>(R.id.numberSeekBar)
        seekBar.setProgress(menuNum)

        //ToggleButtonの制御
        btnNumList.add(findViewById<ToggleButton>(btnList[0]))
        btnNumList[0].setChecked(booleanFlg)

        val minId = 1
        val maxId = realm.where<MenuDBModel>().max("id")!!.toInt()
        var record = realm.where<MenuDBModel>().equalTo("id", minId).findFirst()
        var nextCategory:MenuDBModel? = realm.where<MenuDBModel>().notEqualTo("category2",record?.category2).greaterThan("id",record?.id!!).findFirst()
        var whenCategoryChangeIdList = mutableListOf<Int?>(1,nextCategory!!.id!!)//カテゴリー２の境界id
        for(i in 1..13) {
            if(i>1){
                //カテゴリー２の変わるタイミングのId検索
                record = nextCategory!!
                nextCategory = realm.where<MenuDBModel>().notEqualTo("category2",record?.category2).greaterThan("id",record?.id!!).findFirst()
                if(nextCategory != null){
                    whenCategoryChangeIdList.add((nextCategory.id!!))
                }
                else if(nextCategory == null) {
                    nextCategory = record
                    whenCategoryChangeIdList.add(null)

                }
            }
            realm.executeTransaction {
                recordFlg = record?.Flag
                btnNumList.add(findViewById<ToggleButton>(btnList[i]))
                btnNumList[i].setChecked(recordFlg!!)
            }
        }
        //toggleButton押された際のDB書き換え
        //record = realm.where<MenuDBModel>().equalTo("id",minId).findFirst()

        for(i in 0..13){
            btnNumList[i].setOnCheckedChangeListener{
                buttonView, isChecked ->
                run {

                    //押されたボタンが何番目か検索
                    var btnId = buttonView.toString().toCharArray()
                    var btnNum = btnId[btnId.size - 2].toChar() - 'a'

                    //重複が押されたら真偽逆転
                    if (btnNum == 0) {
                        booleanFlg = !booleanFlg
                        duplicationFlg = booleanFlg
                    }

                    else {
                        
                        realm.executeTransaction {
                            //該当レコードの切り替え
                            var endNum by Delegates.notNull<Int>()
                            if(whenCategoryChangeIdList[i] != null) {
                                endNum = whenCategoryChangeIdList[i]!! - 1
                            }
                            if(whenCategoryChangeIdList[i] == null)
                                endNum = maxId
                            if(whenCategoryChangeIdList[i-1]==endNum){
                                var currentRecord = realm.where<MenuDBModel>().equalTo("id",endNum).findFirst()
                                currentRecord!!.Flag = !(currentRecord.Flag!!)
                            }
                            else {
                                for (id in whenCategoryChangeIdList[i - 1]!!..endNum) {
                                    var tmp =
                                        realm.where<MenuDBModel>().equalTo("id", id).findFirst()
                                    tmp!!.Flag = !(tmp.Flag!!)
                                }
                            }

                            //全てオフになった場合の処理
                            var flg = false
                            for(id in whenCategoryChangeIdList){
                                if(id == null)
                                    break
                                else if(realm.where<MenuDBModel>().equalTo("id",id).findFirst()!!.Flag!!) {
                                    flg = true
                                    break
                                }
                            }
                            if(!flg){
                                Toast.makeText(applicationContext, "全てオフにはできません", Toast.LENGTH_SHORT).show()
                                btnNumList[i].setChecked(true)
                                var endNum by Delegates.notNull<Int>()
                                if(whenCategoryChangeIdList[i] != null) {
                                    endNum = whenCategoryChangeIdList[i]!! - 1
                                }
                                if(whenCategoryChangeIdList[i] == null)
                                    endNum = maxId
                                if(whenCategoryChangeIdList[i-1]==endNum){
                                    var currentRecord = realm.where<MenuDBModel>().equalTo("id",endNum).findFirst()
                                    currentRecord!!.Flag = true
                                }
                                else {
                                    for (id in whenCategoryChangeIdList[i - 1]!!..endNum) {
                                        var currentRecord =
                                            realm.where<MenuDBModel>().equalTo("id", id).findFirst()
                                        currentRecord!!.Flag = true
                                    }
                                }
                            }

                        }
                    }

                }
            }
        }

        seekBar.setOnSeekBarChangeListener(controlSeekbar)
    }

    val controlSeekbar = object:  OnSeekBarChangeListener {
            //ツマミがドラッグされた
        override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                num = progress
        }
        //ツマミがタップされた
        override fun onStartTrackingTouch(seekBar: SeekBar) {}
        //ツマミが離された
        override fun onStopTrackingTouch(seekBar: SeekBar) {
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }

    override fun onDestroy() {
        super.onDestroy()
        val dataStore: SharedPreferences = getSharedPreferences("DataStore", Context.MODE_PRIVATE)
        val editor = dataStore.edit()
        editor.putInt("NUMBER", num)
        editor.putBoolean("FLG", duplicationFlg)
        editor.commit()

    }
}