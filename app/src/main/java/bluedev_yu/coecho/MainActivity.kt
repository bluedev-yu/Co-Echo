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
import androidx.fragment.app.Fragment
import bluedev_yu.coecho.Fragment.FragmentMap
import bluedev_yu.coecho.data.model.FollowDTO
import bluedev_yu.coecho.data.model.userDTO
import bluedev_yu.coecho.Fragment.FragmentMyPage
import bluedev_yu.coecho.Fragment.FragmentSNS
import bluedev_yu.coecho.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationBarView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.kakao.util.maps.helper.Utility


class MainActivity : AppCompatActivity(), NavigationBarView.OnItemSelectedListener{

    var auth : FirebaseAuth? = null
    var firestore : FirebaseFirestore?= null //String 등 자료형 데이터베이스
    var firestorage : FirebaseStorage?= null //사진, GIF 등의 파일 데이터베이스

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.statusBarColor = ContextCompat.getColor(this, R.color.green)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.bottomNavBar.setOnItemSelectedListener(this)

        setDefaultFragment()

        binding.bottomNavBar.itemIconTintList = null;



        Toast.makeText(this,auth?.uid.toString(), Toast.LENGTH_LONG).show()
        Log.v("User!",auth?.uid.toString())
        //firebase 로그인, 권한
        firestore?.collection("User")?.document(auth?.uid.toString())?.get()?.addOnSuccessListener {
                doc ->
            if(doc.exists()){
                Log.v("Login1","not exist")
            }
            else
            {
                Log.v("Login2","not exist")
                var userInfo = userDTO()
                userInfo.uid = auth?.currentUser?.uid
                userInfo.strName = auth?.currentUser?.displayName
                firestore?.collection("User")?.document(auth?.uid.toString())?.set(userInfo)
            }
        }?.addOnFailureListener{
            Log.e("What!!!!!!", "Error getting data", it)
        }

        //follow
        firestore?.collection("Follow")?.document(auth?.uid.toString())?.get()?.addOnSuccessListener {
                doc ->
            Log.v("followisexist","true")
            if(doc.exists()){

            }
            else
            {
                var followDTO = FollowDTO()
                followDTO.followers.put(auth?.uid.toString(),auth?.uid.toString())
                followDTO.followings.put(auth?.uid.toString(),auth?.uid.toString())
                firestore?.collection("Follow")?.document(auth?.uid.toString())?.set(followDTO)
            }
        }

        //앨범 권한
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),1)

        printHash()
        //val optionbutton : ImageView = findViewById(R.id.MyPageOptionButton)
        //val drawerLayout : DrawerLayout = findViewById(R.id.MyPageDrawerLayout)
        // 햄버거 메뉴 선택시 오른쪽으로 열린다
        //optionbutton.setOnClickListener{
        //    drawerLayout.openDrawer(GravityCompat.END)
        //}

        //권한 요청 코드
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
        } else if (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        ) {
            Log.d("권한 허용 여부", "***granted***")
        }
    }

    fun printHash() {
        //--해시 키 발급--
        var keyHash = Utility.getKeyHash(this)
        Log.d("해시 키", keyHash)

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean { //내비게이션바 아이템 선택시 프래그먼트 교체
        when (item.itemId) {
            R.id.action_sns -> {
                loadFragment(FragmentSNS())
                return true
            }
            R.id.action_map -> {
                loadFragment(FragmentMap())
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
