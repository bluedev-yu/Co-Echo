package bluedev_yu.coecho.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.compose.animation.core.snap
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import bluedev_yu.coecho.R
import bluedev_yu.coecho.adapter.FeedAdapter
import bluedev_yu.coecho.adapter.SearchHashtagAdapter
import bluedev_yu.coecho.adapter.SearchPeopleAdapter
import bluedev_yu.coecho.data.model.Feeds
import bluedev_yu.coecho.data.model.userDTO
import com.google.firebase.firestore.FirebaseFirestore

class FragmentResultContent(query: String?) : Fragment() {

    lateinit var rv_result_content: RecyclerView
    lateinit var noSearchContent: TextView
    var query: String? = query
    var firestore: FirebaseFirestore? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_result_content, container, false)

        firestore = FirebaseFirestore.getInstance()

        val bundle = Bundle()
        bundle.putString("query", query)

        noSearchContent = view.findViewById(R.id.noSearchContent)

        val hashtagList = arrayListOf<Feeds>()
        val contentUidList = arrayListOf<String>()

        firestore?.collection("Feeds")
            ?.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                hashtagList.clear()
                contentUidList.clear()
                if (querySnapshot == null) {
                    return@addSnapshotListener
                }
                for (snapshot in querySnapshot!!.documents) {
                    val imsi = snapshot.toObject(Feeds::class.java)
                    if (imsi?.content!!.contains(query!!)) //검색내용 포함시
                    {
                        hashtagList.add(imsi)
                        contentUidList.add(snapshot.id)
                    }
                }

                if(hashtagList.size ==0) {
                    noSearchContent.visibility = View.VISIBLE
                }
                else{
                    noSearchContent.visibility = View.INVISIBLE
                    rv_result_content.adapter = FeedAdapter(hashtagList, contentUidList)
                    rv_result_content.adapter!!.notifyDataSetChanged()
                }

            }

        rv_result_content = view.findViewById(R.id.rv_result_content)
        rv_result_content.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        rv_result_content.setHasFixedSize(true)

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