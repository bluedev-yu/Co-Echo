package bluedev_yu.coecho.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import bluedev_yu.coecho.R
import bluedev_yu.coecho.adapter.FeedAdapter
import bluedev_yu.coecho.adapter.ReviewAdapter
import bluedev_yu.coecho.data.model.Feeds
import bluedev_yu.coecho.data.model.ReviewDTO
import bluedev_yu.coecho.databinding.FragmentMyReviewBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class fragmentMyReview(uid: String?): Fragment() {
    private lateinit var rv_review: RecyclerView
    private lateinit var binding: FragmentMyReviewBinding
    var uid: String? = uid
    var firestore : FirebaseFirestore? = null
    var auth : FirebaseAuth?= null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyReviewBinding.inflate(layoutInflater)
        val view = binding.root
        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        if(uid == null) //마이페이지
        {
            uid = auth?.uid!!.toString()
        }

        val bundle = Bundle()
        bundle.putString("uid", uid)


        val reviewList = arrayListOf<ReviewDTO>()

        firestore?.collection("Reviews")?.addSnapshotListener {
                querySnapshot, firebaseFirestoreException ->
            reviewList.clear()
            //contentUidList.clear()
            if (querySnapshot == null) {
                return@addSnapshotListener
            }
            for (snapshot in querySnapshot!!.documents) {
                val imsi = snapshot.toObject(ReviewDTO::class.java)
                if (imsi?.uid!!.equals(uid)) //내가 썼을 시
                {
                    reviewList.add(imsi)
                    //contentUidList.add(snapshot.id)
                }
            }
            rv_review.adapter = ReviewAdapter(reviewList)
            rv_review.adapter!!.notifyDataSetChanged()
        }

        // Inflate the layout for this fragment

        rv_review = view.findViewById(R.id.rv_reviews)

        rv_review.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        rv_review.setHasFixedSize(true)


        return view
    }

//    fun newInstant(): fragmentMyReview {
//        val args = Bundle()
//        val frag = fragmentMyReview()
//        frag.arguments = args
//        return frag
//    }
}