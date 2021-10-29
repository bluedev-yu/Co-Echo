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
import bluedev_yu.coecho.databinding.FragmentMapHashtagBinding
import bluedev_yu.coecho.databinding.FragmentMapSearchBinding


class FragmentMapHashtag(query:String?) : Fragment() {

//    lateinit var recyclerView: RecyclerView
    private lateinit var binding: FragmentMapHashtagBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentMapHashtagBinding.inflate(inflater, container, false)
        val view = binding.root

//        val hashtagList = arrayListOf(
//            Feeds(null, null, null, 0, null, 0, 0, 0, "해시태그1", true),
//            Feeds(null, null, null, 0, null, 0, 0, 0, "해시태그2", true),
//            Feeds(null, null, null, 0, null, 0, 0, 0, "해시태그3", true),
//            Feeds(null, null, null, 0, null, 0, 0, 0, "해시태그4", true)
//        )

//        recyclerView = binding.MapHashtagRecylerView
//        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
//        recyclerView.setHasFixedSize(true)
//        recyclerView.adapter = SearchHashtagAdapter(hashtagList)

        return view
    }

    private fun newInstant(query: String?): FragmentMapHashtag {
        val bundle = Bundle()
        val frag = FragmentMapHashtag(query)
        bundle.putString("query", query)
        frag.arguments = bundle
        return frag
    }


}