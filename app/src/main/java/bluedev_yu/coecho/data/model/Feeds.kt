package bluedev_yu.coecho.data.model

import java.util.*
import kotlin.collections.HashMap

class Feeds(
    var feedImgUrl: String? = null,
    var uid: String? = null,
    var strName : String? = null,//사용자 이름
    var imageUrl : String?= null, //프로필사진
    var title : Int?= 0,  //칭호
    var content : String? = null,
    var timeStamp : Long?= null,
    var likeCnt: Int = 0,
    var commentCnt: Int =0,
    var hashtag: String? = null,
    var privacy: Boolean = false,
    var likes:MutableMap<String, String> = mutableMapOf())