package your_packagename

import io.realm.Realm
import io.realm.kotlin.createObject
import io.realm.kotlin.where

private val menu = arrayOf("もも貴族焼 たれ","もも貴族焼 塩","もも貴族焼 スパイス","むね貴族焼 たれ","むね貴族焼 塩","むね貴族焼 スパイス","せせり -ガーリック入-","つくね塩","ハート塩 ‐ガーリック入-","かわ塩","砂ずり (砂肝)","ひざなんこつ","やげんなんこつ","手羽先塩","三角 (ぼんじり)","ちからこぶ塩","ささみ塩焼 -わさび添え-","つくねたれ","ハートたれ","かわたれ","きも (レバー)","つくねチーズ焼","ちからこぶたれ","みたれ (もも肉)","手羽先たれ","もちもちチーズ焼","豚バラ串焼","ピーマン肉詰 -ポン酢味-","牛串焼 -果実とにんにくの旨味-","きも焼 -塩ごま油添え-","むね明太マヨ焼","枝豆","キャベツ盛","キャベツ盛+ごま油","親鶏炙り焼 -塩ポン酢味-","ホルモンねぎ盛ポン酢","味付煮玉子","超!白ねぎ塩こんぶ","冷やしトマト","塩だれキューリ","ささみの燻製","カマンベールコロッケ","ひざなんこつ唐揚","トリキの唐揚","とり天 -梅肉ソース添え-","チキン南蛮","鶏皮チップ","北海道 海と大地のポテトサラダ","ふんわり山芋の鉄板焼","ポテトフライ","焼とり丼","とり釜飯","とり雑炊","とり白湯めん","白ごはん (味噌汁付)","チョコパフェｰチュロ添え-","カタラーナアイス","ザ・プレミアム・モルツ","メガ金麦 (ビール系飲料)","メガハイボール (ジムビーム)","ゆずはちみつ","白桃カルピス","男梅サワー","こだわり酒場のレモンサワー","濃いめのレモンサワー","優しいレモンサワー","角瓶","ジムビームホワイト","知多","角ハイボール","知多ハイボール","コーラハイ (ウイスキーandコーラ)","ジンジャーハイ (ウイスキーandジンジャーエール)","ウーロンハイ","緑茶ハイ","翠ジンソーダ","優しいカシスオレンジ割","優しいカシスミルク割","優しいレモンとカシス","優しいみかんのお酒 オレンジ割","トリキホワイト","トリキレッド","黒霧島(芋)","よかいち (麦)","松竹梅 豪快 純米酒 (カップ酒)","紀州の南高梅酒","オールフリー","ウーロン茶","オレンジジュース","コーラ","ジンジャーエール","レモネード","レモンスカッシュ","ホットウーロン茶","ミックスジュース","大人のジンジャーソーダ","大人のはちみつレモン","大人のホットジンジャー","やさしい苺とミルク","ザップチューハイ","ジャージー乳ヨーグルトのお酒 ロック","果肉が嬉しいいちじくチューハイ","優しいヨーグルトミルク~ジャージー乳ヨーグルト使用~","大人になってもクリームソーダーチューハイ メロン","大人になってもクリームソーダ・チューハイ ミックスベリー","大人になってもクリームソーダーチューハイ フルーツ","クリームソーダ (ソフトドリンク) メロン","クリームソーダ (ソフトドリンク) ミックスベリー","クリームソーダ (ソフトドリンク) ミックスフルーツ","タンドリーちからこぶ","ゴロゴロ野菜のチキンカレーコロッケ","カレー辛麺","やわらか鶏天カレー","フライチキンピカタ -4種のタルタル-","チーズとトマトのフリット -メイプルソース-","さくら明太マヨうどん","タマポテ鉄板焼","〆に向かないカレーアイス","スイートポテトパフェ")
private val category1List = arrayOf("焼き鳥","サブメニュー","ドリンク","フェアメニュー")
private val category2List = arrayOf("貴族焼","塩焼","たれ焼","串焼","スピードメニュー","逸品料理","デザート","酒","ノンアルコール","ソフトドリンク","フェアメニュー（ドリンク）","フェアメニュー（逸品）","フェアメニュー（デザート）")
private val category1NumList = arrayOf(32,58,100)
private val category2NumList = arrayOf(7,18,26,32,42,56,58,87,88,100,110,118)
class MenuDB {
    fun makeDB(){
        var category1Num = 0
        var category2Num = 0

        val realm = Realm.getDefaultInstance()
        var id = 1
        for(i in menu){
            realm.executeTransaction {
                val maxId = realm.where<MenuDBModel>().max("id")
                val nextId = (maxId?.toLong()?:0L) + 1L
                var obj = realm.createObject<MenuDBModel>(nextId)
                if(category1Num < category1NumList.size && nextId.toInt() == category1NumList[category1Num])
                    category1Num ++
                if(category2Num < category2NumList.size && nextId.toInt() == category2NumList[category2Num])
                    category2Num ++
                obj.category1 = category1List[category1Num]
                obj.category2 = category2List[category2Num]
                obj.name = i
                obj.Flag = true
            }

        }
    }

    fun searchDB(id:Int): MenuDBModel? {
        val realm = Realm.getDefaultInstance()
        val result = realm.where(MenuDBModel::class.java).equalTo("id",id).findFirst()
        return result
    }

}
