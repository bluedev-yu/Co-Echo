package bluedev_yu.coecho

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.core.content.ContextCompat
import bluedev_yu.coecho.data.model.userDTO
import bluedev_yu.coecho.databinding.UploadFeedBinding
import bluedev_yu.coecho.fragment.FragmentMyPage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage


class UploadFeed : AppCompatActivity() {

    lateinit var cancleWriting: TextView
    lateinit var tvPhoto: TextView
    lateinit var etHashtag: EditText
    lateinit var etText: EditText
    lateinit var btnUpload: Button
    lateinit var privacy: String

    private var view  : View? = null
    var auth : FirebaseAuth? = null
    var firestore : FirebaseFirestore?= null //String 등 자료형 데이터베이스
    var firestorage : FirebaseStorage?= null //사진, GIF 등의 파일 데이터베이스
    private lateinit var binding : UploadFeedBinding

    var pickImageFromAlbum =0
    var uriPhoto : Uri?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = UploadFeedBinding.inflate(layoutInflater)
        view = binding.root
        setContentView(view)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        firestorage = FirebaseStorage.getInstance()

        cancleWriting = findViewById(R.id.tv_cancleWriting)
        cancleWriting.setOnClickListener {
            onBackPressed()
        }

        tvPhoto = findViewById(R.id.tv_photo)
            //혜주야 부탁해
            //갤러리에서 가져오는 그거야 ..

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
            var hashtag = etHashtag.text //해시태그 문자열
            var text = etText.text //글 문자열

            if(privacy.equals("나만보기")){

            }else if(privacy.equals("전체공개")){

            }

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == pickImageFromAlbum){
            if(resultCode == Activity.RESULT_OK){
                Toast.makeText(this,"들어오므",Toast.LENGTH_LONG).show()
                uriPhoto = data?.data
                val ImagePreview : ImageView = binding.ImagePreview
                ImagePreview.setImageURI(uriPhoto)

//                if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
//
//                    funImageUpLoad(view!!)
//                }
            }
        }
    }

    fun funImageUpLoad(view : View){
        //var timestamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        var user = auth?.currentUser?.uid.toString()
        var imgFileName = "Feed"+user+".png"
        var storageRef = firestorage?.reference?.child("Feed")?.child(imgFileName)

        storageRef?.putFile(uriPhoto!!)?.addOnSuccessListener {
            storageRef.downloadUrl.addOnSuccessListener{
                    uri->
                var userInfo = userDTO()
                userInfo.imageUrl = uri.toString()
                firestore?.collection("User")?.document(auth?.uid.toString())?.update("imageUrl",userInfo.imageUrl)
            }
        }

    }

    fun onRadioButtonClicked(view: View){
        if(view is RadioButton){
            val checked = view.isChecked

            when(view.getId()){
                R.id.rb_private ->
                    if(checked){
                        //나만보기로 올리기
                        privacy = "나만보기"
                    }
                R.id.rb_public ->
                    if(checked){
                        //전체공개로 올리기
                        privacy = "전체공개"
                    }
            }
        }
    }
}