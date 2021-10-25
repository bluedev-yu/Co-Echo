package bluedev_yu.coecho.fragment

// import android.app.Fragment
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.core.view.GravityCompat
import androidx.fragment.app.FragmentActivity
import androidx.viewpager.widget.ViewPager
import bluedev_yu.coecho.R
import bluedev_yu.coecho.adapter.FragmentAdapter
import bluedev_yu.coecho.databinding.FragmentMapBinding
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class FragmentMap : Fragment(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: FragmentMapBinding

    var auth : FirebaseAuth? = null
    var firestore : FirebaseFirestore?= null //String 등 자료형 데이터베이스
    var firestorage : FirebaseStorage?= null //사진, GIF 등의 파일 데이터베이스

    private var viewProfile  : View? = null

    lateinit var searchView: SearchView
    lateinit var spinnerAdapter: SpinnerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentMapBinding.inflate(inflater, container, false)
        val view = binding.root

        // 옵션 버튼 선택시 오른쪽으로 열린다
        binding.MapOptionButton.setOnClickListener{
            binding.MapDrawerLayout.openDrawer(GravityCompat.END)
        }

        // 네비게이션 드로워 아이템 클릭 속성 부여
        binding.MapNavigationView.setNavigationItemSelectedListener(this)

        // 맵 서치뷰 리스너
        searchView = binding.MapSearchBar
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                val transaction = childFragmentManager.beginTransaction()
                transaction.replace(R.id.MapView, fragmentMapSearch())
                transaction.disallowAddToBackStack()
                transaction.commit()
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        val transaction = childFragmentManager.beginTransaction()
        transaction.replace(R.id.MapView, FragmentShowMap())
        transaction.disallowAddToBackStack()
        transaction.commit()

        return view
    }

    // 네비게이션 드로워가 켜진 상태에서 뒤로가기 버튼을 눌렀을때 네비게이션 드로워 종료
//    fun onBackPressed() {
//        if(binding.MapDrawerLayout.isDrawerOpen(GravityCompat.END))
//        {
//            binding.MapDrawerLayout.closeDrawers()
//        }
//    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId)
        {
            R.id.map1 -> Toast.makeText(context, "map1", Toast.LENGTH_SHORT).show()
        }
        binding.MapDrawerLayout.closeDrawers()
        return false
    }
}