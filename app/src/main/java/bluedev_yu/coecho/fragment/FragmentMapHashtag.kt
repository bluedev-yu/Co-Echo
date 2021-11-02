package bluedev_yu.coecho.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import bluedev_yu.coecho.R
import bluedev_yu.coecho.adapter.MapPlacesAdapter
import bluedev_yu.coecho.adapter.SearchHashtagAdapter
import bluedev_yu.coecho.data.model.Feeds
import bluedev_yu.coecho.data.model.Place
import bluedev_yu.coecho.data.model.userDTO
import bluedev_yu.coecho.databinding.FragmentMapHashtagBinding
import bluedev_yu.coecho.databinding.FragmentMapSearchBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_map_hashtag.*


class FragmentMapHashtag(query:String?) : Fragment() {

//    lateinit var recyclerView: RecyclerView
    private lateinit var binding: FragmentMapHashtagBinding
    private var rv_place: RecyclerView? = null
    var firestore : FirebaseFirestore? = null
    var auth : FirebaseAuth?= null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentMapHashtagBinding.inflate(inflater, container, false)
        val view = binding.root

        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        val query=arguments?.getString("query")

        // Inflate the layout for this fragment
        val placeList = arrayListOf<Place>()
        var placelike: MutableMap<String, String>
        val nofeed = view.findViewById<TextView>(R.id.nofeed_myplace)


        //1) 해시태그에서 검색

//                        rv_place = binding.MapHashtagRecylerView
//                        rv_place!!.adapter = MapPlacesAdapter(placeList)
//                        rv_place!!.adapter!!.notifyDataSetChanged()
//                        rv_place!!.layoutManager =
//                            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
//                        rv_place!!.setHasFixedSize(true)
        return view
    }

    private fun newInstant(query: String?): FragmentMapHashtag {
        val bundle = Bundle()
        val frag = FragmentMapHashtag(query)
        bundle.putString("query", query)
        frag.arguments = bundle
        return frag
    }


}