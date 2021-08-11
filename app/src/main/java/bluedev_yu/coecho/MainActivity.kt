package bluedev_yu.coecho

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.WindowManager
import androidx.fragment.app.Fragment
import bluedev_yu.coecho.fragment.FragmentMap
import bluedev_yu.coecho.fragment.FragmentMyPage
import bluedev_yu.coecho.fragment.FragmentSNS
import bluedev_yu.coecho.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationBarView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage


class MainActivity : AppCompatActivity(), NavigationBarView.OnItemSelectedListener{

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.bottomNavBar.setOnItemSelectedListener(this)

        setDefaultFragment()

        binding.bottomNavBar.itemIconTintList = null;
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
