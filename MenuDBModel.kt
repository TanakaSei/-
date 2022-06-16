package your_packagename

import io.realm.RealmObject
import  io.realm.annotations.PrimaryKey
import io.realm.annotations.Required
import  java.util.*
open class MenuDBModel: RealmObject() {
    @PrimaryKey
    var id : Int? = null
    var category1: String = ""
    var category2: String = ""
    var Flag: Boolean? = null
    var name: String = ""
}
