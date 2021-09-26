package bluedev_yu.coecho.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import bluedev_yu.coecho.FeedAdapter
import bluedev_yu.coecho.Feeds
import bluedev_yu.coecho.R
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.floatingactionbutton.FloatingActionButton

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
        Feeds(R.drawable.man1, "차은우", "안녕하세용가리~", "#친환경 브랜드", R.drawable.feedimg2, 2, 10),
        Feeds(R.drawable.woman1, "강미주", "제 말이 들리시나요?", "#러쉬", R.drawable.feedimg1, 100, 29),
        Feeds(R.drawable.man1, "김동률", "전 할말 없어여~", "#해시태그", R.drawable.feedimg2, 20, 12),
        Feeds(R.drawable.woman1, "길혜주", "코에코 화이팅~", "뭐하징", R.drawable.feedimg1, 10, 29),
        Feeds(R.drawable.man1, "홍길동", "오랜만에 신세계 러쉬 갔다왔어요~~~여러분!! 러쉬가 친환경 브랜드인거 알고계셨나요?? 입욕제로 유명한 건 알고있었는데 패키지까지 친환경인 건 몰랐어요!! 종이랑 안에있는 완충제까지 전부 다 물에 녹는 성분이래요!! 여러분들도 러쉬한 번 들려보세용ㅎㅎㅎ",
        "이렇게하는건가요", R.drawable.feedimg2, 22, 104),
        Feeds(R.drawable.woman1, "김예현", "빵꾸똥꾸", "빵꾸똥꾸", R.drawable.feedimg1, 110, 299)
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
            println("보이니~~~~~~~~~~~~~~~")
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