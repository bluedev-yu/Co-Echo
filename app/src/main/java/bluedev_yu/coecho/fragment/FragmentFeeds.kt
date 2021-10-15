package bluedev_yu.coecho.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import bluedev_yu.coecho.R
import bluedev_yu.coecho.UploadFeed
import bluedev_yu.coecho.adapter.FeedAdapter
import bluedev_yu.coecho.data.model.Feeds
import bluedev_yu.coecho.data.model.userDTO
import bluedev_yu.coecho.databinding.FragmentFeedsBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentFeeds.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentFeeds : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding: FragmentFeedsBinding

    lateinit var rv_feed: RecyclerView
    lateinit var fab: FloatingActionButton
    lateinit var swipeRefreshLayout: SwipeRefreshLayout


    val userList = arrayListOf(
        userDTO("윤혜영", null, null, 0),
        userDTO("윤혜돌", null, null, 0),
        userDTO("윤혜명", null,  null, 0),
        userDTO("윤혜준", null,  null, 0),
        userDTO("윤혜지", null,  null, 0)
        )

    val feedList = arrayListOf(
        Feeds(null, null, "하이", 10, 14, "해시태그1", true),
        Feeds(null, null, "하이", 23, 4, "해시태그2", true),
        Feeds(null, null, "하이", 19, 44, "해시태그3", true),
        Feeds(null, null, "하이", 30, 14, "해시태그4", true),
        Feeds(null, null, "하이", 34, 95, "해시태그5", true)

        //여기다가 데이터 배열로 넣으면 돼
    )

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
        // Inflate the layout for this fragment

        binding = FragmentFeedsBinding.inflate(layoutInflater)
        val view = binding.root

        //리사이클러뷰 추가하기
        rv_feed = view.findViewById(R.id.rv_feed)

        rv_feed.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        rv_feed.setHasFixedSize(true)
        rv_feed.adapter = FeedAdapter(feedList, userList)

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

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FragmentFeeds.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragmentFeeds().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}