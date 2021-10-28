package bluedev_yu.coecho

import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import bluedev_yu.coecho.adapter.CommentAdapter
import bluedev_yu.coecho.data.model.Feeds
import bluedev_yu.coecho.data.model.userDTO
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.android.synthetic.main.activity_feed_detail.*
import org.w3c.dom.Text

class FeedDetail : AppCompatActivity() {
    lateinit var arrow: ImageView
    lateinit var rv_comments: RecyclerView
    lateinit var tv_uploadComment: TextView

    var contentUid : String? = null
    var user : FirebaseUser ? = null
    var destinationUid : String? = null
    var commentSnapshot : ListenerRegistration? = null

    var auth : FirebaseAuth? = null
    var firestore : FirebaseFirestore?= null //String 등 자료형 데이터베이스

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed_detail)

        user = FirebaseAuth.getInstance().currentUser

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        destinationUid = intent.getStringExtra("destinationUid")
        contentUid = intent.getStringExtra("contentUid")

        //뒤로 가기 버튼
        arrow = findViewById(R.id.iv_arrow)
        arrow.setOnClickListener {
            onBackPressed()
        }


        //클릭된 피드의 정보로 초기화
        val imageUrl = intent.getSerializableExtra("imageUrl")
        val feedImage = intent.getSerializableExtra("feedImage")
        val title = intent.getSerializableExtra("title")
        val strName = intent.getSerializableExtra("strName")
        val timeStamp = intent.getSerializableExtra("timeStamp")
        val content = intent.getSerializableExtra("content")
        val hashtag = intent.getSerializableExtra("hashtag")
        val likeCnt = intent.getSerializableExtra("likeCnt")
        val uid = intent.getSerializableExtra("uid") //내가 쓴 글인지 확인 위함
        var commentCnt : Int =0

        var tv_profileimage = findViewById<ImageView>(R.id.feed_profile) //피드 쓴사람 프로필 이미지
        var tv_commentprofile = findViewById<ImageView>(R.id.feed_user_profile) //내 프로필 이미지 -> 내가 댓글 쓰므로
        var tv_title = findViewById<TextView>(R.id.feed_title)
        var tv_name = findViewById<TextView>(R.id.feed_name)
        var tv_content = findViewById<TextView>(R.id.feed_content)
        var tv_hashtag = findViewById<TextView>(R.id.feed_hashtag)
        var tv_commentCnt = findViewById<TextView>(R.id.feed_comment_cnt)
        var tv_like_cnt = findViewById<TextView>(R.id.feed_like_cnt)
        var feed_timeStamp = findViewById<TextView>(R.id.feed_timestamp)
        var feed_like_img = findViewById<ImageView>(R.id.feed_like_img)
        var feed_image = findViewById<ImageView>(R.id.feedImageInDetail)

        var commentList = arrayListOf<Feeds.Comment>()


        Glide.with(this).load(feedImage).into(feed_image) //피드 이미지
        //comment count
        firestore?.collection("Feeds")?.document(contentUid!!)?.addSnapshotListener{
                documentSnapshot, FirebaseFirestoreException ->

            val doc = documentSnapshot?.toObject(Feeds::class.java)

            Log.v("doc",doc.toString())

            if(doc == null) //피드가 지워지거나 없음
            {
                commentList.clear()
                rv_comments.adapter!!.notifyDataSetChanged()
            }
            else
            {
                commentCnt = doc!!.commentCnt
                tv_commentCnt.setText(commentCnt.toString())
            }
        }

        //미리 하트가 비었는가 찼는가
        firestore?.collection("Feeds")?.document(contentUid!!)?.addSnapshotListener{
            documentSnapshot, FirebaseFirestoreException ->

            var doc = documentSnapshot?.toObject(Feeds::class.java)
            if(doc?.likes!!.containsKey(auth?.uid.toString())) //좋아요 눌렀을 경우
            {
                feed_like_img.setImageResource(R.drawable.like)
            }
            else
            {
                feed_like_img.setImageResource(R.drawable.blank_like) //안눌렀을 경우
            }
            //좋아요 수 동기화
            tv_like_cnt.setText(doc.likeCnt.toString())
        }

        //피드 프로필사진
        if(imageUrl == null) //기본 이미지
            Glide.with(this).load(R.drawable.default_profilephoto).apply(
                RequestOptions().circleCrop()).into(tv_profileimage) //유저 프로필 이미지
        else
        {
            Glide.with(this).load(imageUrl).apply(
                RequestOptions().circleCrop()).into(tv_profileimage) //유저 프로필 이미지
        }

        //내 프로필 사진
        if(uid!!.equals(auth?.uid.toString())) //내가 쓴 글일 경우
        {
            //그냥 피드이미지 갖다쓰면 됨
            if(imageUrl == null) //기본 이미지
                Glide.with(this).load(R.drawable.default_profilephoto).apply(
                    RequestOptions().circleCrop()).into(tv_commentprofile) //유저 프로필 이미지
            else
            {
                Glide.with(this).load(imageUrl).apply(
                    RequestOptions().circleCrop()).into(tv_commentprofile) //유저 프로필 이미지
            }
        }
        else //내피드 아님..
        {
            firestore?.collection("User")?.document(auth?.uid.toString())?.addSnapshotListener{
                documentSnapshot, firebaseFirestoreException ->

                val doc = documentSnapshot?.toObject(userDTO::class.java)

                if(doc!!.imageUrl == null) //기본 이미지
                    Glide.with(this).load(R.drawable.default_profilephoto).apply(
                        RequestOptions().circleCrop()).into(tv_commentprofile) //유저 프로필 이미지
                else
                {
                    Glide.with(this).load(doc!!.imageUrl).apply(
                        RequestOptions().circleCrop()).into(tv_commentprofile) //유저 프로필 이미지
                }
            }
        }

        feed_like_img.setOnClickListener {
            likeEvent()
        }


        tv_name.setText(strName.toString())
        tv_content.setText(content.toString())
        tv_hashtag.setText("#"+hashtag.toString())
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
            val comment = Feeds.Comment()

            comment.strName = user!!.displayName
            comment.comment = feed_user_comment.text.toString()
            comment.uid = user!!.uid
            comment.timestamp = System.currentTimeMillis()

            Log.v("commentSize Before", commentList.size.toString())
            firestore?.collection("Feeds")?.document(contentUid!!)?.collection("Comments")?.document()?.set(comment)
            Log.v("commentSize After", commentList.size.toString())
            firestore?.collection("Feeds")?.document(contentUid!!)?.update("commentCnt",(commentCnt+1))


            feed_user_comment.setText("")

        }

        commentSnapshot = firestore?.collection("Feeds")?.document(contentUid!!)?.collection("Comments")?.addSnapshotListener{
                querySnapshot, firebaseFirestoreException ->
            commentList.clear()
            if(querySnapshot == null)
                return@addSnapshotListener
            for(snapshot in querySnapshot?.documents)
            {
                commentList.add(snapshot.toObject(Feeds.Comment::class.java)!!)
            }
            rv_comments.adapter = CommentAdapter(commentList)
            rv_comments.adapter!!.notifyDataSetChanged()
        }

        rv_comments = findViewById(R.id.rv_comments)
        rv_comments.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rv_comments.setHasFixedSize(true)

    }

    override fun onStop() {
        super.onStop()
        commentSnapshot?.remove()
    }

    fun likeEvent()
    {
        var tsDoc = firestore?.collection("Feeds")?.document(contentUid!!)
        firestore?.runTransaction{
                transaction ->

            val uid = auth?.uid.toString()
            val feedDTO = transaction.get(tsDoc!!).toObject(Feeds::class.java)

            if(feedDTO!!.likes.containsKey(uid)){ //이미 좋아요 한 경우 -> 좋아요 철회
                feedDTO?.likeCnt = feedDTO.likeCnt-1
                feedDTO?.likes.remove(uid)
            }
            else //좋아요 아직 안함 -> 좋아요 하기
            {
                feedDTO.likes[uid] = uid
                feedDTO.likeCnt = feedDTO.likeCnt+1
            }

            transaction.set(tsDoc,feedDTO)
        }
    }
}