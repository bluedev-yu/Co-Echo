package bluedev_yu.coecho

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.*

import androidx.core.content.ContextCompat
import bluedev_yu.coecho.data.model.Feeds
import bluedev_yu.coecho.data.model.userDTO
import bluedev_yu.coecho.databinding.UploadFeedBinding
import bluedev_yu.coecho.fragment.FragmentMyPage
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await
import org.koin.androidx.scope.lifecycleScope
import java.lang.Exception


class UploadFeed : AppCompatActivity() {

    lateinit var cancleWriting: TextView
    lateinit var tvPhoto: TextView
    lateinit var etHashtag: EditText
    lateinit var etText: EditText
    lateinit var btnUpload: Button
    var privacy: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_feed)

        cancleWriting = findViewById(R.id.tv_cancleWriting)
        cancleWriting.setOnClickListener {
            onBackPressed()
        }

        tvPhoto = findViewById(R.id.tv_photo)
        tvPhoto.setOnClickListener {
            
        //프로필이미지 바꾸기
        val FeedPhotoUploadButton : Button = binding.FeedPhotoUploadButton
        FeedPhotoUploadButton.setOnClickListener{
            var photoPickerIntent = Intent(Intent.ACTION_PICK)
            photoPickerIntent.type = "image/*"
            startActivityForResult(photoPickerIntent,pickImageFromAlbum)
        }

        etHashtag = findViewById(R.id.et_hashtag)
        etText = findViewById(R.id.et_text)

        btnUpload = findViewById(R.id.btn_upload)
        btnUpload.setOnClickListener {
            //해시태그, 글, 공개범위 등록

            var FeedDTO = Feeds()
            FeedDTO.hashtag = etHashtag.text.toString() //해시태그 문자열
            FeedDTO.content = etText.text.toString() //글 문자열

            if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                CoroutineScope(Dispatchers.IO).launch {
                    var uriString : String?
                    runBlocking {
                        uriString = funImageUpLoad(view!!)
                    }
                    FeedDTO.feedImgUrl = uriString
                }
            }

            FeedDTO.privacy = privacy
            FeedDTO.uid = auth?.uid
            FeedDTO.likes = HashMap()

            firestore?.collection("Feeds")?.document()?.set(FeedDTO)?.addOnCompleteListener {
                task ->
                if(task.isSuccessful)
                {
                    Toast.makeText(this,"게시 완료",Toast.LENGTH_SHORT).show()
                    val nextIntent = Intent(this,MainActivity::class.java)
                    startActivity(nextIntent)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == pickImageFromAlbum){
            if(resultCode == Activity.RESULT_OK){
                uriPhoto = data?.data
                val ImagePreview : ImageView = binding.ImagePreview
                ImagePreview.setImageURI(uriPhoto)
                ImagePreview.visibility = View.VISIBLE
            }
        }
    }

    suspend fun funImageUpLoad(view : View) : String?{
        //var timestamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        var user = auth?.currentUser?.uid.toString()
        var imgFileName = "Feed"+user+".png"
        var storageRef = firestorage?.reference?.child("Feed")?.child(imgFileName)

        return withContext(Dispatchers.IO) {
            storageRef?.putFile(uriPhoto!!)?.await()
                ?.storage?.downloadUrl?.await().toString()
        }
    }

    fun onRadioButtonClicked(view: View){
        if(view is RadioButton){
            val checked = view.isChecked

            when(view.getId()){
                R.id.rb_private ->
                    if(checked){
                        //나만보기로 올리기
                        privacy = true
                    }
                R.id.rb_public ->
                    if(checked){
                        //전체공개로 올리기
                        privacy = false
                    }
            }
        }
    }
}