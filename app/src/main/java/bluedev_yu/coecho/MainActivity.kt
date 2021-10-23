package bluedev_yu.coecho

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.WindowManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import bluedev_yu.coecho.fragment.FragmentMap
import bluedev_yu.coecho.fragment.FragmentMyPage
import bluedev_yu.coecho.fragment.FragmentSNS
import bluedev_yu.coecho.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationBarView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.kakao.util.maps.helper.Utility
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView


class MainActivity : AppCompatActivity(), NavigationBarView.OnItemSelectedListener{

    var firestore : FirebaseFirestore?= null //String 등 자료형 데이터베이스
    var firestorage : FirebaseStorage?= null //사진, GIF 등의 파일 데이터베이스
    val placeList = arrayListOf<Place>()

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.bottomNavBar.setOnItemSelectedListener(this)

        setDefaultFragment()

        binding.bottomNavBar.itemIconTintList = null;

        //권한 요청 코드
        if (PackageManager.PERMISSION_DENIED == ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        ) {
            Log.d("권한 허용 여부", "***denied***")
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1004)
        }
        else if (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        ) {
            Log.d("권한 허용 여부", "***granted***")
        }
    }

    fun printHash()
    {
        //--해시 키 발급--
        var keyHash = Utility.getKeyHash(this)
        Log.d("해시 키",keyHash)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean { //내비게이션바 아이템 선택시 프래그먼트 교체
        when (item.itemId) {
            R.id.action_sns -> {
                loadFragment(FragmentSNS())
                return true
            }
            R.id.action_map -> {
                Log.d("Fragment 상태",FragmentMap().isAdded.toString()+" "+FragmentMap().isVisible.toString())
                if(!FragmentMap().isVisible) {
                    loadFragment(FragmentMap())
                }
                return true
            }
            R.id.action_myPage -> {
                loadFragment(FragmentMyPage())
                return true
            }
        }
        return false
    }

    private fun setDefaultFragment(){ //앱 실행시 디폴트 프래그먼트 설정
        loadFragment(FragmentSNS())
    }

    private fun loadFragment(fragment: Fragment) { //프래그먼트 로드
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frameLayout, fragment)
        transaction.disallowAddToBackStack()
        transaction.commit()
    }





}
