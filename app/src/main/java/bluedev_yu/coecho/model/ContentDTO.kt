package bluedev_yu.coecho.model

import java.sql.Timestamp

//Content.user.contentid
data class ContentDTO(var explain : String?= null,
                      var imageurl : String?= null,
                      var contentId : String?= null,
                      var uid : String?= null,
                      var userId : String?= null, //유저 아이디
                      var timestamp: Long?= null,
                      var favoriteCount: Int =0,
                      var favorites : MutableMap<String, Boolean> = HashMap(),
                      var hashTag : MutableMap<String, Boolean> = HashMap())

//Comment.contentid.Commentid
{  data class Comment(var uid : String ?= null,
                      var CommentId : String?= null,
                      var pidid: String?= null,
                      var userId: String?= null,
                      var comment: String?= null,
                      var timestamp: Long? = null,)}
