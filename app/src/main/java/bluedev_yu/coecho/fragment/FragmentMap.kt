package bluedev_yu.coecho.fragment

import kotlinx.coroutines.*
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import bluedev_yu.coecho.R
import bluedev_yu.coecho.databinding.FragmentMapBinding
import android.widget.*
import androidx.core.view.GravityCompat
import bluedev_yu.coecho.*
import com.google.android.material.navigation.NavigationView


class FragmentMap : Fragment(), NavigationView.OnNavigationItemSelectedListener, onBack{

    private lateinit var binding: FragmentMapBinding

    lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentMapBinding.inflate(inflater, container, false)
        val view = binding.root

        // 햄버거 메뉴 선택시 오른쪽으로 열린다
        binding.MapOptionButton.setOnClickListener {
            binding.MapDrawerLayout
                .openDrawer(GravityCompat.END)
        }
        // 네비게이션 드로워 아이템 클릭 속성 부여
        binding.MapNavigationView
            .setNavigationItemSelectedListener(this)

        // 맵 서치뷰 리스너
        searchView = binding.MapSearchBar
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                val transaction = childFragmentManager.beginTransaction()
                transaction.replace(R.id.MapFragment, FragmentMapSearch())
                transaction.disallowAddToBackStack()
                transaction.commit()
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        val transaction = childFragmentManager.beginTransaction()
        transaction.replace(R.id.MapFragment, FragmentMapShow())
        transaction.disallowAddToBackStack()
        transaction.commit()

        return view
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId)
        {
            R.id.marked_place -> loadFrag(FragmentMapFavorite())
//            R.id.marked_place -> Toast.makeText(context, "즐겨찾기", Toast.LENGTH_SHORT).show()
        }
        binding.MapDrawerLayout.closeDrawers()
        return false
    }

    override fun onBackPressed() {
        if (binding.MapDrawerLayout.isDrawerOpen(GravityCompat.END)){
            binding.MapDrawerLayout.closeDrawers()
        }
        else{
            activity?.finish()
        }
    }

    private fun loadFrag(fragment: Fragment){
        val tra = childFragmentManager.beginTransaction()
        tra.replace(R.id.MapDrawerLayout, fragment)
        tra.disallowAddToBackStack()
        tra.commit()

    }
}