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
import bluedev_yu.coecho.data.model.Feeds
import bluedev_yu.coecho.data.model.Place
import bluedev_yu.coecho.data.model.userDTO
import bluedev_yu.coecho.databinding.FragmentMapPlacesBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

private lateinit var binding : FragmentMapPlacesBinding

class fragmentSubscriber(uid: String?) : Fragment() {
    private var rv_place: RecyclerView? = null
    var uid: String? = uid
    var firestore : FirebaseFirestore? = null
    var auth : FirebaseAuth?= null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMapPlacesBinding.inflate(layoutInflater)
        val view = binding.root

        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        if(uid == null) //마이페이지
        {
            uid = auth?.uid!!.toString()
        }

        val bundle = Bundle()
        bundle.putString("uid", uid)

        // Inflate the layout for this fragment
        val placeList = arrayListOf<Place>()
        var placelike: MutableMap<String, String>
        val nofeed = view.findViewById<TextView>(R.id.nofeed_myplace)

        //유저에 있는 placeLike
        firestore?.collection("User")?.document(auth?.uid.toString())?.get()?.addOnSuccessListener {
            doc ->
            if(doc.exists())
            {
                var userDTO = doc.toObject(userDTO::class.java)
                placelike = userDTO!!.placeLike

                firestore?.collection("Places")?.get()?.addOnSuccessListener{
                        task ->
                    placeList.clear()
                    if (task == null) {
                        return@addOnSuccessListener
                    }
                    else
                    {
                        for (snapshot in task!!.documents) {
                            if(placelike.containsKey(snapshot.id)) //지금 doc이 placelike에 포함되어 있는 경우
                            {
                                var place = snapshot.data

                                var imsi = Place()
                                if (place != null) {
                                    imsi!!.placeName = place.get("placeName").toString()
                                    imsi!!.placeAdress = place.get("address").toString()
                                    Log.v("placeName",place.get("placeName").toString())
                                    Log.v("placeAdress",place.get("address").toString())

                                }
                                placeList.add(imsi!!)
                            }
                        }
                    }

                    Log.v("placeList",placeList.size.toString())
                    Log.v("placeList",placeList.toString())

                    if(placeList.size ==0)
                    {
                        nofeed.visibility = View.VISIBLE
                    }
                    else {
                        rv_place = binding.MapPlacesRecyclerView
                        rv_place!!.adapter = MapPlacesAdapter(placeList)
                        rv_place!!.adapter!!.notifyDataSetChanged()
                        rv_place!!.layoutManager =
                            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                        rv_place!!.setHasFixedSize(true)
                    }

                }
            }
        }



        return view
    }

}