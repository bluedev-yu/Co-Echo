package bluedev_yu.coecho

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import net.daum.mf.map.api.MapView
import com.kakao.sdk.common.util.Utility

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //--해시 키 발급--
//        var keyHash = Utility.getKeyHash(this)
//        Log.d("해시 키",keyHash)
//        val nextIntent = Intent(this, MapActivity::class.java)
//        startActivity(nextIntent)
    }
}
