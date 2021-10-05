package bluedev_yu.coecho.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import bluedev_yu.coecho.FeedAdapter
import bluedev_yu.coecho.R
import bluedev_yu.coecho.data.model.Feeds

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [fragmentMyFeed.newInstance] factory method to
 * create an instance of this fragment.
 */
class fragmentMyFeed : Fragment() {
    private lateinit var rv_myfeed: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val myfeedList = arrayListOf(
            Feeds(null, null, null, "윤혜영", "나야나", 100, 90, "해시태그1"),

            Feeds(null, null, null, "윤혜영", "dd", 10, 0, "해시태그2"),

            Feeds(null, null, null, "윤혜영", "ss", 90, 90, "해시태그3")
        )

        val view = inflater.inflate(R.layout.fragment_my_feed, container, false)

        //리사이클러뷰 추가하기
        rv_myfeed = view.findViewById(R.id.rv_myfeed)

        rv_myfeed.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        rv_myfeed.setHasFixedSize(true)

        rv_myfeed.adapter = FeedAdapter(myfeedList)

        return view
    }

    fun newInstant(): fragmentMyFeed {
        val args = Bundle()
        val frag = fragmentMyFeed()
        frag.arguments = args
        return frag
    }

}