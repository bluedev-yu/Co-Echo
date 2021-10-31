package bluedev_yu.coecho.data.model

import java.net.URL

data class userDTO(var strName : String? = null,//사용자 이름
                    var uid : String? = null,//uid
                   var imageUrl : String?= null, //프로필사진
                   var title : Int?= 0 ,//칭호
                    var mypageBackground : String?= null
) //마이페이지 뒷배경