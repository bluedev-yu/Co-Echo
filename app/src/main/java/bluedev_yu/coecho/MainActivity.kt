package bluedev_yu.coecho

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import bluedev_yu.coecho.model.ContentDTO
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import android.view.MenuItem
import androidx.fragment.app.Fragment
import bluedev_yu.coecho.fragment.FragmentMap
import bluedev_yu.coecho.fragment.FragmentMyPage
import bluedev_yu.coecho.fragment.FragmentSNS
import bluedev_yu.coecho.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationBarView


class MainActivity : AppCompatActivity(), NavigationBarView.OnItemSelectedListener{
    private lateinit var binding: ActivityMainBinding

    var firestore : FirebaseFirestore?= null //String 등 자료형 데이터베이스
    var firestorage : FirebaseStorage?= null //사진, GIF 등의 파일 데이터베이스


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        
        firestore = FirebaseFirestore.getInstance() //객체 할당
        firestorage = FirebaseStorage.getInstance()

        binding.bottomNavBar.setOnItemSelectedListener(this)

        setDefaultFragment()

        binding.bottomNavBar.setItemIconTintList(null);
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean { //내비게이션바 아이템 선택시 프래그먼트 교체
        when (item.itemId) {
            R.id.action_sns -> {
                loadFragment(FragmentSNS())
                return true
            }
            R.id.action_map -> {
                loadFragment(FragmentMap())
                return true
            }
            R.id.action_myPage -> {
                loadFragment(FragmentMyPage())
                return true
            }
        }
        return false
    }

    private fun setDefaultFragment(){ //앱 실행시 디폴트 프래그먼트 설정
        loadFragment(FragmentSNS())
    }

    private fun loadFragment(fragment: Fragment) { //프래그먼트 로드
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frameLayout, fragment)
        transaction.disallowAddToBackStack()
        transaction.commit()


        //피드 업로드 fun
        fun contentUpload() {
            //uid, userid는 임시로 생성
            //explain, imageurl, uid, userid, timestamp, favoriteCount=0, favorites, hashTag

            var contentDTO = ContentDTO()

            //줄글
            contentDTO.explain = "hi"
            //이미지 url
            contentDTO.imageurl = null
            //uid
            contentDTO.uid = "1"
            //userid(닉네임)
            contentDTO.userId = "임시"
            //timestamp
            contentDTO.timestamp = System.currentTimeMillis()

            //Content.user.contentid
            firestore?.collection("Content")?.document(contentDTO.uid!!)?.set(contentDTO) //db에 넣기
        }


        //피드 가져오기 fun
        //fun getContent(collectionname : String, documentname : String) : ContentDTO){


        fun commentUpload(Content: ContentDTO) { //댓글 업로드 fun, 인자는 댓글 쓸 피드

            var comment = ContentDTO.Comment()

            comment.uid = "1" //작성자 uid
            comment.userId = "임시" //작성자 userid
            comment.timestamp = System.currentTimeMillis()
            comment.comment = "댓글 내용"

            //Comment.contentid.Commentid
            firestore?.collection("Comment")?.document(Content.contentId!!)?.set(comment)
        }

    }

}

