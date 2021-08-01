package bluedev_yu.coecho

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import bluedev_yu.coecho.Fragment.FragmentMap
import bluedev_yu.coecho.Fragment.FragmentMyPage
import bluedev_yu.coecho.Fragment.FragmentSNS
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView

class MainActivity : AppCompatActivity(), NavigationBarView.OnItemSelectedListener{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavBar = findViewById<BottomNavigationView>(R.id.bottomNavBar)
        bottomNavBar.setOnItemSelectedListener(this)

        setDefaultFragment()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
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

    private fun setDefaultFragment(){
        loadFragment(FragmentSNS())
    }

    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frameLayout, fragment)
        transaction.disallowAddToBackStack()
        transaction.commit()
    }
}