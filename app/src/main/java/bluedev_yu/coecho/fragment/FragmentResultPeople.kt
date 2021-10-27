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
import bluedev_yu.coecho.adapter.FeedAdapter
import bluedev_yu.coecho.adapter.SearchPeopleAdapter
import bluedev_yu.coecho.data.model.Feeds
import bluedev_yu.coecho.data.model.FollowDTO
import bluedev_yu.coecho.data.model.userDTO
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class FragmentResultPeople(query: String?) : Fragment() {

    lateinit var rv_result_people: RecyclerView
    var query: String? = query
    var firestore : FirebaseFirestore? = null
    var auth : FirebaseAuth?= null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_result_people, container, false)

        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        val bundle = Bundle()
        bundle.putString("query", query)

        //검색
        val userlist = arrayListOf<userDTO>()

        firestore?.collection("User")?.addSnapshotListener{
                querySnapshot, firebaseFirestoreException ->
            userlist.clear()
            if(querySnapshot == null) {
                return@addSnapshotListener
            }
            for(snapshot in querySnapshot!!.documents)
            {
                val imsi = snapshot.toObject(userDTO::class.java)
                if(imsi?.strName!!.contains(query!!) && !imsi.uid!!.equals(auth?.uid.toString())) //검색내용 포함시
                    userlist.add(imsi)
            }

            rv_result_people.adapter = SearchPeopleAdapter(userlist)
            rv_result_people.adapter!!.notifyDataSetChanged()

            if(userlist.size == 0){
                Toast.makeText(requireContext(), "사용자 검색 결과 없음", Toast.LENGTH_SHORT).show()
            }
        }

        rv_result_people = view.findViewById(R.id.rv_result_people)
        rv_result_people.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        rv_result_people.setHasFixedSize(true)

        return view

    }

//    private fun newInstant(query: String?): FragmentResultPeople {
//        val bundle = Bundle()
//        val frag = FragmentResultPeople(query)
//        bundle.putString("query", query)
//        frag.arguments = bundle
//
//        Toast.makeText(requireContext(), "내 쿼리는 $query", Toast.LENGTH_SHORT).show()
//
//        return frag
//    }
}