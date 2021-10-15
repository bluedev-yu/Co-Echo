package bluedev_yu.coecho.data.model

data class FollowDTO(

        var followerCount: Int = 0,
        var followers: ArrayList<String>,

        var followingCount: Int = 0,
        var followings: ArrayList<String>
)