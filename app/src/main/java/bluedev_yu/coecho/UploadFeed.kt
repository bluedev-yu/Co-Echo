package bluedev_yu.coecho

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*

class UploadFeed : AppCompatActivity() {

    lateinit var cancleWriting: TextView
    lateinit var tvPhoto: TextView
    lateinit var etHashtag: EditText
    lateinit var etText: EditText
    lateinit var tvUpload: TextView
    lateinit var privacy: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_feed)

        cancleWriting = findViewById(R.id.tv_cancleWriting)
        cancleWriting.setOnClickListener {
            onBackPressed()
        }

        tvPhoto = findViewById(R.id.tv_photo)
        tvPhoto.setOnClickListener {
            //혜주야 부탁해
            //갤러리에서 가져오는 그거야 ..
        }

        etHashtag = findViewById(R.id.et_hashtag)
        etText = findViewById(R.id.et_text)

        tvUpload = findViewById(R.id.tv_upload)
        tvUpload.setOnClickListener {
            //해시태그, 글, 공개범위 등록
            var hashtag = etHashtag.text //해시태그 문자열
            var text = etText.text //글 문자열

            if(privacy.equals("나만보기")){

            }else if(privacy.equals("전체공개")){

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