package bluedev_yu.coecho.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import bluedev_yu.coecho.R
import bluedev_yu.coecho.adapter.SearchHashtagAdapter
import bluedev_yu.coecho.data.model.Feeds

class FragmentResultHashtag : Fragment() {

    lateinit var rv_result_hashtag: RecyclerView
    private var orderId: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_result_hashtag, container, false)

        val hashtagList = arrayListOf(
            Feeds(null, null, null, 0, null, 0, 0, 0, "해시태그1", true),
            Feeds(null, null, null, 0, null, 0, 0, 0, "해시태그2", true),
            Feeds(null, null, null, 0, null, 0, 0, 0, "해시태그3", true),
            Feeds(null, null, null, 0, null, 0, 0, 0, "해시태그4", true)
        )

        rv_result_hashtag = view.findViewById(R.id.rv_result_hashtag)
        rv_result_hashtag.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        rv_result_hashtag.setHasFixedSize(true)
        rv_result_hashtag.adapter = SearchHashtagAdapter(hashtagList)

        return view
    }

    private fun newInstant() : FragmentResultHashtag {
        val bundle = Bundle()
        val frag = FragmentResultHashtag()
        frag.arguments = bundle
        return frag
    }
}