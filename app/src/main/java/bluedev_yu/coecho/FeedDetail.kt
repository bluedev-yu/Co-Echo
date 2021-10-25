package bluedev_yu.coecho

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import bluedev_yu.coecho.adapter.CommentAdapter
import bluedev_yu.coecho.data.model.Comments

class FeedDetail : AppCompatActivity() {
    lateinit var arrow: ImageView
    lateinit var rv_comments: RecyclerView
    lateinit var tv_uploadComment: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed_detail)

        //뒤로 가기 버튼
        arrow = findViewById(R.id.iv_arrow)
        arrow.setOnClickListener {
            onBackPressed()
        }

        //클릭된 피드의 정보로 초기화
        val uid = intent.getSerializableExtra("uid")
        val content = intent.getSerializableExtra("content")
        val hashtag = intent.getSerializableExtra("hashtag")
        val likeCnt = intent.getSerializableExtra("likeCnt")

        var tv_name = findViewById<TextView>(R.id.feed_name)
        var tv_content = findViewById<TextView>(R.id.feed_content)
        var tv_hashtag = findViewById<TextView>(R.id.feed_hashtag)
        var tv_like_cnt = findViewById<TextView>(R.id.feed_like_cnt)

        tv_name.setText(uid.toString())
        tv_content.setText(content.toString())
        tv_hashtag.setText(hashtag.toString())
        tv_like_cnt.setText(likeCnt.toString())

        //게시 버튼
        tv_uploadComment = findViewById(R.id.feed_comment_upload)
        tv_uploadComment.setOnClickListener {
            //댓글 게시 눌렀을 때 데이터 올리기
        }

        var commentList = arrayListOf(
            Comments(null, "차은우", "안녕하세요"),
            Comments(null, "윤혜영", "댓글 달고가요~"),
            Comments(null, "길혜주", "총총ㅎㅎ")
        )

        rv_comments = findViewById(R.id.rv_comments)

        rv_comments.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rv_comments.setHasFixedSize(true)
        rv_comments.adapter = CommentAdapter(commentList)

    }
}