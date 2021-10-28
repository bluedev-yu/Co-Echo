package bluedev_yu.coecho.adapter

import android.content.Intent
import android.os.Bundle
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import bluedev_yu.coecho.FeedDetail
import bluedev_yu.coecho.R
import bluedev_yu.coecho.data.model.Feeds
import bluedev_yu.coecho.Fragment.FragmentMyPage
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList


class FeedAdapter(val feedList: ArrayList<Feeds>, val contentUidList : ArrayList<String>) : 
RecyclerView.Adapter<FeedAdapter.CustomViewHolder>(){

    var auth : FirebaseAuth? = null
    var firestore : FirebaseFirestore?= null //String 등 자료형 데이터베이스
    var firestorage : FirebaseStorage?= null //사진, GIF 등의 파일 데이터베이스

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)

        var iv_feed_share: ImageView

//        iv_feed_share = view.findViewById(R.id.feed_share)
//        iv_feed_share.setOnClickListener {
//            //하단 드로어
//            val bottomSheetDialog = BottomSheetDialog(
//                parent.context, R.style.BottomSheetDialogTheme
//            )
//
//            val bottomSheetView = LayoutInflater.from(parent.context).inflate(
//                R.layout.layout_bottom_sheet, parent.findViewById(R.id.bottomSheet) as LinearLayout?
//            )
//
//            bottomSheetDialog.setContentView(bottomSheetView)
//            bottomSheetDialog.show()
//        }

        return CustomViewHolder(view).apply {
            feedCardView.setOnClickListener {
                val curPos: Int = adapterPosition
                val feed: Feeds = feedList.get(curPos)

                val intent = Intent(parent.context, FeedDetail::class.java)
                intent.putExtra("content", feed.content)
                ContextCompat.startActivity(parent.context, intent, null)

            }
        }
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        var feeduid = feedList.get(position).uid

        holder.strName.text = feedList.get(position).strName //유저 이름
        //프로필사진
        if(feedList.get(position).imageUrl == null) //기본 이미지
            Glide.with(holder.itemView.context).load(R.drawable.default_profilephoto).apply(
                RequestOptions().circleCrop()).into(holder.profileImgUrl) //유저 프로필 이미지
        else
        {
            Glide.with(holder.itemView.context).load(feedList.get(position).imageUrl!!.toUri()).apply(
                RequestOptions().circleCrop()).into(holder.profileImgUrl) //유저 프로필 이미지
        }
        when(feedList.get(position).title) //칭호
        {
            0 -> holder.userTitle.setText(R.string.grade1)
            1 -> holder.userTitle.setText(R.string.grade2)
        }

        //holder.profileImgUrl.setImageResource(feedList.get(position).profileImgUrl)
        holder.strName.text = feedList.get(position).strName
        holder.profileImgUrl.setOnClickListener(object : View.OnClickListener {
            //해당 유저의 마이페이지를 띄우기
            override fun onClick(v: View?) {
                var fragmentUserPage = FragmentMyPage()
                var bundle = Bundle()
                bundle.putString("uid", feeduid)
                fragmentUserPage.arguments = bundle

                val activity = v!!.context as AppCompatActivity
                activity.supportFragmentManager.beginTransaction()
                    .replace(R.id.snsLayout, fragmentUserPage)
                    .addToBackStack(null)
                    .commit()
            }
        })

        var isheared : Boolean

        //미리 하트가 비었는가 찼는가
        if(feedList[position].likes.containsKey(auth?.uid.toString())) //좋아요 눌렀을 경우
        {
            holder.ivLike.setImageResource(R.drawable.like)
            isheared = true
        }
        else
        {
            holder.ivLike.setImageResource(R.drawable.blank_like) //안눌렀을 경우
            isheared = false
        }

        holder.timeStamp.text = feedList.get(position).timeStamp?.convertBoardTime()

        holder.content.text = feedList.get(position).content
        holder.hashtag.text = "#"+feedList.get(position).hashtag
        //holder.feedImgUrl.setImageResource(feedList.get(position).feedImgUrl)
        holder.likeCnt.text = feedList.get(position).likeCnt.toString()
        holder.commentCnt.text = feedList.get(position).commentCnt.toString()
        holder.feedCardView.setOnClickListener {
            val intent = Intent(holder.itemView?.context, FeedDetail::class.java)
            intent.putExtra("uid", feedList.get(position).uid)
            intent.putExtra("timeStamp",feedList.get(position).timeStamp)
            intent.putExtra("content", feedList.get(position).content)
            intent.putExtra("hashtag", feedList.get(position).hashtag)
            intent.putExtra("likeCnt", feedList.get(position).likeCnt)
            intent.putExtra("strName", feedList.get(position).strName)
            intent.putExtra("imageUrl",feedList.get(position).imageUrl)
            intent.putExtra("title",feedList.get(position).title)
            intent.putExtra("commentCnt",feedList.get(position).commentCnt)

            intent.putExtra("contentUid",contentUidList.get(position))
            ContextCompat.startActivity(holder.itemView?.context, intent, null)
        }

        holder.isLikeClicked.setOnClickListener {
            likeEvent(position)
        }
    }

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

//    fun currentTimeToLong(): Long? {
//        return System.currentTimeMillis()
//    }
//
//    fun convertDateToLong(date: String): Long? {
//        val df = SimpleDateFormat("yyyy.MM.dd HH:mm")
//        return df.parse(date).time
//    }

    //타임스탬프 -> 시간:분 (08:23)
    fun Long.convertHourMinute(): String = DateFormat.format("HH:mm", this).toString()

    //타임스탬프 -> 날짜 시간:분 (2021.01.07 08:23)
    fun Long.convertDateTimeMinute(): String =
        DateFormat.format("yyyy.MM.dd HH:mm", this).toString()

    private fun likeEvent(position: Int)
    {
        var tsDoc = firestore?.collection("Feeds")?.document(contentUidList[position])
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

    override fun getItemCount(): Int {
        return feedList.size
    }

    class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var profileImgUrl = itemView.findViewById<ImageView>(R.id.feed_profile) //프로필 이미지
        var userTitle = itemView.findViewById<TextView>(R.id.feed_title) //칭호
        var strName = itemView.findViewById<TextView>(R.id.feed_name) //이름
        var timeStamp = itemView.findViewById<TextView>(R.id.feed_timestamp) //타임스탬프
        var content = itemView.findViewById<TextView>(R.id.feed_content) //피드 글
        var hashtag = itemView.findViewById<TextView>(R.id.feed_hashtag) //해시태그
        //var feedImgUrl = itemView.findViewById<ImageView>(R.id.iv_image) //피드 이미지
        var ivLike = itemView.findViewById<ImageView>(R.id.feed_like_img) //좋아요 하트
        var isLikeClicked = itemView.findViewById<ImageView>(R.id.feed_like_img) //좋아요 하트
        var likeCnt = itemView.findViewById<TextView>(R.id.feed_like_cnt) //좋아요 수
        var commentCnt = itemView.findViewById<TextView>(R.id.feed_comment_cnt) //댓글 수
        var feedCardView = itemView.findViewById<CardView>(R.id.feed_cardview) //피드 카드뷰
    }
}
