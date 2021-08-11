package bluedev_yu.coecho

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.mapmain.*
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView


class MapActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    val eventListener = MarkerEventListener(this)

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.mapmain)

        // 햄버거 메뉴 선택시 오른쪽으로 열린다
        hambuger_menu.setOnClickListener{
            layout_drawer.openDrawer(GravityCompat.END)
        }
        // 네비게이션 드로워 아이템 클릭 속성 부여
        hambuger_navigation_view.setNavigationItemSelectedListener(this)

        imageView13.setOnClickListener {
            val intent = Intent(this, Place_detail::class.java)
            startActivity(intent)
        }

        val mapView = MapView(this)
        val mapViewContainer = findViewById<View>(R.id.map_view) as ViewGroup
        mapViewContainer.addView(mapView)

        //마커 추가 코드
        mapView.addPOIItem(newCustomMapPoiItem("제로스테이", 35.81943491247418, 128.52228528837313))
        mapView.addPOIItem(newCustomMapPoiItem("더 커먼", 35.86863293890943, 128.61172204038763))
        mapView.addPOIItem(newCustomMapPoiItem("오브젝트 서교점",37.55567794970775, 126.92979269499948))
        mapView.addPOIItem(newCustomMapPoiItem("제제상회",35.86085217817711, 128.59818896508298))


        mapView.addPOIItem(newMapPoiItem("야미스토어",35.88787328754492, 128.70543100254224))
        mapView.addPOIItem(newMapPoiItem("꼬두람이",35.90233355828279, 128.61052507620627))
        mapView.addPOIItem(newMapPoiItem("그로잉",35.848023694028505, 128.5968855019969))
        mapView.addPOIItem(newMapPoiItem("버거데이",35.84427201048634, 128.7033817611035))
        mapView.addPOIItem(newMapPoiItem("책빵고스란히",35.866978451216234, 128.61279562909166))
        mapView.addPOIItem(newMapPoiItem("땅과 사람이야기",35.86274508609894, 128.69574558645667))
        mapView.addPOIItem(newMapPoiItem("아라리오",35.83619833541316, 128.58327150846532))
        mapView.addPOIItem(newMapPoiItem("지읒상점",35.86392057725657, 128.60797885108323))
        mapView.addPOIItem(newMapPoiItem("진책방",35.85319892703311, 128.61656219584384))
        mapView.addPOIItem(newMapPoiItem("하고카페",35.83686062629213, 128.58207838413546))



        //맵 초기 위치 설정(마커 있는 진천동 부근으로 임의설정)
        mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(35.81, 128.52), true);

        //권한 요청 코드(denied가 뜰 경우 앱을 다시 실행시켜 권한 요청 다시 받아서 실행)
        if (PackageManager.PERMISSION_DENIED == ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        ) {
            Log.d("권한 허용 여부", "***denied***")
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                1004
            )
            if (PackageManager.PERMISSION_DENIED == ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            )
                Log.d("권한 허용 여부", "***still denied***")
        } else if (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        ) Log.d("권한 허용 여부", "***granted***")


        //API키 인증 성공 확인 코드(아래 로그가 뜨지 않을 경우 김예현에게 문의)
        mapView.setOpenAPIKeyAuthenticationResultListener { mapView, i, s ->
            Log.d("카카오맵 인증 로그", "성공")
        }

        //맵뷰에 이벤트 리스너 적용
        mapView.setMapViewEventListener(mapltner)
        mapView.setPOIItemEventListener(eventListener)

    }

    // 네비게이션 드로워 아이템 선택 시 수행
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId)
        {
            R.id.item1 -> Toast.makeText(applicationContext,"item1",Toast.LENGTH_SHORT).show()
            R.id.item2 -> Toast.makeText(applicationContext,"item2",Toast.LENGTH_SHORT).show()
            R.id.item3 -> Toast.makeText(applicationContext,"item3",Toast.LENGTH_SHORT).show()
        }
        layout_drawer.closeDrawers() // 네비게이션 드로워 닫기
        return false
    }

    // 뒤로가기 버튼 눌렷을때 네비게이션 드로워 종료
    override fun onBackPressed() {
        if(layout_drawer.isDrawerOpen(GravityCompat.END))
        {
            layout_drawer.closeDrawers()
        }
        else
        {
            super.onBackPressed() // 일반 백버튼 기능실행
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


    fun newMapPoiItem(iName: String, lat: Double, lon: Double): MapPOIItem//지도에 마커 추가 함수
    {
        val marker = MapPOIItem()
        marker.apply {
            itemName = iName
            mapPoint = MapPoint.mapPointWithGeoCoord(lat, lon)
            markerType = MapPOIItem.MarkerType.BluePin
            selectedMarkerType = MapPOIItem.MarkerType.BluePin
            isCustomImageAutoscale = true
            setCustomImageAnchor(0.5f, 1.0f)
        }
        return marker
    }


    fun newCustomMapPoiItem(iName: String, lat: Double, lon: Double): MapPOIItem {
        val marker = MapPOIItem();
        marker.apply {
            itemName = iName
            tag = 1
            mapPoint = MapPoint.mapPointWithGeoCoord(lat, lon)
            markerType = MapPOIItem.MarkerType.CustomImage // 마커타입을 커스텀 마커로 지정.
            customImageResourceId = R.drawable.pin //마커 이미지 설정 -> 나뭇잎 모양
            isCustomImageAutoscale = true
            setCustomImageAnchor(0.5f, 1.0f)
        }
        return marker
    }

    class MarkerEventListener(val context: Context) : MapView.POIItemEventListener{
        override fun onPOIItemSelected(p0: MapView?, p1: MapPOIItem?) {
            //마커 클릭 이벤트
        }

        override fun onCalloutBalloonOfPOIItemTouched(p0: MapView?, p1: MapPOIItem?) {
            //말품선 클릭 1
            val intent = Intent(context, Place_detail::class.java)
            context.startActivity(intent)
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
}

