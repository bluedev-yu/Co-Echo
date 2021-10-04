package bluedev_yu.coecho.data.model

data class FollowDTO(

        var followerCount: Int = 0,
        var followers: List<String> = listOf(),

        var followingCount: Int = 0,
        var followings: List<String> = listOf(),
)