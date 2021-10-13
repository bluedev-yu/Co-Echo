package bluedev_yu.coecho.fragment

import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.core.view.GravityCompat
import bluedev_yu.coecho.R
import bluedev_yu.coecho.databinding.FragmentMapBinding
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage


class FragmentMap : Fragment(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: FragmentMapBinding

    var auth : FirebaseAuth? = null
    var firestore : FirebaseFirestore?= null //String 등 자료형 데이터베이스
    var firestorage : FirebaseStorage?= null //사진, GIF 등의 파일 데이터베이스


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentMapBinding.inflate(inflater, container, false)

        // 옵션 버튼 선택시 오른쪽으로 열린다
        binding.MapOptionButton.setOnClickListener{
            binding.MapDrawerLayout.openDrawer(GravityCompat.END)
        }

        // 네비게이션 드로워 아이템 클릭 속성 부여
        binding.mapNavigationView.setNavigationItemSelectedListener(this)

        val view = binding.root
        return view
    }



    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId)
        {
            R.id.map1 -> Toast.makeText(context, "map1", Toast.LENGTH_SHORT).show()
        }
        binding.MapDrawerLayout.closeDrawers()
        return false
    }
}