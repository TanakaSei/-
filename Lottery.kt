package yourpackage

import io.realm.Realm
import io.realm.kotlin.where

open class Lottery{
    val realm = Realm.getDefaultInstance()
    var exclusionIndexList = mutableListOf<Int>(1)

    fun startSelection(menuNum: Int, duplicateFlg:Boolean): MutableList<Int> {

        val maxId = realm.where<MenuDBModel>().max("id")!!.toInt()
        val minId = realm.where<MenuDBModel>().min("id")!!.toInt()

        var index = 0
        val selectedMenu = mutableListOf<Int>()
        var exclusionCategoryList = mutableListOf<String>("")   //除外する商品カテゴリーリスト
        var allCategoryList = getAllCategory(minId) //重複していない状態での全ての商品カテゴリー(category2)を取得
        println(exclusionIndexList)
        for(id in exclusionIndexList){
            val record = realm.where<MenuDBModel>().equalTo("id", id).findFirst()
            if(record!!.category2 == allCategoryList[index]){
                if(record!!.Flag == false)
                    exclusionCategoryList.add(allCategoryList[index])
                index ++
            }
        }

        var selectNum = getMenuAmount(minId)
        if(selectNum > menuNum)
            selectNum = menuNum

        //除外カテゴリー無し，メニュー重複あり
        if(exclusionCategoryList.size == 1){
            if (duplicateFlg) {
                for (i in 0 until selectNum) {
                    index = (0..maxId).random()
                    selectedMenu.add(index)
                }
            }
            //除外カテゴリー無し，メニュー重複なし
            else if(!duplicateFlg){
                var i = 0
                while(i < selectNum){
                    index = (1..maxId).random()
                    //初回のみ
                    if(i==0) {
                        i++
                        selectedMenu.add(index)
                    }
                    //２回目以降
                    else {
                        var indexFlg = false
                        //既に確定したselectedMenuに全てとindexを比較して重複している要素がないか確認
                        for (j in (0 until selectedMenu.size)){
                            if(index == selectedMenu[j])
                                indexFlg = true
                        }
                        //重複していなければ次のメニュー決定へ
                        if (!indexFlg){
                            i++
                            selectedMenu.add(index)
                        }
                    }

                }
            }
        } else{
            //除外カテゴリーあり，メニュー重複あり
            if (duplicateFlg){
                var i = 0
                //入力したメニュー数が決まるまで繰り返し
                while(i < selectNum){
                    index = (1..maxId).random()
                    if (exclusionChecker(index, ArrayList(exclusionCategoryList))){
                        continue
                    }
                    else{
                        i++
                        selectedMenu.add(index)
                    }
                }
            }
            //除外カテゴリーあり，メニュー重複なし
            else {
                var i = 0
                while(i < selectNum){
                    index = (1..maxId).random()
                    //初回のみ
                    if(i == 0 && !exclusionChecker(index, ArrayList(exclusionCategoryList))) {
                        i++
                        selectedMenu.add(index)
                    } //２回目以降
                    else if(i >= 1 && !exclusionChecker(index, ArrayList(exclusionCategoryList))){
                        var indexFlg = false
                        //既に確定したselectedMenuに全てとindexを比較して重複している要素がないか確認
                        for (j in (0 until selectedMenu.size)) {
                            if (index == selectedMenu[j])
                                indexFlg = true
                        }
                        //重複していなければ次のメニュー決定へ
                        if (!indexFlg){
                            i++
                            selectedMenu.add(index)
                        }
                    }

                }

            }
        }
        return selectedMenu
    }
    //重複が無ければfalseを返す
    fun exclusionChecker(index: Int,exclusionCategoryList:ArrayList<String>):Boolean{
        //除外カテゴリーチェック
        for(j in 1 until exclusionCategoryList.size) {
            var exclusion = realm.where<MenuDBModel>().equalTo("category2", exclusionCategoryList[j]).findFirst()
            var exclusion2 = exclusion?.category2
            //exclusionとカテゴリーが一致すればtrueを返して終了
            var compareExclusion = realm.where<MenuDBModel>().equalTo("id", index).findFirst()
            if (exclusion2 == (compareExclusion?.category2)) {
                return true
            }
        }
        return false
    }
    fun getAllCategory(minId:Int): MutableList<String> {
        var allCategoryList = mutableListOf<String>()
        var record = realm.where<MenuDBModel>().equalTo("id", minId).findFirst()
        var nextCategory:MenuDBModel? = realm.where<MenuDBModel>().notEqualTo("category2",record?.category2).greaterThan("id",record?.id!!).findFirst()
        allCategoryList.add(record.category2)
        allCategoryList.add(nextCategory!!.category2)
        while(true) {
            record = nextCategory!!
            nextCategory = realm.where<MenuDBModel>().notEqualTo("category2", record?.category2)
                .greaterThan("id", record?.id!!).findFirst()
            if (nextCategory != null) {
                allCategoryList.add((nextCategory.category2!!))
                exclusionIndexList.add(record.id!!)
            }
            else if (nextCategory == null) {
                exclusionIndexList.add(record.id!!)
                break
            }
        }
        return allCategoryList
    }
    fun getMenuAmount(minId:Int):Int{
        var menuNum:Int = 0
        var record = realm.where<MenuDBModel>().equalTo("id", minId).findFirst()
        var nextRecord:MenuDBModel? = realm.where<MenuDBModel>().greaterThan("id",record?.id!!).findFirst()
        while(true){
            if(record!!.Flag!!){
                menuNum++
            }
            if(nextRecord == null){
                break
            }
            record = nextRecord
            nextRecord = realm.where<MenuDBModel>().greaterThan("id",record?.id!!).findFirst()
        }
        return  menuNum
    }

}
