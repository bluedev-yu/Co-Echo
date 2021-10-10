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
import bluedev_yu.coecho.adapter.SearchPeopleAdapter
import bluedev_yu.coecho.data.model.Feeds

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentResultHashtag.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentResultHashtag : Fragment() {

    lateinit var rv_result_hashtag: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_result_hashtag, container, false)

        val hashtagList = arrayListOf(
            Feeds(null, null, null, 0, 0, "해시태그1", false),
            Feeds(null, null, null, 0, 0, "해시태그2", false),
            Feeds(null, null, null, 0, 0, "해시태그3", false),
            Feeds(null, null, null, 0, 0, "해시태그4", false)
        )

        rv_result_hashtag = view.findViewById(R.id.rv_result_hashtag)
        rv_result_hashtag.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        rv_result_hashtag.setHasFixedSize(true)
        rv_result_hashtag.adapter = SearchHashtagAdapter(hashtagList)

        return view
    }

    private fun newInstant() : FragmentResultHashtag
    {
        val args = Bundle()
        val frag = FragmentResultHashtag()
        frag.arguments = args
        return frag
    }
}