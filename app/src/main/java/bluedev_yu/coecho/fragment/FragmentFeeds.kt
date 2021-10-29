package bluedev_yu.coecho.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.compose.animation.core.snap
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import bluedev_yu.coecho.R
import bluedev_yu.coecho.UploadFeed
import bluedev_yu.coecho.adapter.FeedAdapter
import bluedev_yu.coecho.data.model.Feeds
import bluedev_yu.coecho.data.model.FollowDTO
import bluedev_yu.coecho.databinding.FragmentFeedsBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class FragmentFeeds : Fragment() {

    private lateinit var binding: FragmentFeedsBinding

    lateinit var rv_feed: RecyclerView
    lateinit var fab: FloatingActionButton
    lateinit var swipeRefreshLayout: SwipeRefreshLayout

    var auth : FirebaseAuth? = null
    var firestore : FirebaseFirestore?= null //String 등 자료형 데이터베이스
    var firestorage : FirebaseStorage?= null //사진, GIF 등의 파일 데이터베이스
    var followings :MutableMap<String,String> = mutableMapOf()

    var feedList = arrayListOf<Feeds>()
    var contentUidList = arrayListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentFeedsBinding.inflate(layoutInflater)
        val view = binding.root

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        firestorage = FirebaseStorage.getInstance()

        //리사이클러뷰 추가하기
        rv_feed = view.findViewById(R.id.rv_feed)
        val nofeed = view.findViewById<TextView>(R.id.nofeed)

        rv_feed.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        rv_feed.setHasFixedSize(true)
        //1) 해당 uid의 follows 가져오기

        firestore?.collection("Follow")?.document(auth?.uid.toString())?.addSnapshotListener{
                documentSnapshot, firebaseFirestoreException ->

            //Log.v("Following document", documentSnapshot?.toObject(FollowDTO::class.java).toString())
            var document = documentSnapshot?.toObject(FollowDTO::class.java)
            if(document == null)
            {

            }
            else if(document?.followingCount == 0) //팔로우 하는사람 없음
            {
                //자식프레그먼트 text뷰 추가 필요
                followings = mutableMapOf()
            }
            else
            {
                followings = document?.followings!!
                //2) 해당 사람들의 피드 가져와서 timestamp로 정렬, 피드 보여주기

                if(followings.isNotEmpty()) //팔로우 하는사람 있을 때
                {
                    Log.v("FeedSize",feedList.size.toString())

                    firestore?.collection("Feeds")?.whereIn("uid", followings.values.toList())?.addSnapshotListener{
                            querySnapshot, firebaseFirestoreException ->
                        feedList.clear()
                        contentUidList.clear()
                        if(querySnapshot == null) {
                            return@addSnapshotListener
                        }
                        for(snapshot in querySnapshot!!.documents)
                        {
                            var now = snapshot.toObject(Feeds::class.java)
                            if(now?.uid!!.equals(auth?.uid.toString()) || (now?.uid!!.equals(auth?.uid.toString())==false && now.privacy == false)) //내것이거나, 남것이지만 public인 경우
                            {
                                feedList.add(now!!)
                                contentUidList.add(snapshot.id)
                            }
                        }
                        if(feedList.size ==0)
                        {
                            nofeed.visibility = View.VISIBLE
                        }
                        else
                        {
                            nofeed.visibility = View.INVISIBLE
                            rv_feed.adapter = FeedAdapter(feedList,contentUidList)
                            rv_feed.adapter!!.notifyDataSetChanged()
                        }
                    }
                }
            }
        }

        //fab 클릭하면 피드 작성 페이지로
        fab = view.findViewById(R.id.btn_uploadFeed)
        fab.setOnClickListener {
            val intent = Intent(requireContext(), UploadFeed::class.java)
            requireContext().startActivity(intent)
        }

        //pull to refresh
        swipeRefreshLayout = binding.swipeRefreshLayout
        swipeRefreshLayout.setOnRefreshListener {
            //스와이프 할 때마다 피드 추가
            swipeRefreshLayout.isRefreshing = false
        }

        return view
    }

}