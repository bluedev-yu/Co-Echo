package bluedev_yu.coecho

import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.format.DateFormat
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
import java.util.*
import java.util.concurrent.TimeUnit

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

        /*
        * 수정사항
        * 1. timestamp 출력
        * 2. 좋아요 여부도 같이 가져와야함
        * 3. comment
        * 4. title도 가져와야 함
        * 5. 해시태그 앞에 # 달건가?
        * 7. 좋아요 버튼이랑 다시 구현해야 함.
        * */


        //클릭된 피드의 정보로 초기화
        val imageUrl = intent.getSerializableExtra("imageUrl")
        val title = intent.getSerializableExtra("title")
        val strName = intent.getSerializableExtra("strName")
        val timeStamp = intent.getLongExtra("timeStamp", 0)
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

        //comment count
        firestore?.collection("Feeds")?.document(contentUid!!)?.addSnapshotListener{
                documentSnapshot, FirebaseFirestoreException ->

            val doc = documentSnapshot?.toObject(Feeds::class.java)
            commentCnt = doc!!.commentCnt
            tv_commentCnt.setText(commentCnt.toString())
        }

        //intent.getSerializableExtra("commentCnt")

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

        //타임스탬프 -> 시간:분 (08:23)
        fun Long.convertHourMinute(): String = DateFormat.format("HH:mm", this).toString()

        //타임스탬프 -> 날짜 시간:분 (2021.01.07 08:23)
        fun Long.convertDateTimeMinute(): String =
            DateFormat.format("yyyy.MM.dd HH:mm", this).toString()

        fun Long.convertBoardTime(): String {
            val now = Calendar.getInstance(Locale.KOREA)
            val diffInMillis = now.timeInMillis - this //현재와의 밀리초 차이
            //오늘 기준 00시
            now.set(Calendar.HOUR_OF_DAY, 0)
            now.set(Calendar.MINUTE, 0)
            now.set(Calendar.SECOND, 0)
            now.set(Calendar.MILLISECOND, 0)
            //toDays()로 하면 날이 아예 24시간이 자나야 날 수로쳐서 계산이 이상하게됨
            val todayOfHour =
                TimeUnit.MILLISECONDS.toHours(now.timeInMillis)
            val thisOfHour = TimeUnit.MILLISECONDS.toHours(this)
            val currentDiffHour = diffInMillis / 1000 / 60 / 60L //현재와 시간 차이
            return if (currentDiffHour < 1) { //현재시간 60분 전
                (diffInMillis / 1000 / 60L).toString() + "분 전"
            } else {
                if (todayOfHour < thisOfHour || (
                            thisOfHour - todayOfHour in 0..23)
                ) { //오늘
                    this.convertHourMinute() //(08:23)
                } else { // 하루 이상 지난 날
                    this.convertDateTimeMinute() // (2021.01.07 08:23)
                }
            }
        }

        feed_timeStamp.setText(timeStamp.convertBoardTime())

        when(title) //칭호
        {
            0 -> tv_title.setText(R.string.grade1)
            1 -> tv_title.setText(R.string.grade2)
        }

        var commentList = arrayListOf<Feeds.Comment>()
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