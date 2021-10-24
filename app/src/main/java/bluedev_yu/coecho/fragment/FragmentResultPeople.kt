package bluedev_yu.coecho.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import bluedev_yu.coecho.R
import bluedev_yu.coecho.adapter.SearchPeopleAdapter
import bluedev_yu.coecho.data.model.userDTO

class FragmentResultPeople : Fragment() {

    lateinit var rv_result_people: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_result_people, container, false)

        val userlist = arrayListOf(
            userDTO("윤혜영", null, "윤혜영", 0),
            userDTO("윤혜돌", null, "윤혜영", 0),
            userDTO("윤혜명", null, "윤혜영", 0),
            userDTO("윤혜석", null, "윤혜영", 0),
            userDTO("윤혜준", null, "윤혜영", 0),
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