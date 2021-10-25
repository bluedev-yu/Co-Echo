package bluedev_yu.coecho

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import bluedev_yu.coecho.adapter.CommentAdapter
import bluedev_yu.coecho.data.model.Feeds
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ListenerRegistration

class FeedDetail : AppCompatActivity() {
    lateinit var arrow: ImageView
    lateinit var rv_comments: RecyclerView
    lateinit var tv_uploadComment: TextView

    var contentUid : String? = null
    var user : FirebaseUser ? = null
    var destinationUid : String? = null
    var commentSnapshot : ListenerRegistration? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed_detail)

        user = FirebaseAuth.getInstance().currentUser
        destinationUid = intent.getStringExtra("destinationUid")
        contentUid = intent.getStringExtra("contentUid")


        //뒤로 가기 버튼
        arrow = findViewById(R.id.iv_arrow)
        arrow.setOnClickListener {
            onBackPressed()
        }

        /*
        * 수정사항
        * 1. timestamp 출력
        * 2. 좋아요 여부도 같이 가져와야함
        * 3. comment
        * 4. title도 가져와야 함
        * 5. 해시태그 앞에 # 달건가?
        * 6. content profile image도 가져와야 함
        * 7. 좋아요 버튼이랑 다시 구현해야 함.
        * */


        //클릭된 피드의 정보로 초기화
        val imageUrl = intent.getSerializableExtra("imageUrl")
        val title = intent.getSerializableExtra("title")
        val strName = intent.getSerializableExtra("strName")
        val timeStamp = intent.getSerializableExtra("timeStamp")
        val content = intent.getSerializableExtra("content")
        val hashtag = intent.getSerializableExtra("hashtag")
        val likeCnt = intent.getSerializableExtra("likeCnt")

        var tv_profileimage = findViewById<ImageView>(R.id.feed_profile)
        var tv_title = findViewById<TextView>(R.id.feed_title)
        var tv_name = findViewById<TextView>(R.id.feed_name)
        var tv_content = findViewById<TextView>(R.id.feed_content)
        var tv_hashtag = findViewById<TextView>(R.id.feed_hashtag)
        var tv_like_cnt = findViewById<TextView>(R.id.feed_like_cnt)
        var feed_timeStamp = findViewById<TextView>(R.id.feed_timestamp)

        //프로필사진
        if(imageUrl == null) //기본 이미지
            Glide.with(this).load(R.drawable.default_profilephoto).apply(
                RequestOptions().circleCrop()).into(tv_profileimage) //유저 프로필 이미지
        else
        {
            Glide.with(this).load(imageUrl).apply(
                RequestOptions().circleCrop()).into(tv_profileimage) //유저 프로필 이미지
        }
        tv_name.setText(strName.toString())
        tv_content.setText(content.toString())
        tv_hashtag.setText(hashtag.toString())
        tv_like_cnt.setText(likeCnt.toString())
        feed_timeStamp.setText(timeStamp.toString())
        when(title) //칭호
        {
            0 -> tv_title.setText(R.string.grade1)
            1 -> tv_title.setText(R.string.grade2)
        }

        //게시 버튼
        tv_uploadComment = findViewById(R.id.feed_comment_upload)
        tv_uploadComment.setOnClickListener {
            //댓글 게시 눌렀을 때 데이터 올리기

        }

        var commentList = arrayListOf<Feeds.Comment>()

        rv_comments = findViewById(R.id.rv_comments)

        rv_comments.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rv_comments.setHasFixedSize(true)
        rv_comments.adapter = CommentAdapter(commentList)

    }
}