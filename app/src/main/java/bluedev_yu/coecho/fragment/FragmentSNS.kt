package bluedev_yu.coecho.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import bluedev_yu.coecho.data.model.FollowDTO
import bluedev_yu.coecho.adapter.FeedAdapter
import bluedev_yu.coecho.data.model.Feeds
import bluedev_yu.coecho.R
import bluedev_yu.coecho.databinding.FragmentSnsBinding
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentMap.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentSNS : Fragment() {

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    var auth : FirebaseAuth? = FirebaseAuth.getInstance()
    var firestore : FirebaseFirestore?= FirebaseFirestore.getInstance() //String 등 자료형 데이터베이스
    var firestorage : FirebaseStorage?= null //사진, GIF 등의 파일 데이터베이스

    lateinit var rv_feed: RecyclerView
    lateinit var fab: ExtendedFloatingActionButton

    private lateinit var binding: FragmentSnsBinding

    lateinit var sv_sns: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSnsBinding.inflate(layoutInflater)
        val view = binding.root

        var feedList : ArrayList<Feeds> = arrayListOf()

        //리사이클러뷰 추가하기
        rv_feed = view.findViewById(R.id.rv_feed)

        rv_feed.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        rv_feed.setHasFixedSize(true)

        var followings : ArrayList<String> = ArrayList()
        var feeds : ArrayList<Feeds> = ArrayList()


        //1) 해당 uid의 follows 가져오기

        firestore?.collection("Follow")?.document(auth?.uid.toString())?.addSnapshotListener{
            documentSnapshot, firebaseFirestoreException ->

            var document = documentSnapshot?.toObject(FollowDTO::class.java)
            if(document?.followingCount == 0) //팔로우 하는사람 없음
            {
                //자식프레그먼트 text뷰 추가 필요
                followings = arrayListOf()
            }
            else
            {
                followings = document?.followings!!
                //2) 해당 사람들의 피드 가져와서 timestamp로 정렬, 피드 보여주기

                if(followings.isNotEmpty()) //팔로우 하는사람 있을 때
                {
                    feeds.clear()
                    firestore?.collection("Feeds")?.whereIn("uid", followings)?.addSnapshotListener{
                        querySnapshot, firebaseFirestoreException ->
                        if(querySnapshot == null) {
                            Toast.makeText(this.context,"no!!!!!!!",Toast.LENGTH_LONG).show()
                            return@addSnapshotListener
                        }
                        for(snapshot in querySnapshot!!.documents)
                        {
                            var item = snapshot.toObject(Feeds::class.java)!!
                            Toast.makeText(this.context,item.uid.toString(),Toast.LENGTH_LONG).show()
                            feeds.add(item)
                        }
                        rv_feed.adapter = FeedAdapter(feeds)
                    }
                }
            }
        }




        //searchview 리스너
        sv_sns = binding.svSns
        sv_sns.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                //fragment feed에서 fragment search result로 바꿔야함!
                val transaction = childFragmentManager.beginTransaction()
                transaction.replace(R.id.layout_child, FragmentSearchResults())
                transaction.disallowAddToBackStack()
                transaction.commit()
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                return false
            }

        }
        )

        val transaction = childFragmentManager.beginTransaction()
        transaction.replace(R.id.layout_child, FragmentFeeds())
        transaction.disallowAddToBackStack()
        transaction.commit()

        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FragmentMap.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragmentSNS().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}