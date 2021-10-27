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
import bluedev_yu.coecho.adapter.ReviewAdapter
import bluedev_yu.coecho.data.model.ReviewDTO
import bluedev_yu.coecho.databinding.FragmentMyReviewBinding

class fragmentMyReview(uid: String?): Fragment() {
    private lateinit var rv_review: RecyclerView
    private lateinit var binding: FragmentMyReviewBinding
    var uid: String? = uid

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyReviewBinding.inflate(layoutInflater)
        val view = binding.root

        val bundle = Bundle()
        bundle.putString("uid", uid)

        Toast.makeText(requireContext(), "리뷰/클릭한 사람의 uid : $uid", Toast.LENGTH_SHORT).show()

        // Inflate the layout for this fragment
        val reviewList = arrayListOf(
            ReviewDTO(3.5f, null, null, 100, "리뷰1", null, "윤혜영", 0),
            ReviewDTO(4.5f, null, null, 200, "리뷰2", null, "강미주", 0),
            ReviewDTO(1.5f, null, null, 300, "리뷰3", null, "길혜주", 1),
            ReviewDTO(3.5f, null, null, 100, "리뷰4", null, "김예현", 0),
            ReviewDTO(4.5f, null, null, 200, "리뷰5", null, "강미주", 0),
        )

        rv_review = view.findViewById(R.id.rv_reviews)

        rv_review.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        rv_review.setHasFixedSize(true)

        rv_review.adapter = ReviewAdapter(reviewList)

        return view
    }

//    fun newInstant(): fragmentMyReview {
//        val args = Bundle()
//        val frag = fragmentMyReview()
//        frag.arguments = args
//        return frag
//    }
}