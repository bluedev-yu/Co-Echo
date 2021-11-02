package bluedev_yu.coecho.fragment

import android.Manifest
import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.transition.Transition
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentActivity
import androidx.viewpager.widget.ViewPager
import bluedev_yu.coecho.LoginActivity
import bluedev_yu.coecho.adapter.FragmentAdapter

import bluedev_yu.coecho.R
import bluedev_yu.coecho.data.model.FollowDTO
import bluedev_yu.coecho.data.model.userDTO
import bluedev_yu.coecho.fragment.*
import bluedev_yu.coecho.MyPageBackground
import bluedev_yu.coecho.onBack
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.Target
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.fragment_my_page.*
import kotlinx.android.synthetic.main.fragment_my_page.view.*

class FragmentMyPage : Fragment(), NavigationView.OnNavigationItemSelectedListener, onBack {
    private var _binding: FragmentMyPage? = null
    private val binding get() = _binding!!

    var auth: FirebaseAuth? = null
    var firestore: FirebaseFirestore? = null //String 등 자료형 데이터베이스
    var firestorage: FirebaseStorage? = null //사진, GIF 등의 파일 데이터베이스

    private var viewProfile: View? = null
    var pickImageFromAlbum = 0
    var uriPhoto: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewProfile = inflater.inflate(R.layout.fragment_my_page, container, false)

        //uid 받아오기 - uid가 null이 아니면 다른 사람 페이지
        //MYPAGE, 나의에코, 톱니바퀴 버튼, 설정 총 4개 안보이도록 만들어야함
        val uid = arguments?.getString("uid")

        val MypageFollower: TextView = viewProfile!!.findViewById(R.id.MyPageFollower) //팔로워 수
        val MyPageFollowing: TextView = viewProfile!!.findViewById(R.id.MyPageFollowing) //팔로잉수


        val MyPageOptionButton: ImageView = viewProfile!!.findViewById(R.id.MyPageOptionButton)
        val MyPageDrawerLayout = viewProfile!!.findViewById<DrawerLayout>(R.id.MyPageDrawerLayout)
        MyPageOptionButton.setOnClickListener {
            MyPageDrawerLayout
                .openDrawer(GravityCompat.END)
        }
        // 네비게이션 드로워 아이템 클릭 속성 부여
        var MypageNavigationView = viewProfile!!.findViewById<NavigationView>(R.id.MypageNavigationView)
        MypageNavigationView.setNavigationItemSelectedListener(this)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        firestorage = FirebaseStorage.getInstance()

        if (uid == null || uid == auth?.uid.toString()) //마이페이지
        {
            firestore?.collection("User")?.document(auth?.uid.toString())?.addSnapshotListener{
                    documentSnapshot, firebaseFirestoreException ->
                var document = documentSnapshot?.toObject(userDTO::class.java)
                val ProfileImage : ImageView = viewProfile!!.findViewById(R.id.MypageProfileImage)
                val mypageBackground : FrameLayout = viewProfile!!.findViewById(R.id.mypageBackground)


                //배경화면
                if(document?.mypageBackground != null)
                {
                    activity?.let {
                        Glide.with(it)
                            .load(document.mypageBackground)
                            .into(object : CustomTarget<Drawable>(){
                                override fun onLoadCleared(placeholder: Drawable?) {
                                }

                                override fun onResourceReady(
                                    resource: Drawable,
                                    transition: com.bumptech.glide.request.transition.Transition<in Drawable>?
                                ) {
                                    val layout = mypageBackground
                                    layout.background = resource
                                }

                            })
                    }
                }

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
                    val MypageUsername: TextView = viewProfile!!.findViewById(R.id.MyPageUserName)
                    MypageUsername.text = document?.strName

                    //프로필사진
                    if (document?.imageUrl == null)
                        ProfileImage.setImageResource(R.drawable.default_profilephoto)
                    else {
                        activity?.let {
                            Glide.with(it)
                                .load(document.imageUrl)
                                .apply(RequestOptions().circleCrop())
                                .into(viewProfile!!.findViewById(R.id.MypageProfileImage))
                        }
                    }
                }

            //프로필이미지 바꾸기
            val MypageProfileOptionButton: Button =
                viewProfile!!.findViewById(R.id.MyPageProfileOptionButton)
            MypageProfileOptionButton.setOnClickListener {
                val bottomSheetDialog = BottomSheetDialog(
                    requireContext(), R.style.BottomSheetDialogTheme
                )

                val bottomSheetView = LayoutInflater.from(requireContext()).inflate(
                    R.layout.layout_bottom_sheet,
                    viewProfile!!.findViewById(R.id.bottomSheet) as LinearLayout?
                )

                bottomSheetDialog.setContentView(bottomSheetView)
                bottomSheetDialog.show()

                val changeProfile: TextView =
                    bottomSheetView.findViewById(R.id.mypage_change_profile)
                changeProfile.setOnClickListener {
//                    Toast.makeText(requireContext(), "프로필 바꾸기 선택됨", Toast.LENGTH_SHORT).show()
                    var photoPickerIntent = Intent(Intent.ACTION_PICK)
                    photoPickerIntent.type = "image/*"
                    startActivityForResult(photoPickerIntent, pickImageFromAlbum)
                }
                val changeBackGround: TextView =
                    bottomSheetView.findViewById(R.id.mypage_change_background)
                changeBackGround.setOnClickListener {
                    //fragment mypage -> activity my page background
                    val intent = Intent(this.context, MyPageBackground::class.java)
                    startActivity(intent)
                }
            }



            //로그아웃
            val LogoutButton: Button = viewProfile!!.findViewById(R.id.FollowButton)
            LogoutButton.setOnClickListener {
                auth!!.signOut()
                Toast.makeText(this.context, "로그아웃 되었습니다.", Toast.LENGTH_LONG).show()
                val intent = Intent(this.context, LoginActivity::class.java)
                startActivity(intent)
            }

            firestore?.collection("Follow")?.document(auth?.uid.toString())
                ?.addSnapshotListener { //팔로워 팔로잉 수
                        documentSnapshot, firebaseFirestoreException ->
                    var followDTO = documentSnapshot?.toObject(FollowDTO::class.java)
                    MypageFollower.text =
                        (followDTO?.followerCount!! - 1).toString() + " 팔로워" //한명은 자기 자신
                    MyPageFollowing.text = (followDTO.followingCount - 1).toString() + " 팔로잉"
                }

        }

