package bluedev_yu.coecho

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

class KAKAO_Place {

    //주변 검색 데이터 타입
    data class ResultSearchKeyword(
        var meta: ResMeta,                // 장소 메타데이터
        var documents: List<placeData>          // 검색 결과
    )

    data class ResMeta
        (
        var total_count: Int,
        var pageable_count: Int,
        var is_end: Boolean
    )

    data class placeData
        (
        var id: String,                     // 장소 ID
        var place_name: String,             // 장소명, 업체명
        var category_name: String,          // 카테고리 이름
        var category_group_code: String,    // 중요 카테고리만 그룹핑한 카테고리 그룹 코드
        var category_group_name: String,    // 중요 카테고리만 그룹핑한 카테고리 그룹명
        var phone: String,                  // 전화번호
        var address_name: String,           // 전체 지번 주소
        var road_address_name: String,      // 전체 도로명 주소
        var x: String,                      // X 좌표값 혹은 longitude
        var y: String,                   // Y 좌표값 혹은 latitude
        var place_url: String,              // 장소 상세페이지 URL
        var distance: String                 // 중심좌표까지의 거리. 단, x,y 파라미터를 준 경우에만 존재. 단위는 meter
    )

    interface KAKAOSearch {
        @GET("/v2/local/search/keyword.json")
        fun getSearchKeyword(@Header("Authorization") key: String,
                             @Query("query") query: String,
                             @Query("x") x:String,
                             @Query("y")y:String,
                             @Query("radius")radius:Int):retrofit2.Call<ResultSearchKeyword>
    }
}