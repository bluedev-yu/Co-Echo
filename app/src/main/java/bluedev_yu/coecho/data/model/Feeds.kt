package bluedev_yu.coecho.data.model

import java.sql.Timestamp
import java.util.*
import kotlin.collections.HashMap

class Feeds(
    // var feedImgUrl: String? = null, //피드 사진
    var uid: String? = null,
    var strName : String? = null,//사용자 이름
    var imageUrl : String?= null, //프로필사진
    var title : Int?= 0,  //칭호
    var content : String? = null, //글 내용
    var timeStamp : Long?= null, //타임스탬프
    var likeCnt: Int = 0, //좋아요 수
    var commentCnt: Int =0, //댓글 수
    var hashtag: String? = null, //해시태그
    var privacy: Boolean = false, //공개범위
    var likes:MutableMap<String, String> = mutableMapOf()    )//좋아요한 사람

{
        data class Comment(
            var profileImgUrl: String? = null, //프로필 이미지
            var strName : String?= null, //유저 닉네임
            var timestamp: Timestamp?= null,
            var uid: String? = null, // uid
            var comment: String? = null //comment
        )
}

