package bluedev_yu.coecho.Fragment

import android.os.Bundle
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

private lateinit var binding: FragmentMyFeedBinding

class fragmentMyFeed(uid: String?) : Fragment() {
    private lateinit var rv_feeds: RecyclerView
    var uid: String? = uid

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyFeedBinding.inflate(layoutInflater)
        val view = binding.root

        val bundle = Bundle()
        bundle.putString("uid", uid)

        Toast.makeText(requireContext(), "피드/클릭한 사람의 uid : $uid", Toast.LENGTH_SHORT).show()

        // Inflate the layout for this fragment
        val feedList = arrayListOf(
            Feeds(null, "윤혜영", null, 0, "마이페이지 피드", 200, 10, 20, "해시태그")
        )

        val contentUidList = arrayListOf("길혜주")

        rv_feeds = view.findViewById(R.id.rv_page_feeds)

        rv_feeds.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        rv_feeds.setHasFixedSize(true)

        rv_feeds.adapter = FeedAdapter(feedList, contentUidList)


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