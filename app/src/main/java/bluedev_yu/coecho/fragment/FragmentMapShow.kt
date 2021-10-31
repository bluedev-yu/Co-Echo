package bluedev_yu.coecho.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import bluedev_yu.coecho.KAKAO_Place
import bluedev_yu.coecho.R
import bluedev_yu.coecho.adapter.PlaceAdapter
import bluedev_yu.coecho.data.model.Place
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.*
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class FragmentMapShow : Fragment() {

    var auth: FirebaseAuth? = null
    var firestore: FirebaseFirestore? = null //String 등 자료형 데이터베이스
    var firestorage: FirebaseStorage? = null //사진, GIF 등의 파일 데이터베이스

    //디폴트 위치 서울시청
    var UserLati: Double = 37.541
    var UserLong: Double = 126.986

    lateinit var mapView: MapView
    lateinit var t_rv_places: RecyclerView

    //네비게이션뷰에 들어가는 데이터
    val placeList = arrayListOf<Place>()

    //마커 표시하는 데이터 배열
    val markerList = arrayListOf<MapPOIItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val thisFragView = inflater.inflate(R.layout.fragment_map_show, container, false)

        if (activity == null || requireActivity() == null) {
            Log.i("error", "프래그먼트에서 액티비티 null임")
        } else {

            Log.i("fragmentactivity", "not null")

            mapView = MapView(activity)
            val mapViewContainer = thisFragView.findViewById<MapView>(R.id.MapView) as ViewGroup
            mapViewContainer.addView(mapView)

            //firebase DB 접근
//            val dbinst= DB_Place()
//            dbinst.insert_data("레레레","12.34","56.78","대구 동구")
//            dbinst.read_data()

            if (mapView == null) {
                Log.i("mapView nullcheck", "맵뷰가 널이다")
            } else {
                Log.i("mapView nullcheck", "맵뷰 널 아님")
            }
            mapView.currentLocationTrackingMode =
                MapView.CurrentLocationTrackingMode.TrackingModeOff
            //API키 인증 성공 확인 코드(아래 로그가 뜨지 않을 경우 김예현에게 문의)
            mapView.setOpenAPIKeyAuthenticationResultListener { mapView, i, s ->
                Log.d("카카오맵 인증 로그", "성공")
            }

//            //스피너안의 목록
//            val spinnerItems = resources.getStringArray(R.array.spinner_array)
//            thisFragView.findViewById<Spinner>(R.id.MapSpinner).adapter =
//                ArrayAdapter(requireActivity(), R.layout.spinner_item, spinnerItems)

            //맵뷰에 현재 위치 리스너 적용
            mapView.setCurrentLocationEventListener(mCurrentLocationEventListener)

            //트래킹 버튼을 누를 경우 현재 위치 변환
            thisFragView.findViewById<ImageButton>(R.id.MapTrakingButton)
                .setOnClickListener(View.OnClickListener {
                    if (mapView.currentLocationTrackingMode == MapView.CurrentLocationTrackingMode.TrackingModeOff) {
                        //현재 위치를 중심으로 맵 보여주기
                        Toast.makeText(activity, "장소 추천 중! 잠시만 기다려주세요", Toast.LENGTH_LONG).show()
                        mapView.currentLocationTrackingMode =
                            MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading
                        loadRecommendationP(mapView, t_rv_places)
                    } else {
                        mapView.currentLocationTrackingMode =
                            MapView.CurrentLocationTrackingMode.TrackingModeOff
                    }
                })
            t_rv_places = thisFragView.findViewById<RecyclerView>(R.id.MapRecyclerView)
            t_rv_places.layoutManager =
                LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            t_rv_places.setHasFixedSize(true)

        }

        return thisFragView
    }

    companion object {

        const val BASE_URL = "https://dapi.kakao.com/"
        const val API_KEY = "KakaoAK 0d22f63c01f4013bda7cba8927cd0e33"  // REST API 키
    }

    //주변 검색 데이터 타입
    private suspend fun searchKeyword(mapView: MapView, keyword: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl(FragmentMapShow.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val interFace = retrofit.create(KAKAO_Place.KAKAOSearch::class.java)
        val call = interFace.getSearchKeyword(
            FragmentMapShow.API_KEY,
            keyword,
            UserLong.toString(),
            UserLati.toString(),
            3000
        )
        GlobalScope.launch {
            call.enqueue(object : Callback<KAKAO_Place.ResultSearchKeyword> {
                override fun onResponse(
                    call: retrofit2.Call<KAKAO_Place.ResultSearchKeyword>,
                    response: Response<KAKAO_Place.ResultSearchKeyword>
                ) {
                    Log.d("카카오 검색 성공", "Raw: ${response.raw()}")
                    Log.d("카카오 검색 성공", "Body: ${response.body()}")
                    if (response.body() != null) {
                        var len = Math.min(10, response.body()!!.meta.pageable_count)
                        placeList.clear()
                        markerList.clear()
                        for (i in 0 until len) {
                            val tmp = response.body()!!.documents[i]
                            //Toast.makeText(this@MapActivity,tmp.place_name+" "+tmp.address_name,Toast.LENGTH_SHORT).show()
                            placeList.add(
                                Place(
                                    null,
                                    tmp.place_name,
                                    tmp.category_name,
                                    tmp.phone,
                                    0,
                                    tmp.address_name,
                                    tmp.place_url,
                                    tmp.distance,
                                    tmp.x.toDouble(),
                                    tmp.y.toDouble()
                                )
                            )
                            markerList.add(
                                newCustomMapPoiItem(
                                    tmp.place_name,
                                    tmp.y.toDouble(),
                                    tmp.x.toDouble(),
                                    i
                                )
                            )
                        }
                    }

                }

                override fun onFailure(
                    call: retrofit2.Call<KAKAO_Place.ResultSearchKeyword>,
                    t: Throwable
                ) {
                    Log.d("카카오 검색 실패", "통신 실패: ${t.message}")
                }
            })
        }.join()
    }

    //근처 장소 추천 코드
    private fun loadRecommendationP(mapView: MapView, t_rv_places: RecyclerView) {
        if (false)//db 안을 검색해서 이 근처에 리뷰장소가 있는지 확인해야함
        {
            Log.i("loadP", "a 진입")
        } else {
            Log.i("loadP", "b 진입")
            runBlocking {

                CoroutineScope(Dispatchers.Default).launch {
                    searchKeyword(mapView, "환경")
                }.join()
            }
        }
        Log.i("loadP", placeList.size.toString())
        mapView.removeAllPOIItems()
        for (i in 0 until markerList.size) {
            mapView.addPOIItem(markerList.get(i))
        }
        //mapView.fitMapViewAreaToShowAllPOIItems()
        t_rv_places.getRecycledViewPool().clear()
        t_rv_places.adapter = PlaceAdapter(placeList)
        PlaceAdapter(placeList).notifyDataSetChanged()
    }

    //현재 위치로 맵 조정했을 때 사용자 위경도 저장(+이후 추천순 검색 기능 추가)
    val mCurrentLocationEventListener = object : MapView.CurrentLocationEventListener {
        override fun onCurrentLocationUpdate(p0: MapView?, p1: MapPoint?, p2: Float) {
            Log.i("사용자 경위도", "로케이션 업데이트됨")
            if (p1 != null) {
                var tempLong: Double = p1.mapPointGeoCoord.longitude
                var tempLati: Double = p1.mapPointGeoCoord.latitude
                //if (tempLong!=UserLong||tempLati!=UserLati) {
                if (Math.abs(tempLong - UserLong) + Math.abs(tempLati - UserLati) > 0.0005) {
                    UserLong = p1.mapPointGeoCoord.longitude
                    UserLati = p1.mapPointGeoCoord.latitude
                    Log.i("사용자 경위도", UserLati.toString() + " " + UserLong.toString())
                    loadRecommendationP(mapView, t_rv_places)
                }
            }
        }

        override fun onCurrentLocationDeviceHeadingUpdate(p0: MapView?, p1: Float) {
            TODO("Not yet implemented")
        }

        override fun onCurrentLocationUpdateFailed(p0: MapView?) {
            TODO("Not yet implemented")
        }

        override fun onCurrentLocationUpdateCancelled(p0: MapView?) {
            TODO("Not yet implemented")
        }
    }

    val mapltner = object : MapView.MapViewEventListener {
        override fun onMapViewInitialized(mapView: MapView) {
            Log.i("카카오맵 로그", "successfully initilized")
        }

        //아래는 모두 아직 사용하지 않는 이벤트리스너들
        override fun onMapViewCenterPointMoved(mapView: MapView, mapPoint: MapPoint) {
            Log.i("디테일로그", "onMapViewCenterPointMoved")
        }

        override fun onMapViewZoomLevelChanged(mapView: MapView, i: Int) {
            Log.i("디테일로그", "onMapViewZoomLevelChanged")
        }

        override fun onMapViewSingleTapped(mapView: MapView, mapPoint: MapPoint) {
            Log.i("디테일로그", "onMapViewSingleTapped")
        }

        override fun onMapViewDoubleTapped(mapView: MapView, mapPoint: MapPoint) {
            Log.i("디테일로그", "장소 선택")
        }

        override fun onMapViewLongPressed(mapView: MapView, mapPoint: MapPoint) {
            Log.i("디테일로그", "onMapViewLongPressed")
        }

        override fun onMapViewDragStarted(mapView: MapView, mapPoint: MapPoint) {
            Log.i("디테일로그", "onMapViewDragStarted")
        }

        override fun onMapViewDragEnded(mapView: MapView, mapPoint: MapPoint) {
            Log.i("디테일로그", "onMapViewDragEnded")
        }

        override fun onMapViewMoveFinished(mapView: MapView, mapPoint: MapPoint) {
            Log.i("디테일로그", "onMapViewMoveFinished")
        }
    }

    class MarkerEventListener(val context: Context) : MapView.POIItemEventListener {
        override fun onPOIItemSelected(p0: MapView?, p1: MapPOIItem?) {
            //마커 클릭 이벤트
        }

        override fun onCalloutBalloonOfPOIItemTouched(p0: MapView?, p1: MapPOIItem?) {
//            //말품선 클릭 1
//            val intent = Intent(context, Place_detail::class.java)
//            context.startActivity(intent)
        }

        override fun onCalloutBalloonOfPOIItemTouched(
            p0: MapView?,
            p1: MapPOIItem?,
            p2: MapPOIItem.CalloutBalloonButtonType?
            //말풍선 클릭 2
        ) {
            //혹시나 place_detail을 fragment로 변경할 경우
//            supportFragmentManager.beginTransaction()
//                .add(R.id.framelayout, Place_detail())
//                .commit()

//            val intent = Intent(context, Place_detail::class.java)
//            context.startActivity(intent)

        }

        override fun onDraggablePOIItemMoved(p0: MapView?, p1: MapPOIItem?, p2: MapPoint?) {
            //마커 속성이 idDraggable = true일 때 마커 이동시
        }
    }

    //마커 추가 함수
    fun newMapPoiItem(iName: String, lat: String, lon: String): MapPOIItem//지도에 마커 추가 함수
    {
        val marker = MapPOIItem()
        marker.apply {
            itemName = iName
            mapPoint = MapPoint.mapPointWithGeoCoord(lat.toDouble(), lon.toDouble())
            markerType = MapPOIItem.MarkerType.BluePin
            selectedMarkerType = MapPOIItem.MarkerType.BluePin
            isCustomImageAutoscale = true
            setCustomImageAnchor(0.5f, 1.0f)
        }
        return marker
    }

    //커스텀 마커
    fun newCustomMapPoiItem(iName: String, lat: Double, lon: Double, tagN: Int): MapPOIItem {
        val marker = MapPOIItem();
        marker.apply {
            itemName = iName
            tag = tagN
            mapPoint = MapPoint.mapPointWithGeoCoord(lat, lon)
            markerType = MapPOIItem.MarkerType.CustomImage // 마커타입을 커스텀 마커로 지정.
            customImageResourceId = R.drawable.echo_custom_marker //마커 이미지 설정 -> 나뭇잎 모양
            isCustomImageAutoscale = false
            setCustomImageAnchor(0.5f, 1.0f)
        }
        return marker
    }
}