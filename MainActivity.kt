package com.example.soramban.myapplication

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.AppLaunchChecker
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_main.*



//var exclusionCategoryList = mutableListOf<String>("デザート","ノンアルコール","ソフトドリンク","フェアメニュー（逸品）","フェアメニュー（デザート）","フェアメニュー（ドリンク）","貴族焼","塩焼","たれ焼","串焼","スピードメニュー")
class MainActivity : AppCompatActivity() {
    private lateinit var realm: Realm
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val dataStore: SharedPreferences = getSharedPreferences("DataStore", Context.MODE_PRIVATE)
        val selectNumber = 3
        var duplicateFlg:Boolean = true
        val editor = dataStore.edit()
        editor.putInt("NUMBER", selectNumber)
        editor.putBoolean("FLG", duplicateFlg)
        editor.apply()

        //アクションバー非表示
        val actionBar: ActionBar? = supportActionBar
        if (actionBar != null) {
            actionBar.hide()
        }

        realm = Realm.getDefaultInstance()
        //デバック用
        val menuDB = MenuDB()
        //menuDB.makeDB()
        //初回起動判定
        val checker = AppLaunchChecker.hasStartedFromLauncher(applicationContext)
        if (!checker){
            println("初回起動")
            AppLaunchChecker.onActivityCreate(this)
            menuDB.makeDB()

        }
        else{
            println("not初回起動")
        }

        val selectBtn = findViewById<Button>(R.id.select_start)
        val optionBtn = findViewById<Button>(R.id.option)
        val menuBtn = findViewById<Button>(R.id.menu)

        selectBtn.setOnClickListener(selectActivityStart)
        optionBtn.setOnClickListener(optionActivityStart)
        menuBtn.setOnClickListener(menuActivityStart)

    }

    val selectActivityStart = object:View.OnClickListener {

        override fun onClick(view: View){
            val menuDB = MenuDB()
            val intent = Intent(applicationContext, LotteryResult::class.java)
            startActivity(intent)
        }
    }

    val optionActivityStart = object:View.OnClickListener {
        override fun onClick(view: View){
            val intent = Intent(applicationContext, OptionActivity::class.java)
            //intent.putStringArrayListExtra("List", ArrayList(exclusionCategoryList))
            intent.putExtra("FLG", false)
            startActivity(intent)
            //startActivityForResult(intent, selectNum)

        }
    }

    val menuActivityStart = object:View.OnClickListener {
        override fun onClick(view: View){
            val intent = Intent(applicationContext, ShowMenuActivity::class.java)
            //intent.putExtra("MENU_NAME", 3)
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }

}
