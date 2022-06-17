package com.example.soramban.myapplication

import io.realm.Realm
import io.realm.kotlin.where

open class Lottery{
    val realm = Realm.getDefaultInstance()
    var exclusionIndexList = mutableListOf<Int>(1)
    //private val menu = arrayOf("もも貴族焼 たれ","もも貴族焼 塩","もも貴族焼 スパイス","むね貴族焼 たれ","むね貴族焼 塩","むね貴族焼 スパイス","せせり -ガーリック入-","つくね塩","ハート塩 ‐ガーリック入-","かわ塩","砂ずり (砂肝)","ひざなんこつ","やげんなんこつ","手羽先塩","三角 (ぼんじり)","ちからこぶ塩","ささみ塩焼 -わさび添え-","つくねたれ","ハートたれ","かわたれ","きも (レバー)","つくねチーズ焼","ちからこぶたれ","みたれ (もも肉)","手羽先たれ","もちもちチーズ焼","豚バラ串焼","ピーマン肉詰 -ポン酢味-","牛串焼 -果実とにんにくの旨味-","きも焼 -塩ごま油添え-","むね明太マヨ焼","枝豆","キャベツ盛","キャベツ盛+ごま油","親鶏炙り焼 -塩ポン酢味-","ホルモンねぎ盛ポン酢","味付煮玉子","超!白ねぎ塩こんぶ","冷やしトマト","塩だれキューリ","ささみの燻製","カマンベールコロッケ","ひざなんこつ唐揚","トリキの唐揚","とり天 -梅肉ソース添え-","チキン南蛮","鶏皮チップ","北海道 海と大地のポテトサラダ","ふんわり山芋の鉄板焼","ポテトフライ","焼とり丼","とり釜飯","とり雑炊","とり白湯めん","白ごはん (味噌汁付)","チョコパフェｰチュロ添え-","カタラーナアイス","ザ・プレミアム・モルツ","メガ金麦 (ビール系飲料)","メガハイボール (ジムビーム)","ゆずはちみつ","白桃カルピス","男梅サワー","こだわり酒場のレモンサワー","濃いめのレモンサワー","優しいレモンサワー","角瓶","ジムビームホワイト","知多","角ハイボール","知多ハイボール","コーラハイ (ウイスキーandコーラ)","ジンジャーハイ (ウイスキーandジンジャーエール)","ウーロンハイ","緑茶ハイ","翠ジンソーダ","優しいカシスオレンジ割","優しいカシスミルク割","優しいレモンとカシス","優しいみかんのお酒 オレンジ割","トリキホワイト","トリキレッド","黒霧島(芋)","よかいち (麦)","松竹梅 豪快 純米酒 (カップ酒)","紀州の南高梅酒","オールフリー","ウーロン茶","オレンジジュース","コーラ","ジンジャーエール","レモネード","レモンスカッシュ","ホットウーロン茶","ミックスジュース","大人のジンジャーソーダ","大人のはちみつレモン","大人のホットジンジャー","やさしい苺とミルク","ザップチューハイ","ジャージー乳ヨーグルトのお酒 ロック","果肉が嬉しいいちじくチューハイ","優しいヨーグルトミルク~ジャージー乳ヨーグルト使用~","大人になってもクリームソーダーチューハイ メロン","大人になってもクリームソーダ・チューハイ ミックスベリー","大人になってもクリームソーダーチューハイ フルーツ","クリームソーダ (ソフトドリンク) メロン","クリームソーダ (ソフトドリンク) ミックスベリー","クリームソーダ (ソフトドリンク) ミックスフルーツ","タンドリーちからこぶ","ゴロゴロ野菜のチキンカレーコロッケ","カレー辛麺","やわらか鶏天カレー","フライチキンピカタ -4種のタルタル-","チーズとトマトのフリット -メイプルソース-","さくら明太マヨうどん","タマポテ鉄板焼","〆に向かないカレーアイス","スイートポテトパフェ")

    fun startSelection(menuNum: Int, duplicateFlg:Boolean): MutableList<Int> {

        val maxId = realm.where<MenuDBModel>().max("id")!!.toInt()
        val minId = realm.where<MenuDBModel>().min("id")!!.toInt()

        var index = 0
        val selectedMenu = mutableListOf<Int>()
        var exclusionCategoryList = mutableListOf<String>("")   //除外する商品カテゴリーリスト
        val minRecord = realm.where<MenuDBModel>().equalTo("id", minId).findFirst()
        exclusionIndexList.add(realm.where<MenuDBModel>().notEqualTo("category2",minRecord?.category2).greaterThan("id",minRecord?.id!!).findFirst()!!.id!!.toInt())
        var allCategoryList = getAllCategory(minId) //重複していない状態での全ての商品カテゴリー(category2)を取得
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
                for (i in 0..selectNum - 1) {
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
                        for (j in (0..selectedMenu.size)){
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
                var findFlag = false
                var i = 0
                while(i < selectNum){
                    index = (1..maxId).random()
                    //初回のみ
                    if(i==0 && !exclusionChecker(index, ArrayList(exclusionCategoryList))) {
                        i++
                        selectedMenu.add(index)
                    }
                    //２回目以降
                    else if(i >= 1 && !exclusionChecker(index, ArrayList(exclusionCategoryList))){
                        var indexFlg = false
                        //既に確定したselectedMenuに全てとindexを比較して重複している要素がないか確認
                        for (j in (0..selectedMenu.size)) {
                            if (index == selectedMenu[j])
                                indexFlg = true
                        }
                        //重複していなければ次のメニュー決定へ
                        if (!indexFlg && exclusionChecker(index, ArrayList(exclusionCategoryList))){
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