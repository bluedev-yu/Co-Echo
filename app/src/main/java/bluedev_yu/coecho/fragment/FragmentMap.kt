package bluedev_yu.coecho.fragment

import android.content.Context
import kotlinx.coroutines.*
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import bluedev_yu.coecho.R
import bluedev_yu.coecho.databinding.FragmentMapBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import android.widget.*
import androidx.core.view.GravityCompat
import bluedev_yu.coecho.*
import com.google.android.material.navigation.NavigationView


class FragmentMap : Fragment(){

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


        // 맵 서치뷰 리스너
        searchView = binding.MapSearchBar
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {

                val bundle = Bundle().apply{ putString("query", query)}
                val transaction = childFragmentManager.beginTransaction()
                transaction.replace(R.id.MapFragment, FragmentMapSearch().apply { arguments=bundle })
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


    private fun loadFrag(fragment: Fragment){
        val tra = childFragmentManager.beginTransaction()
        tra.replace(R.id.MapDrawerLayout, fragment)
        tra.disallowAddToBackStack()
        tra.commit()
    }
}