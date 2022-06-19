package yourpackage

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity

import io.realm.Realm
import io.realm.kotlin.where

class LotteryResult : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        val dataStore: SharedPreferences = getSharedPreferences("DataStore", Context.MODE_PRIVATE)
        super.onCreate(savedInstanceState)
        val lottery = Lottery()
        setContentView(R.layout.activity_lottery_result)

        //アクションバー非表示
        val actionBar: ActionBar? = supportActionBar
        if (actionBar != null) {
            actionBar.hide()
        }

        val realm = Realm.getDefaultInstance()
        val menuNum = dataStore.getInt("NUMBER", 2)
        val tf = dataStore.getBoolean("FLG", true)
        val maxId = realm.where<MenuDBModel>().max("id")
        val minId = realm.where<MenuDBModel>().min("id")!!.toInt()
        var flgList = mutableListOf<Boolean>()
        if (maxId != null) {
            var tmp = mutableListOf<String>()
            for (i in 1..(maxId!!.toInt())) {
                var record = realm.where<MenuDBModel>().equalTo("id",i).findFirst()
                tmp.add(record!!.category2)
                flgList.add(record.Flag!!)
            }
            val booleanFlg = dataStore.getBoolean("FLG", false)
            val results = arrayOf(R.id.result1,R.id.result2, R.id.result3)
            var i = 0
            var selectNum = lottery.getMenuAmount(minId)
            if (selectNum>=menuNum){
                selectNum = menuNum
                val dataStore: SharedPreferences = getSharedPreferences("DataStore", Context.MODE_PRIVATE)
                val editor = dataStore.edit()
                editor.putInt("NUMBER", selectNum)
                editor.commit()
            }
            else{
                Toast.makeText(applicationContext, "抽選対象が選択数に満たないため，$selectNum 個選択します", Toast.LENGTH_SHORT).show()
            }
            val menuId:MutableList<Int> = lottery.startSelection(menuNum, booleanFlg)

            while(i < selectNum){
                var menu = realm.where<MenuDBModel>().equalTo("id",menuId[i]).findFirst()
                var result_text = findViewById<TextView>(results[i])
                result_text.text = menu?.name
                i++

            }

            val backBtn = findViewById<Button>(R.id.backBtn)
            backBtn.setOnClickListener {
                finish()
            }
        }


    }
}
