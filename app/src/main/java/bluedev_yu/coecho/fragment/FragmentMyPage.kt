package bluedev_yu.coecho.Fragment

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.viewpager.widget.ViewPager
import bluedev_yu.coecho.LoginActivity
import bluedev_yu.coecho.adapter.FragmentAdapter

import bluedev_yu.coecho.R
import bluedev_yu.coecho.data.model.FollowDTO
import bluedev_yu.coecho.data.model.userDTO
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentMyPage.newInstance] factory method to
 * create an instance of this fragment.
 */
// TODO: Rename and change types of parameters
class FragmentMyPage : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    private var _binding: FragmentMyPage? = null
    private val binding get() = _binding!!

    var auth : FirebaseAuth? = null
    var firestore : FirebaseFirestore?= null //String 등 자료형 데이터베이스
    var firestorage : FirebaseStorage?= null //사진, GIF 등의 파일 데이터베이스

    private var viewProfile  : View? = null
    var pickImageFromAlbum =0
    var uriPhoto : Uri?= null

    var fragmentMyFeed : Fragment ?= null
    var fragmentMyReview : Fragment ?= null
    var fragmentLikeStores : Fragment ? = null
    var fragmentSubscriber : Fragment ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewProfile =  inflater.inflate(R.layout.fragment_my_page, container, false)

        //uid 받아오기 - uid가 null이 아니면 다른 사람 페이지
        //MYPAGE, 나의에코, 톱니바퀴 버튼, 설정 총 4개 안보이도록 만들어야함
        val uid = arguments?.getString("uid")

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        firestorage = FirebaseStorage.getInstance()

        if(uid == null) //마이페이지
        {
            firestore?.collection("User")?.document(auth?.uid.toString())?.addSnapshotListener{
                    documentSnapshot, firebaseFirestoreException ->
                var document = documentSnapshot?.toObject(userDTO::class.java)
                val ProfileImage : ImageView = viewProfile!!.findViewById(R.id.MypageProfileImage)

                //칭호
                val MypageTitle : TextView = viewProfile!!.findViewById(R.id.MyPageTitle)
                if(document?.title ==0)
                {
                    MypageTitle.setText(R.string.grade1)
                }
                else
                {
                    MypageTitle.setText(R.string.grade2)
                }

                //사람이름
                val MypageUsername : TextView = viewProfile!!.findViewById(R.id.MyPageUserName)
                MypageUsername.setText(document?.strName)

                //프로필사진
                if(document?.imageUrl == null)
                    ProfileImage.setImageResource(R.drawable.default_profilephoto)
                else
                {
                    activity?.let {
                        Glide.with(it)
                            .load(document?.imageUrl)
                            .apply(RequestOptions().circleCrop()).into(viewProfile!!.findViewById(R.id.MypageProfileImage))
                    }
                }
            }

            //프로필이미지 바꾸기
            val MypageProfileOptionButton : Button = viewProfile!!.findViewById(R.id.MyPageProfileOptionButton)
            MypageProfileOptionButton.setOnClickListener{
                var photoPickerIntent = Intent(Intent.ACTION_PICK)
                photoPickerIntent.type = "image/*"
                startActivityForResult(photoPickerIntent,pickImageFromAlbum)
            }

            //로그아웃
            val LogoutButton : Button = viewProfile!!.findViewById(R.id.FollowButton)
            LogoutButton.setOnClickListener{
                auth!!.signOut()
                Toast.makeText(this.context,"로그아웃 되었습니다.",Toast.LENGTH_LONG).show()
                val intent = Intent(this.context, LoginActivity::class.java)
                startActivity(intent)
            }
        }

        else //남의 페이지
        {
            //각종 버튼 숨기기, 보이기
            val mypageText : TextView = viewProfile!!.findViewById(R.id.MypageText) //MYPAGE TEXT
            mypageText.visibility = View.INVISIBLE
            val myEchoText : TextView = viewProfile!!.findViewById(R.id.MyEchoText) //나의에코 TEXT
            myEchoText.visibility = View.INVISIBLE
            val MyPageOptionButton : ImageView = viewProfile!!.findViewById(R.id.MyPageOptionButton) //옵션 네비게이션 드로워
            MyPageOptionButton.visibility = View.INVISIBLE
            val MypageProfileOptionButton : Button = viewProfile!!.findViewById(R.id.MyPageProfileOptionButton) //마이페이지 배경변경 버튼
            MypageProfileOptionButton.visibility = View.INVISIBLE
            val followButton : Button = viewProfile!!.findViewById(R.id.FollowButton) //팔로우 버튼 팔로우/언팔로우로 보이기
            followButton.setText("팔로우")


            firestore?.collection("User")?.document(uid)?.addSnapshotListener{
                    documentSnapshot, firebaseFirestoreException ->
                var document = documentSnapshot?.toObject(userDTO::class.java)
                val ProfileImage : ImageView = viewProfile!!.findViewById(R.id.MypageProfileImage)

                //칭호
                val MypageTitle : TextView = viewProfile!!.findViewById(R.id.MyPageTitle)
                if(document?.title ==0)
                {
                    MypageTitle.setText(R.string.grade1)
                }
                else
                {
                    MypageTitle.setText(R.string.grade2)
                }

                //사람이름
                val MypageUsername : TextView = viewProfile!!.findViewById(R.id.MyPageUserName)
                MypageUsername.setText(document?.strName)

                //프로필사진
                if(document?.imageUrl == null)
                    ProfileImage.setImageResource(R.drawable.default_profilephoto)
                else
                {
                    activity?.let {
                        Glide.with(it)
                            .load(document?.imageUrl)
                            .apply(RequestOptions().circleCrop()).into(viewProfile!!.findViewById(R.id.MypageProfileImage))
                    }
                }
            }

            //내가 팔로우하고 있는가 아닌가에 따라 변경 -> 나의 following 에 없으면 버튼이름 "팔로우", 있으면 "팔로우 취소"
            //follower -> 나를, following -> 내가
            //팔로우시 내 following에 남 추가, 남의 follower에 나 추가
            //팔로우 취소시 내 following에서 남 삭제, 남의 follower에서 나 삭제

            firestore?.collection("Follow")?.document(auth?.uid.toString())?.addSnapshotListener{ //내 팔로우 데이터 가져오기
                    documentSnapshot, firebaseFirestoreException ->

                var document = documentSnapshot?.toObject(FollowDTO::class.java) //내 팔로우 데이터
                if(document!!.followings.contains(uid)) //내가 이미 팔로우하고 있는 경우
                {
                    Log.v("contains",document.toString())
                    followButton.setText("팔로우 취소")
                }
                else
                {
                    Log.v("Notcontains",document.toString())
                    followButton.setText("팔로우")
                }

            }

            Log.v("followButtonText",followButton.text.toString())

            //언팔로우하기
            if(followButton.text.toString().equals("팔로우 취소"))//이미 있음
            {
                followButton.setOnClickListener{
                    //팔로우 항목에서 제거하기
                    val docRef = firestore?.collection("Follow")?.document(auth?.uid.toString())
                    val updates = hashMapOf<String,Any>(uid to FieldValue.delete())

                    docRef?.update("followings",updates)?.addOnCompleteListener{
                        followButton.setText("팔로우")
                    }
                    //uid 항목을 삭제
                }
            }
            else //팔로우하기
            {
                followButton.setOnClickListener{
                    //팔로우 항목에서 제거하기
                    val docRef = firestore?.collection("Follow")?.document(auth?.uid.toString())
                    val addfollowing = hashMapOf<String,Any>()
                    addfollowing.put(uid,uid)

                    docRef?.update("followings",addfollowing)?.addOnCompleteListener{
                        followButton.setText("팔로우 취소")
                    }
                    //uid 항목을 삭제
                }
            }


        }


        //탭레이아웃
        val fragmentManager = (activity as FragmentActivity).supportFragmentManager


        val pagerAdapter = FragmentAdapter(fragmentManager)
        val pager = viewProfile!!.findViewById<ViewPager>(R.id.viewPager)
        pager.adapter = pagerAdapter
        val tab = viewProfile!!.findViewById<TabLayout>(R.id.MyPageTabs)
        tab.setupWithViewPager(pager)


        return viewProfile
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == pickImageFromAlbum){
            if(resultCode == Activity.RESULT_OK){
                uriPhoto = data?.data
                val MypageProfileImage : ImageView = viewProfile!!.findViewById(R.id.MypageProfileImage)
                MypageProfileImage.setImageURI(uriPhoto)

                if(ContextCompat.checkSelfPermission(viewProfile!!.context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                    funImageUpLoad(viewProfile!!)
                }
            }
        }
    }

    private fun funImageUpLoad(view : View){
        //var timestamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        var user = auth?.currentUser?.uid.toString()
        var imgFileName = "PROFILE"+user+".png"
        var storageRef = firestorage?.reference?.child("ProfileImages")?.child(imgFileName)

        storageRef?.putFile(uriPhoto!!)?.addOnSuccessListener {
            storageRef.downloadUrl.addOnSuccessListener{
                uri->
                var userInfo = userDTO()
                userInfo.imageUrl = uri.toString()
                firestore?.collection("User")?.document(auth?.uid.toString())?.update("imageUrl",userInfo.imageUrl)
            }
        }

    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FragmentMyPage.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragmentMyPage().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}