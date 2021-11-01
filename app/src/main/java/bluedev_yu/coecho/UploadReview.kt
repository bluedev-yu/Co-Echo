package bluedev_yu.coecho

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.icu.text.SimpleDateFormat
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import bluedev_yu.coecho.data.model.Feeds
import bluedev_yu.coecho.data.model.FollowDTO
import bluedev_yu.coecho.data.model.ReviewDTO
import bluedev_yu.coecho.data.model.userDTO
import bluedev_yu.coecho.fragment.FragmentMap
import bluedev_yu.coecho.fragment.FragmentMyPage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await
import net.daum.android.map.MapActivity
import java.lang.Exception
import java.util.*
import kotlin.collections.HashMap


class UploadReview : AppCompatActivity() {

    lateinit var cancleWriting: TextView
    lateinit var etHashtag: EditText
    lateinit var etText: EditText
    lateinit var tvUpload: TextView
    lateinit var ratingBar : RatingBar

    private var view  : View? = null
    var auth : FirebaseAuth? = null
    var firestore : FirebaseFirestore?= null //String 등 자료형 데이터베이스
    var firestorage : FirebaseStorage?= null //사진, GIF 등의 파일 데이터베이스

    var pickImageFromAlbum =0
    var uriPhoto : Uri?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_review)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        firestorage = FirebaseStorage.getInstance()

        cancleWriting = findViewById(R.id.tv_cancleWriting)
        cancleWriting.setOnClickListener {
            onBackPressed()
        }

        //리뷰 사진 업로드
        val ReviewPhotoUploadButton : TextView = findViewById(R.id.ReviewImageUploadButton)
        ReviewPhotoUploadButton.setOnClickListener{
            var photoPickerIntent = Intent(Intent.ACTION_PICK)
            photoPickerIntent.type = "image/*"
            startActivityForResult(photoPickerIntent,pickImageFromAlbum)
        }

        etHashtag = findViewById(R.id.rv_hashtag)
        etText = findViewById(R.id.rv_text)
        ratingBar = findViewById(R.id.ratingBar)

        tvUpload = findViewById(R.id.rv_uploadReview)
        tvUpload.setOnClickListener {
            //해시태그, 글, 공개범위 등록
            var ReviewDTO = ReviewDTO()
            var tempHash= etHashtag.text.toString()
            if(!tempHash.startsWith("#")){
                Log.i("구간 첵","a")
                ReviewDTO.hashtag="#"+tempHash //해시태그 문자열
                }
            else{
                Log.i("구간 첵","b")
                ReviewDTO.hashtag=tempHash
            }
            ReviewDTO.content = etText.text.toString() //글 문자열
            ReviewDTO.uid = auth?.uid
            ReviewDTO.star = ratingBar.rating
            ReviewDTO.timestamp = System.currentTimeMillis()
            ReviewDTO.pid = intent.getStringExtra("pid")

            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            )
                CoroutineScope(Dispatchers.Main).launch {
                    Log.v("ReviewImage",ReviewDTO.reviewImage.toString())
                    if(ReviewDTO.reviewImage.equals(null)){
                        //리뷰 이미지가 없을 경우
                        val ImagePreview: ImageView = findViewById(R.id.imagePreviewInReview)
                        ImagePreview.visibility = View.GONE
                    }else{
                        //리뷰 이미지가 있을 경우
                        ReviewDTO.reviewImage = funImageUpLoad()
                    }
                    //null check하기
                    if(ReviewDTO.star!!<0.5)
                    {
                        makeToast(true,"별점을 입력해 주세요!")
                    }
                    else if(ReviewDTO.content?.equals("") == true) //내용없음
                    {
                        makeToast(true,"내용을 입력해 주세요!")
                    }
                    else if(ReviewDTO.hashtag?.equals("")== true) //해시태그없음
                    {
                        makeToast(true, "해시태그를 입력해 주세요!")
                    }
//                    else if(ReviewDTO.reviewImage.equals(null))
//                    {
//                        makeToast(true,"사진을 업로드 해주세요!")
//                    }
                    else //다 만족
                    {
                        makeToast(true,"사진을 업로드하고 있습니다...")
                    firestore?.collection("User")?.document(auth?.uid.toString())?.get()
                        ?.addOnSuccessListener { task ->
                            var document = task?.toObject(userDTO::class.java)
                            ReviewDTO.strName = document?.strName
                            ReviewDTO.title = document?.title
                            ReviewDTO.imageUrl = document?.imageUrl

                            firestore?.collection("Reviews")?.document()?.set(ReviewDTO)
                                ?.addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        makeToast(task.isSuccessful,"게시 완료!")
                                        makeToast(task.isSuccessful,"환경을 위해 " + (ReviewDTO.title!! + 1) + "만큼 노력하셨네요!")

                                        val map = FragmentMap()
                                        var fm: FragmentManager = supportFragmentManager
                                        var ft: FragmentTransaction = fm.beginTransaction()
                                        ft.replace(R.id.uploadReview, map).commit()
                                    }
                                }
                            firestore?.collection("User")?.document(auth?.uid.toString())
                                ?.update("title", ReviewDTO.title!! + 1)
                        }

                }}
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == pickImageFromAlbum) {
            if (resultCode == Activity.RESULT_OK) {
                uriPhoto = data?.data
                val ImagePreview: ImageView = findViewById(R.id.imagePreviewInReview)
                ImagePreview.setImageURI(uriPhoto)
                ImagePreview.visibility = View.VISIBLE
            }
        }

    }

    suspend fun funImageUpLoad(): String? {
        var timestamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        var user = auth?.currentUser?.uid.toString()
        var imgFileName = "Review" + user + timestamp + ".png"
        var storageRef = firestorage?.reference?.child("Review")?.child(imgFileName)

        try {
            makeToast(true,"업로드중입니다. 잠시 기다려주세요")
            storageRef?.putFile(uriPhoto!!)?.await()
            val url = storageRef?.downloadUrl?.await().toString()
            return url
        } catch (e: Exception) {
            Log.e("error:", "error:" + e.message.toString())
            return null
        }
    }

    fun makeToast(success: Boolean,text : String){
        if (success){
            Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
        }
    }

}