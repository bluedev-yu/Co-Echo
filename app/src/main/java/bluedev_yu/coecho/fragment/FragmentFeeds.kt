package bluedev_yu.coecho.Fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import bluedev_yu.coecho.R
import bluedev_yu.coecho.UploadFeed
import bluedev_yu.coecho.adapter.FeedAdapter
import bluedev_yu.coecho.data.model.Feeds
import bluedev_yu.coecho.data.model.FollowDTO
import bluedev_yu.coecho.data.model.userDTO
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

    var userSet = hashSetOf<String>()
    var userList = arrayListOf<userDTO>()
    val feedList = arrayListOf(
        Feeds(null, null, "하이 안녕하세요 저는 윤혜영입니다. 안녕하세요~~~~ 안녕하세요 안녕하세요 ~~~ 안녕하세요 ~~~ 안녕하세요 ~~~ 안녕하세요 ~~~", null,  10, 14, "해시태그1", true),
        Feeds(null, null, "하이", null, 23, 4, "해시태그2", true),
        Feeds(null, null, "하이", null,  19, 44, "해시태그3", true),
        Feeds(null, null, "하이", null,  30, 14, "해시태그4", true),
        Feeds(null, null, "하이", null,  34, 95, "해시태그5", true)

        //여기다가 데이터 배열로 넣으면 돼
    )

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
                    feedList.clear()
                    firestore?.collection("Feeds")?.whereIn("uid", followings.values.toList())?.addSnapshotListener{
                            querySnapshot, firebaseFirestoreException ->
                        if(querySnapshot == null) {
                            Toast.makeText(this.context,"no!!!!!!!",Toast.LENGTH_LONG).show()
                            return@addSnapshotListener
                        }
                        for(snapshot in querySnapshot!!.documents)
                        {
                            var item = snapshot.toObject(Feeds::class.java)!!
                            feedList.add(item)
                            firestore?.collection("User")?.document(item.uid.toString())?.addSnapshotListener{
                                    documentSnapshots, firebaseFirestoreException ->
                                var user = documentSnapshots?.toObject(userDTO::class.java)
                                if(!userSet.contains(user?.uid.toString())) //처음본 유저
                                {
                                    userSet.add(user?.uid.toString())
                                    userList.add(user!!)
                                }
                                //개선 필요
                                rv_feed.adapter = FeedAdapter(feedList,userList)
                            }
                        }
                    }
                }
            }
        }
//
//        firestore?.collection("Follow")?.document(auth?.uid.toString())?.addSnapshotListener{
//                documentSnapshot, firebaseFirestoreException ->
//
//            var document = documentSnapshot?.toObject(FollowDTO::class.java)
//            if(document?.followingCount == 0) //팔로우 하는사람 없음
//            {
//                //자식프레그먼트 text뷰 추가 필요
//                followings = arrayListOf()
//            }
//            else
//            {
//                followings = document?.followings!!
//                //2) 해당 사람들의 피드 가져와서 timestamp로 정렬, 피드 보여주기
//
//                if(followings.isNotEmpty()) //팔로우 하는사람 있을 때
//                {
//                    feedList.clear()
//                    firestore?.collection("Feeds")?.whereIn("uid", followings)?.addSnapshotListener{
//                            querySnapshot, firebaseFirestoreException ->
//                        if(querySnapshot == null) {
//                            Toast.makeText(this.context,"no!!!!!!!",Toast.LENGTH_LONG).show()
//                            return@addSnapshotListener
//                        }
//                        for(snapshot in querySnapshot!!.documents)
//                        {
//                            var item = snapshot.toObject(Feeds::class.java)!!
//                            feedList.add(item)
//                            firestore?.collection("User")?.document(item.uid.toString())?.addSnapshotListener{
//                                    documentSnapshots, firebaseFirestoreException ->
//                                var user = documentSnapshots?.toObject(userDTO::class.java)
//                                if(!userSet.contains(user?.uid.toString())) //처음본 유저
//                                {
//                                    userSet.add(user?.uid.toString())
//                                    userList.add(user!!)
//                                }
//                                //개선 필요
//                                rv_feed.adapter = FeedAdapter(feedList,userList)
//                            }
//                        }
//                    }
//                }
//            }
//        }

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
            Toast.makeText(requireContext(), "하이~~~", Toast.LENGTH_SHORT).show()
            swipeRefreshLayout.isRefreshing = false
        }

        return view
    }

}