package bluedev_yu.coecho.data.model

data class FollowDTO(

        var followerCount: Int = 0,
        var followers: ArrayList<String> = arrayListOf(),

        var followingCount: Int = 0,
        var followings: ArrayList<String> = arrayListOf()
)