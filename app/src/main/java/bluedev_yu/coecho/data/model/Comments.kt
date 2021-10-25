package bluedev_yu.coecho.data.model

import java.sql.Timestamp

class Comments(
    var profileImgUrl: String? = null, //프로필 이미지
    var strName : String?= null, //유저 닉네임
    var timestamp: Timestamp ?= null,
    var uid: String? = null, // uid
    var comment: String? = null //comment
)