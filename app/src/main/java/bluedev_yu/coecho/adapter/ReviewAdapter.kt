package bluedev_yu.coecho.adapter

import android.media.Image
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import bluedev_yu.coecho.DB_Place
import bluedev_yu.coecho.R
import bluedev_yu.coecho.data.model.ReviewDTO
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

class ReviewAdapter(val reviewList: ArrayList<ReviewDTO>): RecyclerView.Adapter<ReviewAdapter.CustomViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewAdapter.CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_review, parent, false)
        return CustomViewHolder(view)
    }


    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.star.rating = reviewList.get(position).star!!
        var pid = reviewList.get(position).pid
        if (pid != null) {
            CoroutineScope(Dispatchers.Main).launch {
                var placeName = DB_Place().placeAtrribute(pid)
                holder.place.setText(placeName)
            }
        }
        if (reviewList.get(position).title!! < 20) //칭호
        {
            holder.title.setText(R.string.grade1)
        } else if (reviewList.get(position).title!! < 40) //칭호
        {
            holder.title.setText(R.string.grade2)
        } else
            holder.title.setText(R.string.grade3)
        holder.strName.text = reviewList.get(position).strName

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



        holder.timeStamp.text = reviewList.get(position).timestamp?.convertBoardTime()


        holder.content.text = reviewList.get(position).content

        if(reviewList.get(position).reviewImage.equals(null)){
            //리뷰 이미지가 없을 경우
            holder.reviewImage.visibility = View.GONE
        }else{
            //리뷰 이미지가 있을 경우
            Glide.with(holder.itemView.context).load(reviewList.get(position).reviewImage!!).into(holder.reviewImage) //피드 이미지

        }

        //프로필사진
        if (reviewList.get(position).imageUrl == null) //기본 이미지
            Glide.with(holder.itemView.context).load(R.drawable.default_profilephoto).apply(
                RequestOptions().circleCrop()
            ).into(holder.profileImage) //유저 프로필 이미지
        else {
            Glide.with(holder.itemView.context).load(reviewList.get(position).imageUrl!!.toUri())
                .apply(
                    RequestOptions().circleCrop()
                ).into(holder.profileImage) //유저 프로필 이미지
        }
    }

    override fun getItemCount(): Int {
        return reviewList.size
    }

    class CustomViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val star = itemView.findViewById<RatingBar>(R.id.review_star) //별점
        val title = itemView.findViewById<TextView>(R.id.review_title) //사용자 칭호
        val place = itemView.findViewById<TextView>(R.id.review_place)//가게 이름
        val strName = itemView.findViewById<TextView>(R.id.review_name) //사용자 이름
        val timeStamp = itemView.findViewById<TextView>(R.id.review_timestamp) //리뷰 타임스탬프
        val content = itemView.findViewById<TextView>(R.id.review_content) //리뷰 내용
        val profileImage = itemView.findViewById<ImageView>(R.id.review_profileImage)
        val reviewImage = itemView.findViewById<ImageView>(R.id.reviewImage)
    }
}