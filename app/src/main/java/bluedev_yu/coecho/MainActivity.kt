package bluedev_yu.coecho

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.kakao.sdk.common.util.Utility

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //--해시 키 발급--
        var keyHash = Utility.getKeyHash(this)
        Log.d("해시 키",keyHash)
//        val nextIntent = Intent(this, MapActivity::class.java)
//        startActivity(nextIntent)
    }
}
