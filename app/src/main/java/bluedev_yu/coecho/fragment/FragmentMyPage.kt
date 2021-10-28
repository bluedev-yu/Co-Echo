package bluedev_yu.coecho.fragment

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
import com.google.android.gms.auth.api.Auth
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class FragmentMyPage : Fragment() {
    private var _binding: FragmentMyPage? = null
    private val binding get() = _binding!!

    var auth : FirebaseAuth? = null
    var firestore : FirebaseFirestore?= null //String 등 자료형 데이터베이스
    var firestorage : FirebaseStorage?= null //사진, GIF 등의 파일 데이터베이스

    private var viewProfile  : View? = null
    var pickImageFromAlbum =0
    var uriPhoto : Uri?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

        val MypageFollower : TextView = viewProfile!!.findViewById(R.id.MyPageFollower) //팔로워 수
        val MyPageFollowing : TextView = viewProfile!!.findViewById(R.id.MyPageFollowing) //팔로잉수

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        firestorage = FirebaseStorage.getInstance()

        if(uid == null || uid == auth?.uid.toString()) //마이페이지
        {
            firestore?.collection("User")?.document(auth?.uid.toString())?.addSnapshotListener{
                    documentSnapshot, firebaseFirestoreException ->
                var document = documentSnapshot?.toObject(userDTO::class.java)
                val ProfileImage : ImageView = viewProfile!!.findViewById(R.id.MypageProfileImage)

                //칭호
                val MypageTitle : TextView = viewProfile!!.findViewById(R.id.MyPageTitle)
                Log.v("title?",document?.title.toString())
                if(document?.title!! <20) //칭호
                {
                    MypageTitle.setText(R.string.grade1)
                }
                else if(document?.title!!  <40) //칭호
                {
                    MypageTitle.setText(R.string.grade2)
                }
                else
                    MypageTitle.setText(R.string.grade3)

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

            firestore?.collection("Follow")?.document(auth?.uid.toString())?.addSnapshotListener{ //팔로워 팔로잉 수
                    documentSnapshot, firebaseFirestoreException ->
                var followDTO = documentSnapshot?.toObject(FollowDTO::class.java)
                MypageFollower.setText((followDTO?.followerCount!!-1).toString()+" 팔로워") //한명은 자기 자신
                MyPageFollowing.setText((followDTO?.followingCount!!-1).toString()+" 팔로잉")
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

            firestore?.collection("Follow")?.document(auth?.uid.toString())?.addSnapshotListener{
                    documentSnapshot, firebaseFirestoreException ->
                var followDTO = documentSnapshot?.toObject(FollowDTO::class.java)
                if(followDTO?.followings!!.containsKey(uid.toString())) //follow하고 있을 경우
                {
                    followButton.setText("팔로우 취소")
                }
                else
                {
                    followButton.setText("팔로우")
                }
            }

            firestore?.collection("Follow")?.document(uid)?.addSnapshotListener{ //팔로워 팔로잉 수
                    documentSnapshot, firebaseFirestoreException ->
                var followDTO = documentSnapshot?.toObject(FollowDTO::class.java)
                MypageFollower.setText((followDTO?.followerCount!!-1).toString()+" 팔로워") //한명은 자기 자신
                MyPageFollowing.setText((followDTO?.followingCount!!-1).toString()+" 팔로잉")
            }

            //유저정보 가져오기
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

            followButton.setOnClickListener{
                var tsDocFollowing = firestore?.collection("Follow")?.document(auth?.uid.toString())
                firestore?.runTransaction { //내 팔로우 데이터 가져오기
                        transaction ->
                    var followDTO = transaction.get(tsDocFollowing!!).toObject(FollowDTO::class.java)
                    if (followDTO == null) //empty
                    {
                        followDTO = FollowDTO()
                        followDTO.followingCount = 2
                        followDTO.followings[uid!!] = uid
                        followDTO.followings[auth?.uid.toString()] = auth?.uid.toString()
                        followDTO.followers[auth?.uid.toString()] = auth?.uid.toString()

                        if (tsDocFollowing != null) {
                            transaction.set(tsDocFollowing, followDTO)
                        }
                        return@runTransaction
                    }

                    if (followDTO?.followings?.containsKey(uid)) { //이미 팔로우하고 있을 경우 -> 언팔로우
                        followDTO?.followingCount = followDTO?.followingCount - 1
                        followDTO?.followings.remove(uid)
                    } else //팔로우하기
                    {
                        followDTO?.followingCount = followDTO?.followingCount + 1
                        followDTO?.followings[uid] = uid
                    }

                    if (tsDocFollowing != null) {
                        transaction.set(tsDocFollowing, followDTO)
                    }
                    return@runTransaction
                }

                var tsDocFollower = firestore?.collection("Follow")?.document(uid) //남의 아이디
                firestore?.runTransaction { //내 팔로우 데이터 가져오기
                        transaction ->

                    var followDTO =
                        tsDocFollower?.let { transaction.get(it).toObject(FollowDTO::class.java) }
                    if (followDTO == null) //empty
                    {
                        followDTO = FollowDTO()
                        followDTO!!.followerCount = 2
                        followDTO!!.followers[auth?.uid.toString()!!] = auth?.uid.toString()
                        followDTO!!.followers[uid!!]=uid
                        followDTO!!.followings[uid!!]=uid

                        if (tsDocFollower != null) {
                            transaction.set(tsDocFollower, followDTO!!)
                        }
                        return@runTransaction
                    }

                    if (followDTO?.followers?.containsKey(auth?.uid.toString())!!) { //이미 팔로우하고 있을 경우 -> 언팔로우
                        followDTO?.followerCount = followDTO?.followerCount!! - 1
                        followDTO?.followers!!.remove(auth?.uid.toString())
                    } else //팔로우하기
                    {
                        followDTO?.followerCount = followDTO?.followerCount!! + 1
                        followDTO?.followers!![auth?.uid.toString()] = auth?.uid.toString()
                    }

                    if (tsDocFollower != null) {
                        transaction.set(tsDocFollower, followDTO!!)
                    }
                    return@runTransaction
                }
            }
            //내가 팔로우하고 있는가 아닌가에 따라 변경 -> 나의 following 에 없으면 버튼이름 "팔로우", 있으면 "팔로우 취소"
            //follower -> 나를, following -> 내가
            //팔로우시 내 following에 남 추가, 남의 follower에 나 추가
            //팔로우 취소시 내 following에서 남 삭제, 남의 follower에서 나 삭제


        }


        //탭레이아웃
        val fragmentManager = (activity as FragmentActivity).supportFragmentManager

        Toast.makeText(requireContext(), "페이지/클릭한 사람의 uid : $uid", Toast.LENGTH_SHORT).show()

        val pagerAdapter = FragmentAdapter(childFragmentManager, uid)
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
}