        else //남의 페이지
        {
            //각종 버튼 숨기기, 보이기
            val mypageText: TextView = viewProfile!!.findViewById(R.id.MypageText) //MYPAGE TEXT
            mypageText.visibility = View.INVISIBLE
            val myEchoText: TextView = viewProfile!!.findViewById(R.id.MyEchoText) //나의에코 TEXT
            myEchoText.visibility = View.INVISIBLE
            MyPageOptionButton.visibility = View.INVISIBLE
            val MypageProfileOptionButton: Button =
                viewProfile!!.findViewById(R.id.MyPageProfileOptionButton) //마이페이지 배경변경 버튼
            MypageProfileOptionButton.visibility = View.INVISIBLE
            val followButton: Button =
                viewProfile!!.findViewById(R.id.FollowButton) //팔로우 버튼 팔로우/언팔로우로 보이기

            firestore?.collection("Follow")?.document(auth?.uid.toString())
                ?.addSnapshotListener { documentSnapshot, firebaseFirestoreException ->
                    var followDTO = documentSnapshot?.toObject(FollowDTO::class.java)
                    if (followDTO?.followings!!.containsKey(uid.toString())) //follow하고 있을 경우
                    {
                        followButton.text = "팔로우 취소"
                    } else {
                        followButton.text = "팔로우"
                    }
                }

            firestore?.collection("Follow")?.document(uid)?.addSnapshotListener { //팔로워 팔로잉 수
                    documentSnapshot, firebaseFirestoreException ->
                var followDTO = documentSnapshot?.toObject(FollowDTO::class.java)
                MypageFollower.text =
                    (followDTO?.followerCount!! - 1).toString() + " 팔로워" //한명은 자기 자신
                MyPageFollowing.text = (followDTO.followingCount - 1).toString() + " 팔로잉"
            }

            //유저정보 가져오기
            firestore?.collection("User")?.document(uid)
                ?.addSnapshotListener { documentSnapshot, firebaseFirestoreException ->
                    var document = documentSnapshot?.toObject(userDTO::class.java)
                    val ProfileImage: ImageView =
                        viewProfile!!.findViewById(R.id.MypageProfileImage)

                    //칭호
                    val MypageTitle: TextView = viewProfile!!.findViewById(R.id.MyPageTitle)
                    if (document?.title == 0) {
                        MypageTitle.setText(R.string.grade1)
                    } else {
                        MypageTitle.setText(R.string.grade2)
                    }

                    //사람이름
                    val MypageUsername: TextView = viewProfile!!.findViewById(R.id.MyPageUserName)
                    MypageUsername.text = document?.strName

                    //프로필사진
                    if (document?.imageUrl == null)
                        ProfileImage.setImageResource(R.drawable.default_profilephoto)
                    else {
                        activity?.let {
                            Glide.with(it)
                                .load(document.imageUrl)
                                .apply(RequestOptions().circleCrop())
                                .into(viewProfile!!.findViewById(R.id.MypageProfileImage))
                        }
                    }
                }

            followButton.setOnClickListener {
                var tsDocFollowing = firestore?.collection("Follow")?.document(auth?.uid.toString())
                firestore?.runTransaction { //내 팔로우 데이터 가져오기
                        transaction ->
                    var followDTO =
                        transaction.get(tsDocFollowing!!).toObject(FollowDTO::class.java)
                    if (followDTO == null) //empty
                    {
                        followDTO = FollowDTO()
                        followDTO.followingCount = 2
                        followDTO.followings[uid] = uid
                        followDTO.followings[auth?.uid.toString()] = auth?.uid.toString()
                        followDTO.followers[auth?.uid.toString()] = auth?.uid.toString()

                        if (tsDocFollowing != null) {
                            transaction.set(tsDocFollowing, followDTO)
                        }
                        return@runTransaction
                    }

                    if (followDTO.followings.containsKey(uid)) { //이미 팔로우하고 있을 경우 -> 언팔로우
                        followDTO.followingCount = followDTO.followingCount - 1
                        followDTO.followings.remove(uid)
                    } else //팔로우하기
                    {
                        followDTO.followingCount = followDTO.followingCount + 1
                        followDTO.followings[uid] = uid
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
                        followDTO!!.followers[auth?.uid.toString()] = auth?.uid.toString()
                        followDTO!!.followers[uid] = uid
                        followDTO!!.followings[uid] = uid

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

        val pagerAdapter = FragmentAdapter(childFragmentManager, uid)
        val pager = viewProfile!!.findViewById<ViewPager>(R.id.viewPager)
        pager.adapter = pagerAdapter
        val tab = viewProfile!!.findViewById<TabLayout>(R.id.MyPageTabs)
        tab.setupWithViewPager(pager)


        return viewProfile
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == pickImageFromAlbum) {
            if (resultCode == Activity.RESULT_OK) {
                uriPhoto = data?.data
                val MypageProfileImage: ImageView =
                    viewProfile!!.findViewById(R.id.MypageProfileImage)
                MypageProfileImage.setImageURI(uriPhoto)

                if (ContextCompat.checkSelfPermission(
                        viewProfile!!.context,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    funImageUpLoad(viewProfile!!)
                }
            }
        }
    }

    private fun funImageUpLoad(view: View) {
        //var timestamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        var user = auth?.currentUser?.uid.toString()
        var imgFileName = "PROFILE" + user + ".png"
        var storageRef = firestorage?.reference?.child("ProfileImages")?.child(imgFileName)

        storageRef?.putFile(uriPhoto!!)?.addOnSuccessListener {
            storageRef.downloadUrl.addOnSuccessListener { uri ->
                var userInfo = userDTO()
                userInfo.imageUrl = uri.toString()
                firestore?.collection("User")?.document(auth?.uid.toString())
                    ?.update("imageUrl", userInfo.imageUrl)
            }
        }

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId)
        {
            R.id.logout -> {
                logout()
            }
            R.id.DeleteAccount -> {
                deleteAccount()
            }
        }
        MyPageDrawerLayout.closeDrawers()
        return false
    }

    override fun onBackPressed() {
        if (binding.MyPageDrawerLayout.isDrawerOpen(GravityCompat.END)){
            binding.MyPageDrawerLayout.closeDrawers()
        }
        else{
            activity?.finish()
        }
    }

    private fun loadFrag(fragment: Fragment){
        val tra = childFragmentManager.beginTransaction()
        tra.replace(R.id.MyPageDrawerLayout, fragment)
        tra.disallowAddToBackStack()
        tra.commit()

    }

    fun logout()
    {
        auth!!.signOut()
        Toast.makeText(this.context, "로그아웃 되었습니다.", Toast.LENGTH_LONG).show()
        val intent = Intent(this.context, LoginActivity::class.java)
        startActivity(intent)
    }

    fun deleteAccountfun()
    {
        firestore?.collection("User")?.document(auth?.uid.toString())?.delete() //User 지우기
        firestore?.collection("Feeds")?.whereIn("uid", listOf(auth?.uid.toString()))
        auth?.currentUser!!.delete().addOnCompleteListener {
                task ->
            if(task.isSuccessful){
                Toast.makeText(this.context, "아이디 삭제가 완료되었습니다", Toast.LENGTH_LONG).show()

                //로그아웃처리
                FirebaseAuth.getInstance().signOut()
            }else{
                Toast.makeText(this.context, task.exception.toString(), Toast.LENGTH_LONG).show()

            }
        }
    }

    fun deleteAccount()
    {
        val intent = Intent(this.context, LoginActivity::class.java)
        val dlg: AlertDialog.Builder = AlertDialog.Builder(this.requireContext(),  android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth)
        dlg.setTitle("계정탈퇴") //제목
        dlg.setMessage("정말 계정을 삭제하시겠습니까?") // 메시지
        dlg.setPositiveButton("확인", DialogInterface.OnClickListener { dialog, which ->
            //startActivity(intent)
            Toast.makeText(this.context,"추후 업데이트 예정입니다.",Toast.LENGTH_LONG).show()
        })
        dlg.setNegativeButton("취소",DialogInterface.OnClickListener { dialog, which ->
        })
        var alertDialog = dlg.create()
        val window = alertDialog.window
        window?.setGravity(Gravity.CENTER)
        alertDialog.show()
    }
}