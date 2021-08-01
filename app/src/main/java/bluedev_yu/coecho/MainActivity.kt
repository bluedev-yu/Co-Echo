package bluedev_yu.coecho

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.view.ViewGroup

import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import net.daum.mf.map.api.MapView


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mapmain)
        val mapView = MapView(this)
        //mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(37.53737528, 127.00557633), true);
        val mapViewContainer = findViewById<View>(R.id.map_view) as ViewGroup
        mapViewContainer.addView(mapView)
        if(PackageManager.PERMISSION_DENIED==ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION))
        {
            System.out.println("***denied***")
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                1004)//requestcode 1004로 해도 되는지 확인 필요
            if(PackageManager.PERMISSION_DENIED==ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION))
                System.out.println("***still denied***")
        }
        else if(PackageManager.PERMISSION_GRANTED==ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION))
            System.out.println("***granted***")
    }


}