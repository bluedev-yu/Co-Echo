package bluedev_yu.coecho.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import bluedev_yu.coecho.FeedAdapter
import bluedev_yu.coecho.data.model.Feeds
import bluedev_yu.coecho.R
import bluedev_yu.coecho.UploadFeed
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton

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

    lateinit var rv_feed: RecyclerView
    lateinit var fab: ExtendedFloatingActionButton

    val feedList = arrayListOf(
        Feeds(null, null, null, "윤혜영", "안녕하세용", 22, 10, "#친환경")
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
        val view = inflater.inflate(R.layout.fragment_sns, container, false)

        //리사이클러뷰 추가하기
        rv_feed = view.findViewById(R.id.rv_feed)

        rv_feed.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        rv_feed.setHasFixedSize(true)

        rv_feed.adapter = FeedAdapter(feedList)

        fab = view.findViewById(R.id.btn_upload)
        fab.setOnClickListener {
            val intent = Intent(requireContext(), UploadFeed::class.java)
            requireContext().startActivity(intent)
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
         * @return A new instance of fragment FragmentMap.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragmentMap().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}