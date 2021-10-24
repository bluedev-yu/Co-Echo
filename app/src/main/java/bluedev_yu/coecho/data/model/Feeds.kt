package bluedev_yu.coecho.data.model

class Feeds(
//            var feedId: String? = null,
//            var feedImgUrl: String? = null,
            var uid: String? = null,
            var content : String? = null,
            var hashtag: String? = null,
            var privacy: Boolean = false,
            var likeCnt: Int = 0,
            var commentCnt: Int =0,
            var likes:MutableMap<String, Boolean> = HashMap())