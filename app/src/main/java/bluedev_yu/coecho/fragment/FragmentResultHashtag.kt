package bluedev_yu.coecho.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import bluedev_yu.coecho.R
import bluedev_yu.coecho.adapter.SearchHashtagAdapter
import bluedev_yu.coecho.data.model.Feeds

class FragmentResultHashtag(query: String?) : Fragment() {

    lateinit var rv_result_hashtag: RecyclerView
    var query: String? = query

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_result_hashtag, container, false)

        val bundle = Bundle()
        bundle.putString("query", query)

        Toast.makeText(requireContext(), "해시태그 쿼리는 $query", Toast.LENGTH_SHORT).show()

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

//    private fun newInstant(query: String) : FragmentResultHashtag{
//        val bundle = Bundle()
//        val frag = FragmentResultHashtag(query)
//        bundle.getString("query", query)
//        frag.arguments = bundle
//
//        Toast.makeText(requireContext(), "검색결과 : $query", Toast.LENGTH_SHORT).show()
//        return frag
//    }
}