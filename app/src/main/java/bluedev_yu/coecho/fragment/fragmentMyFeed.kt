package bluedev_yu.coecho.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import bluedev_yu.coecho.R
import bluedev_yu.coecho.adapter.FeedAdapter
import bluedev_yu.coecho.data.model.Feeds
import bluedev_yu.coecho.databinding.FragmentMyFeedBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

private lateinit var binding: FragmentMyFeedBinding

class fragmentMyFeed(uid: String?) : Fragment() {
    private lateinit var rv_feeds: RecyclerView
    var uid: String? = uid
    var firestore : FirebaseFirestore? = null
    var auth : FirebaseAuth?= null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyFeedBinding.inflate(layoutInflater)
        val view = binding.root

        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        if(uid == null) //마이페이지
        {
            uid = auth?.uid!!.toString()
            Toast.makeText(this.context,"마이페이지!",Toast.LENGTH_SHORT).show()
        }

        val bundle = Bundle()
        bundle.putString("uid", uid)

        // Inflate the layout for this fragment
        val feedList = arrayListOf<Feeds>()
        val contentUidList = arrayListOf<String>()

        firestore?.collection("Feeds")?.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
            feedList.clear()
            contentUidList.clear()
            if (querySnapshot == null) {
                return@addSnapshotListener
            }
            for (snapshot in querySnapshot!!.documents) {
                val now = snapshot.toObject(Feeds::class.java)
                //내피드인 경우 -> 전부 보이기
                //남피드인 경우 -> 공개한것만 보이기
                if(uid.equals(auth?.uid.toString())) //내가 내 마이페이지
                {
                    if(now?.uid.toString().equals(uid)) //내피드
                    {
                        feedList.add(now!!)
                        contentUidList.add(snapshot.id)
                    }
                }
                else //남이 내페이지, 내가 남페이지
                {
                    if ((now?.uid!!.equals(uid) && now.privacy == false)) //남페이지이고 공개된 경우
                    {
                        feedList.add(now)
                        contentUidList.add(snapshot.id)
                    }
                }
            }
            rv_feeds.adapter = FeedAdapter(feedList,contentUidList)
            rv_feeds.adapter!!.notifyDataSetChanged()
        }

        rv_feeds = view.findViewById(R.id.rv_page_feeds)

        rv_feeds.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        rv_feeds.setHasFixedSize(true)



        return view
    }

//    fun newInstant() : fragmentMyFeed
//    {
//        val args = Bundle()
//        val frag = fragmentMyFeed()
//        frag.arguments = args
//        return frag
//    }

}