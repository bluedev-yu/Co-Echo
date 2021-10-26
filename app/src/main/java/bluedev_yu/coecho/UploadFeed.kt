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
import bluedev_yu.coecho.data.model.Feeds
import bluedev_yu.coecho.data.model.FollowDTO
import bluedev_yu.coecho.data.model.userDTO
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.*
import java.util.*
import kotlin.collections.HashMap


class UploadFeed : AppCompatActivity() {

    lateinit var cancleWriting: TextView
    lateinit var tvPhoto: TextView
    lateinit var etHashtag: EditText
    lateinit var etText: EditText
    lateinit var tvUpload: TextView
    var privacy: Boolean = false

    private var view  : View? = null
    var auth : FirebaseAuth? = null
    var firestore : FirebaseFirestore?= null //String 등 자료형 데이터베이스
    var firestorage : FirebaseStorage?= null //사진, GIF 등의 파일 데이터베이스

    var pickImageFromAlbum =0
    var uriPhoto : Uri?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_feed)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        firestorage = FirebaseStorage.getInstance()

        cancleWriting = findViewById(R.id.tv_cancleWriting)
        cancleWriting.setOnClickListener {
            onBackPressed()
        }

//        tvPhoto = findViewById(R.id.tv_photo)

        //프로필이미지 바꾸기
        val FeedPhotoUploadButton : TextView = findViewById(R.id.tv_uploadFeed)
        FeedPhotoUploadButton.setOnClickListener{
            var photoPickerIntent = Intent(Intent.ACTION_PICK)
            photoPickerIntent.type = "image/*"
            startActivityForResult(photoPickerIntent,pickImageFromAlbum)
        }

        etHashtag = findViewById(R.id.et_hashtag)
        etText = findViewById(R.id.et_text)

        tvUpload = findViewById(R.id.tv_uploadFeed)
        tvUpload.setOnClickListener {
            //해시태그, 글, 공개범위 등록
            var FeedDTO = Feeds()
            FeedDTO.hashtag = etHashtag.text.toString() //해시태그 문자열
            FeedDTO.content = etText.text.toString() //글 문자열

            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            )
            //CoroutineScope(Dispatchers.IO).launch {
            //uriString = funImageUpLoad()
//                FeedDTO.feedImgUrl = null

            FeedDTO.privacy = privacy
            FeedDTO.uid = auth?.uid
            FeedDTO.likes = HashMap()
            FeedDTO.timeStamp = System.currentTimeMillis()

            //피드에서 나의 정보 가져오기
            firestore?.collection("User")?.document(auth?.uid.toString())?.get()?.addOnSuccessListener{
                    task ->
                var document = task?.toObject(userDTO::class.java)
                FeedDTO.imageUrl = document?.imageUrl
                FeedDTO.strName = document?.strName
                FeedDTO.title = document?.title
                FeedDTO.commentCnt = 0

                firestore?.collection("Feeds")?.document()?.set(FeedDTO)
                    ?.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "게시 완료", Toast.LENGTH_SHORT).show()
                            val nextIntent = Intent(this, MainActivity::class.java)
                            startActivity(nextIntent)
                        }
                    }
            }

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

//        if (requestCode == pickImageFromAlbum) {
//            if (resultCode == Activity.RESULT_OK) {
//                uriPhoto = data?.data
//                val ImagePreview: ImageView = findViewById(R.id.ImagePreview)
//                ImagePreview.setImageURI(uriPhoto)
//                ImagePreview.visibility = View.VISIBLE
//            }
//        }

//        suspend fun funImageUpLoad(): String? {
//            var timestamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
//            var user = auth?.currentUser?.uid.toString()
//            var imgFileName = "Feed" + user + timestamp + ".png"
//            var storageRef = firestorage?.reference?.child("Feed")?.child(imgFileName)
//            var FeedDTO: Feeds? = null
//
//            storageRef?.putFile(uriPhoto!!)?.addOnSuccessListener {
//                storageRef.downloadUrl.addOnSuccessListener { uri ->
//                    FeedDTO = Feeds()
//                    FeedDTO!!.feedImgUrl = uri.toString()
//                    firestore?.collection("Feeds")?.document(auth?.uid.toString())
//                        ?.update("feedImgUrl", FeedDTO)
//                }
//            }
//            Looper.prepare()
//            Toast.makeText(this, "피드", Toast.LENGTH_LONG)
//            delay(5000)
//            Toast.makeText(this, FeedDTO?.feedImgUrl.toString(), Toast.LENGTH_LONG)
//            return FeedDTO!!.feedImgUrl.toString()
//        }
    }

    fun onRadioButtonClicked(view: View) {
        fun onRadioButtonClicked(view: View) {
            if (view is RadioButton) {
                val checked = view.isChecked

                when (view.getId()) {
                    R.id.rb_private ->
                        if (checked) {
                            //나만보기로 올리기
                            privacy = true
                        }
                    R.id.rb_public ->
                        if (checked) {
                            //전체공개로 올리기
                            privacy = false
                        }
                }
            }
        }
    }
}