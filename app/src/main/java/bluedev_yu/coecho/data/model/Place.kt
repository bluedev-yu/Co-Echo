package bluedev_yu.coecho.data.model

class Place(
    var placeName: String? = null,
    var placeCategory: String? = null,
    var placePhone: String? = null,
    var placeAdress: String? = null,
    var placeURL:String? = null,
    var placeDistanceFromMyLocation: String? = null,
    var placeX:Double = 0.0,//longitude
    var placeY:Double = 0.0)//latitude