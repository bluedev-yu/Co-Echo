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
import bluedev_yu.coecho.data.model.ReviewDTO
import bluedev_yu.coecho.data.model.userDTO
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.*
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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_review)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        cancleWriting = findViewById(R.id.tv_cancleWriting)
        cancleWriting.setOnClickListener {
            onBackPressed()
        }

        etHashtag = findViewById(R.id.rv_hashtag)
        etText = findViewById(R.id.rv_text)
        ratingBar = findViewById(R.id.ratingBar)

        tvUpload = findViewById(R.id.rv_uploadFeed)
        tvUpload.setOnClickListener {
            //해시태그, 글, 공개범위 등록
            var ReviewDTO = ReviewDTO()
            ReviewDTO.hashtag = etHashtag.text.toString() //해시태그 문자열
            ReviewDTO.content = etText.text.toString() //글 문자열
            ReviewDTO.uid = auth?.uid
            ReviewDTO.star = ratingBar.rating
            ReviewDTO.timestamp = System.currentTimeMillis()
            ReviewDTO.pid=intent.getStringExtra("pid")


            firestore?.collection("User")?.document(auth?.uid.toString())?.get()?.addOnSuccessListener{
                    task ->
                var document = task?.toObject(userDTO::class.java)
                ReviewDTO.strName = document?.strName
                ReviewDTO.title = document?.title

                firestore?.collection("Reviews")?.document()?.set(ReviewDTO)
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


}