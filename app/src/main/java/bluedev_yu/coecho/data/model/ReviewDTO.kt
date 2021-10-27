package bluedev_yu.coecho.data.model

data class ReviewDTO(
    var star : Float? = null,
    var pid : String? = null,
    var uid : String? = null,
    var timestamp: Long? = null,
    var content : String? = null,
    var hashtag: String? = null,
    var strName : String? = null,//사용자 이름
    var title : Int?= 0 //칭호
)