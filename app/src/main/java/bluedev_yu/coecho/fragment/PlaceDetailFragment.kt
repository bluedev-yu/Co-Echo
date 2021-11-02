package bluedev_yu.coecho.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import bluedev_yu.coecho.*
import bluedev_yu.coecho.adapter.ReviewAdapter
import bluedev_yu.coecho.data.model.Place
import bluedev_yu.coecho.data.model.ReviewDTO
import com.google.firebase.FirebaseException
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_place_detail.*
import kotlinx.android.synthetic.main.fragment_place_detail.view.*
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await

class PlaceDetailFragment : Fragment() {

    var firebase : FirebaseFirestore?= null
    var auth : FirebaseAuth ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        firebase = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        var reviewList = arrayListOf<ReviewDTO>()
        var thisFragView = inflater.inflate(R.layout.fragment_place_detail, container, false)
        val pName = arguments?.getString("name")
        val pAddress = arguments?.getString("address")
        val pCategory = arguments?.getString("category")
        val pPhone = arguments?.getString("phone")
        val pUrl = arguments?.getString("url")
        val pX = arguments?.getDouble("x")
        val pY = arguments?.getDouble("y")

        thisFragView.tv_placename_placedetail.setText(pName)
        thisFragView.tv_address_placedetail.setText(pAddress)
        thisFragView.tv_category_placedetail.setText(pCategory)
        thisFragView.tv_phone_placedetail.setText(pPhone)
        thisFragView.tv_url_placedetail.setText(pUrl)

        thisFragView.ib_backbutton.setOnClickListener {
            requireActivity().onBackPressed()
        }


        lateinit var pid: String
        val t_rv_review = thisFragView.findViewById<RecyclerView>(R.id.rv_placeReview)
        t_rv_review.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        t_rv_review.setHasFixedSize(true)
        t_rv_review.getRecycledViewPool().clear()

        val t_scrollItem =
            thisFragView.findViewById<LinearLayout>(R.id.ll_horizontal_item)

        var tempRes: Pair<String, String>?
        CoroutineScope(Dispatchers.Main).launch {
            pid = DB_Place().search_data(
                pName!!, pAddress!!
            )
            Log.i("placeId", "앙영" + pid)
            if (pid == "false") {
                Toast.makeText(activity, "리뷰 로드 오류 발생", Toast.LENGTH_LONG).show()
            } else if (pid == "none") {
                Toast.makeText(activity, "리뷰가 아직 없습니다", Toast.LENGTH_LONG).show()
            } else {
                CoroutineScope(Dispatchers.Main).launch {
                    tempRes = DB_Review().getHashtag(pid)
                    Log.d("리뷰 검색 결과", tempRes.toString())
                    if (tempRes != null) {
                        if (tempRes!!.first != "") {
                            layout_hashtag1.visibility = View.VISIBLE
                            hashtag1_1.text = tempRes!!.first
                        }
                        if (tempRes!!.second != "") {
                            layout_hashtag2.visibility = View.VISIBLE
                            hashtag1_2.text = tempRes!!.second
                        }
                    } else {
                        ll_horizontal_item.addView(inflater.inflate(R.layout.no_image_item, null))
                    }
                    loadPlaceReview(pid, reviewList)
                    Log.i("로드되었다", reviewList.toString())
                    val tvCount = thisFragView.findViewById<TextView>(R.id.tv_review_cnt)
                    tvCount.setText("방문자리뷰 " + reviewList.size)
                    t_rv_review.adapter = ReviewAdapter(reviewList)
                    t_rv_review.adapter!!.notifyDataSetChanged()

//                    val db = FirebaseFirestore.getInstance().collection("Reviews")
//                    val a = db.whereEqualTo("pid", pid).get()
//                    a.await()
//                    for (doc in a.result.documents) {
//                        var img = doc.data.get("reviewImage")
//                        if (img != null) {
//                            Glide.with(requireContext())
//                                .load(img)
//                                .into(holder.reviewImage) //피드 이미지
//                            // }
//                        }
                    }
                }
            }


        firebase?.collection("User")?.document(auth?.uid.toString())?.addSnapshotListener{
                documentSnapshot, FirebaseFirestoreException ->
            //미리 북마크가 비었는가 찼는가
            var doc = documentSnapshot?.toObject(userDTO::class.java)
            CoroutineScope(Dispatchers.Main).launch {
                pid = DB_Place().search_data(
                    pName!!, pAddress!!
                )
            if (doc?.placeLike!!.containsKey(pid)) //좋아요 눌렀을 경우
            {
                thisFragView.bookmark.setBackgroundResource(R.drawable.bookmark_filled)
            } else {
                thisFragView.bookmark.setBackgroundResource(R.drawable.bookmark_empty)
            }
            }
        }

        thisFragView.bookmark.setOnClickListener{
            placelikeEvent(pid!!)
        }


        thisFragView.findViewById<Button>(R.id.btn_write_review).setOnClickListener {
            if (pid == "false")
                Toast.makeText(activity, "pid 로드 오류 발생", Toast.LENGTH_LONG).show()
            else {
                if (pid == "none") {
                    //Log.i("널 췍",pName+pCategory+pPhone+pAddress+pAddress+pUrl+pX+pY)
                    CoroutineScope(Dispatchers.Main).launch {
                        DB_Place().insert_data(
                            Place(
                                pName!!,
                                pCategory!!,
                                pPhone!!,
                                pAddress!!,
                                pUrl!!,
                                "",
                                pX!!,
                                pY!!
                            )
                        )
                        Log.i("넘어가는placeId", "앙영" + pid)
                        val intent = Intent(requireContext(), UploadReview::class.java)
                        intent.putExtra("pName", pName)
                        intent.putExtra("pAddress", pAddress)
                        requireContext().startActivity(intent)
                    }
                } else {
                        Log.i("넘어가는placeId", "앙영" + pid)
                        val intent = Intent(requireContext(), UploadReview::class.java)
                        intent.putExtra("pid", pid)
                        requireContext().startActivity(intent)

                }
            }
        }

            // Inflate the layout for this fragment
            return thisFragView
        }
        suspend fun loadPlaceReview(pid: String, list: ArrayList<ReviewDTO>): Boolean {
            val db = Firebase.firestore
            var a = db.collection("Reviews").whereEqualTo("pid", pid).get()
            try {
                a.await()
                if (a.result.isEmpty)
                    return false
                else {
                    for (document in a.result) {
                        list.add(document.toObject(ReviewDTO::class.java))
                    }
                    return true
                }
            } catch (e: FirebaseException) {
                Log.e("error:", "error:" + e.message.toString())
                return false
            }
        }

    fun placelikeEvent(pid : String)
    {
        var tsDoc = firebase?.collection("User")?.document(auth?.uid.toString())
        firebase?.runTransaction{
                transaction ->

            val uid = auth?.uid.toString()
            val userDTO = transaction.get(tsDoc!!).toObject(userDTO::class.java)

            if(userDTO!!.placeLike.containsKey(pid)){ //이미 북마크 한 경우 -> 북마크 철회
                Log.v("bookmark","철회")
                userDTO?.placeLike.remove(pid)
            }
            else //좋아요 아직 안함 -> 좋아요 하기
            {
                Log.v("bookmark","추가")
                userDTO.placeLike[pid] = pid
            }
            transaction.set(tsDoc,userDTO)
        }
    }

}