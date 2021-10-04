package bluedev_yu.coecho.data.model

data class FollowDTO(

        var followerCount: Int = 0,
        var followers: Array<String>,

        var followingCount: Int = 0,
        var followings: Array<String>,
)