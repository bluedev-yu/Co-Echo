package bluedev_yu.coecho.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import bluedev_yu.coecho.R
import bluedev_yu.coecho.adapter.FeedAdapter
import bluedev_yu.coecho.adapter.SearchPeopleAdapter
import bluedev_yu.coecho.data.model.Feeds
import bluedev_yu.coecho.data.model.userDTO

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentResultPeople.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentResultPeople : Fragment() {

    lateinit var rv_result_people: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_result_people, container, false)

        val userlist = arrayListOf(
            Feeds(R.drawable.man1, null, "윤혜영", "윤혜영", null, 0, 0, null),
            Feeds(R.drawable.woman1, null, "길혜주", "길혜주", null, 0, 0, null),
            Feeds(R.drawable.man1, null, "김예현", "김예현", null, 0, 0, null),
            Feeds(R.drawable.woman1, null, "강미주", "강미주", null, 0, 0, null),
            )

        rv_result_people = view.findViewById(R.id.rv_result_people)
        rv_result_people.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        rv_result_people.setHasFixedSize(true)
        rv_result_people.adapter = SearchPeopleAdapter(userlist)

        return view

    }

    private fun newInstant(): FragmentResultPeople {
        val args = Bundle()
        val frag = FragmentResultPeople()
        frag.arguments = args
        return frag
    }
}