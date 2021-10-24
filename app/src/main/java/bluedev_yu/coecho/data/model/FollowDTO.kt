package bluedev_yu.coecho.data.model

data class FollowDTO(

        var followerCount: Int = 1, //나를 팔로우하는 사람
        var followers: MutableMap<String,String> = mutableMapOf(),

        var followingCount: Int = 1, //내가 팔로우하는 사람
        var followings: MutableMap<String,String> = mutableMapOf(),
)