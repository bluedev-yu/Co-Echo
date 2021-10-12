package bluedev_yu.coecho.fragment

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.fragment.app.FragmentActivity
import androidx.viewpager.widget.ViewPager
import bluedev_yu.coecho.adapter.FragmentAdapter

import bluedev_yu.coecho.R
import bluedev_yu.coecho.data.model.userDTO
import bluedev_yu.coecho.databinding.FragmentMyPageBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
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
class FragmentMyPage : Fragment(), NavigationView.OnNavigationItemSelectedListener {
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding: FragmentMyPageBinding
    private val _binding get() = binding!!

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

        binding = FragmentMyPageBinding.inflate(inflater, container, false)

        // 설정 버튼 선택시 오른쪽으로 열린다
        binding.MyPageOptionButton.setOnClickListener{
            binding.MyPageDrawerLayout.openDrawer(GravityCompat.END)
        }

        // 네비게이션 드로워 아이템 클릭 속성 부여
        binding.navigationView.setNavigationItemSelectedListener(this)

        val view = binding.root
        return view


        // Inflate the layout for this fragment
        viewProfile =  inflater.inflate(R.layout.fragment_my_page, container, false)

        //uid 받아오기 - uid가 null이 아니면 다른 사람 페이지
        //MYPAGE, 나의에코, 톱니바퀴 버튼, 설정 총 4개 안보이도록 만들어야함
        val uid = arguments?.getString("uid")
        Toast.makeText(requireContext(), uid.toString(), Toast.LENGTH_SHORT).show()

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        firestorage = FirebaseStorage.getInstance()

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
        /*
        val LogoutButton : Button = viewProfile!!.findViewById(R.id.LogOutButton)
        LogoutButton.setOnClickListener{
            auth!!.signOut()
            Toast.makeText(this.context,"로그아웃 되었습니다.",Toast.LENGTH_LONG)
            val intent = Intent(this.context, LoginActivity::class.java)
            startActivity(intent)
        }*/

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

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId)
        {
            R.id.itme1 -> Toast.makeText(context, "몰라", Toast.LENGTH_SHORT).show()
            R.id.itme2 -> Toast.makeText(context, "몰라2", Toast.LENGTH_SHORT).show()
            R.id.itme3 -> Toast.makeText(context, "몰라3", Toast.LENGTH_SHORT).show()

        }
        binding.MyPageDrawerLayout.closeDrawers()
        return false
    }
}