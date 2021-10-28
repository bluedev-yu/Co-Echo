package bluedev_yu.coecho.adapter

import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import bluedev_yu.coecho.R
import bluedev_yu.coecho.data.model.ReviewDTO
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class ReviewAdapter(val reviewList: ArrayList<ReviewDTO>): RecyclerView.Adapter<ReviewAdapter.CustomViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewAdapter.CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_review, parent, false)
        return CustomViewHolder(view)
    }


    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.star.rating = reviewList.get(position).star!!
        if(reviewList.get(position).title!! <20) //칭호
        {
            holder.title.setText(R.string.grade1)
        }
        else if(reviewList.get(position).title!! <40) //칭호
        {
            holder.title.setText(R.string.grade2)
        }
        else
            holder.title.setText(R.string.grade3)
        holder.strName.text = reviewList.get(position).strName
        holder.timeStamp.text = reviewList.get(position).timestamp.toString()
        holder.content.text = reviewList.get(position).content

        //프로필사진
        if(reviewList.get(position).imageUrl == null) //기본 이미지
            Glide.with(holder.itemView.context).load(R.drawable.default_profilephoto).apply(
                RequestOptions().circleCrop()).into(holder.profileImage) //유저 프로필 이미지
        else
        {
            Glide.with(holder.itemView.context).load(reviewList.get(position).imageUrl!!.toUri()).apply(
                RequestOptions().circleCrop()).into(holder.profileImage) //유저 프로필 이미지
        }
    }

    override fun getItemCount(): Int {
        return reviewList.size
    }

    class CustomViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val star = itemView.findViewById<RatingBar>(R.id.review_star) //별점
        val title = itemView.findViewById<TextView>(R.id.review_title) //사용자 칭호
        val strName = itemView.findViewById<TextView>(R.id.review_name) //사용자 이름
        val timeStamp = itemView.findViewById<TextView>(R.id.review_timestamp) //리뷰 타임스탬프
        val content = itemView.findViewById<TextView>(R.id.review_content) //리뷰 내용
        val profileImage = itemView.findViewById<ImageView>(R.id.review_profileImage)
    }
}