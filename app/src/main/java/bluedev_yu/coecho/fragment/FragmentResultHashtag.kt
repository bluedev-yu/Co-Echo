package bluedev_yu.coecho.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import bluedev_yu.coecho.R
import bluedev_yu.coecho.adapter.SearchHashtagAdapter
import bluedev_yu.coecho.adapter.SearchPeopleAdapter
import bluedev_yu.coecho.data.model.Feeds
import bluedev_yu.coecho.data.model.userDTO
import com.google.firebase.firestore.FirebaseFirestore

class FragmentResultHashtag(query: String?) : Fragment() {

    lateinit var rv_result_hashtag: RecyclerView
    var query: String? = query
    var firestore : FirebaseFirestore? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_result_hashtag, container, false)

        firestore = FirebaseFirestore.getInstance()

        val bundle = Bundle()
        bundle.putString("query", query)

        val hashtagList = arrayListOf<Feeds>()

        firestore?.collection("Feeds")?.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
            hashtagList.clear()
            if (querySnapshot == null) {
                return@addSnapshotListener
            }
            for (snapshot in querySnapshot!!.documents) {
                val imsi = snapshot.toObject(Feeds::class.java)
                if (imsi?.hashtag!!.contains(query!!)) //검색내용 포함시
                    hashtagList.add(imsi)
            }
            rv_result_hashtag.adapter = SearchHashtagAdapter(hashtagList)
            rv_result_hashtag.adapter!!.notifyDataSetChanged()
        }

        rv_result_hashtag = view.findViewById(R.id.rv_result_hashtag)
        rv_result_hashtag.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        rv_result_hashtag.setHasFixedSize(true)

        return view
    }

//    private fun newInstant(query: String) : FragmentResultHashtag{
//        val bundle = Bundle()
//        val frag = FragmentResultHashtag(query)
//        bundle.getString("query", query)
//        frag.arguments = bundle
//
//        Toast.makeText(requireContext(), "검색결과 : $query", Toast.LENGTH_SHORT).show()
//        return frag
//    }
}