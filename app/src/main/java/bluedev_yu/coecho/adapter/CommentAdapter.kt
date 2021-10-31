package bluedev_yu.coecho.adapter

import android.app.Activity
import android.os.Bundle
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import bluedev_yu.coecho.R
import bluedev_yu.coecho.fragment.FragmentMyPage
import bluedev_yu.coecho.data.model.Feeds
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

class CommentAdapter(val commentList: ArrayList<Feeds.Comment>): RecyclerView.Adapter<CommentAdapter.CustomViewHolder>(){

    var auth : FirebaseAuth? = null
    var firestore : FirebaseFirestore?= null //String 등 자료형 데이터베이스

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentAdapter.CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_comment, parent, false)
        return CustomViewHolder(view)
    }

    override fun onBindViewHolder(holder: CommentAdapter.CustomViewHolder, position: Int) {
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        firestore?.collection("User")?.document(commentList[position].uid!!)?.addSnapshotListener{
            documentSnapshot, firebaseFirestoreException ->

            if(documentSnapshot?.data != null)
            {
                val imageUrl = documentSnapshot?.data!!["imageUrl"]
                //프로필사진

                if(imageUrl == null) //기본 이미지
                    Glide.with(holder.itemView.context).load(R.drawable.default_profilephoto).apply(
                        RequestOptions().circleCrop()).into(holder.profileImgUrl) //유저 프로필 이미지
                else
                {
                    Glide.with(holder.itemView.context).load(imageUrl).apply(
                        RequestOptions().circleCrop()).into(holder.profileImgUrl) //유저 프로필 이미지
                }
            }
        }

        holder.userId.text = commentList.get(position).strName
        holder.userId.setOnClickListener (object: View.OnClickListener{
            //해당 유저의 마이페이지를 띄우기
            override fun onClick(v: View?) {
                var fragmentUserPage = FragmentMyPage()
                var bundle = Bundle()
                bundle.putString("uid", commentList.get(position).uid.toString())
                fragmentUserPage.arguments = bundle

                val activity = v!!.context as AppCompatActivity
                activity.supportFragmentManager.beginTransaction()
                    .replace(R.id.layout_feed_detail, fragmentUserPage)
                    .addToBackStack(null)
                    .commit()
            }
        })
        holder.comment.text = commentList.get(position).comment
        holder.timeStamp.text = commentList.get(position).timestamp?.convertBoardTime()
    }

    override fun getItemCount(): Int {
return commentList.size
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

    //타임스탬프 -> 시간:분 (08:23)
    fun Long.convertHourMinute(): String = DateFormat.format("HH:mm", this).toString()

    //타임스탬프 -> 날짜 시간:분 (2021.01.07 08:23)
    fun Long.convertDateTimeMinute(): String =
        DateFormat.format("yyyy.MM.dd HH:mm", this).toString()


    class CustomViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var profileImgUrl = itemView.findViewById<ImageView>(R.id.comment_profileImage)
        var userId = itemView.findViewById<TextView>(R.id.tv_commentUserId) //댓글단 사람 아이디
        var comment = itemView.findViewById<TextView>(R.id.tv_commentText) //댓글 내용
        var timeStamp = itemView.findViewById<TextView>(R.id.commentTimestamp)
    }

}